package org.mtcc.llm.webflux.user.service;

import java.util.Map;

import org.mtcc.llm.webflux.exception.BaseException;
import org.mtcc.llm.webflux.exception.ErrorCode;
import org.mtcc.llm.webflux.llmclient.model.jsonformat.AnswerListResponseDto;
import org.mtcc.llm.webflux.llmclient.service.LlmWebClientService;
import org.mtcc.llm.webflux.user.controller.dto.LlmModel;
import org.mtcc.llm.webflux.user.controller.dto.LlmType;
import org.mtcc.llm.webflux.user.controller.dto.UserChatResponse;
import org.mtcc.llm.webflux.user.controller.dto.UserCotChatResponse;
import org.mtcc.llm.webflux.user.service.dto.LlmChatRequest;
import org.mtcc.llm.webflux.user.service.dto.LlmChatResponse;
import org.mtcc.llm.webflux.user.service.dto.UserChatCommand;
import org.mtcc.llm.webflux.user.service.dto.UserCotChatCommand;
import org.mtcc.llm.webflux.util.ChatUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserChatServiceImpl implements UserChatService {
	private final Map<LlmType, LlmWebClientService> llmServiceMap;
	private final ObjectMapper objectMapper;

	@Override
	public Mono<UserChatResponse> getOneShotChat(UserChatCommand command) {
		LlmChatRequest request = LlmChatRequest.of(command, "적절히 응답해주라~");
		Mono<LlmChatResponse> llmChatResponse = llmServiceMap.get(command.llmModel().getLlmType())
			.getChatCompletion(request);

		return llmChatResponse
			.map(response -> new UserChatResponse(response.llmResponse()));

		/**
		 * mono 의 흐름으로 통제하는 형태의 구현
		 */
		//		return Mono.defer(() -> {
		// 			LlmChatRequest request = LlmChatRequest.from(command, "적절히 응답해주라~");
		// 			Mono<LlmChatResponse> llmChatResponse = llmServiceMap.get(command.llmModel().getLlmType())
		// 				.getChatCompletion(request);
		//
		// 			return llmChatResponse
		// 				.map(response -> new UserChatResponse(response.llmResponse()));
		// 		});
	}

	@Override
	public Flux<UserChatResponse> getOneShotChatStream(UserChatCommand command) {
		LlmChatRequest request = LlmChatRequest.of(command, "적절히 응답해주라~");
		Flux<LlmChatResponse> llmChatResponse = llmServiceMap.get(command.llmModel().getLlmType())
			.getChatCompletionStream(request);
		return llmChatResponse.map(response -> new UserChatResponse(response.llmResponse()));
	}

	/**
	 * 1 단계: 문제를 이해하기
	 * 2 단계 문제를 단계별로 풀어가기
	 * 3 단계: 최종 응답
	 * <p>
	 * 1. 사용자의 요청을 효율적으로 분석하기 위한 단계를 LLM 에게 물어봄
	 * -> answerList: 분석 단계를 LLM 이 응답
	 * 2. 분석 단계 별로 LLM 에게 요청을 보내어 상세하게 분석
	 * 3. 단계별로 분석된 결과를 종합하여 최종 응답
	 */
	@Override
	public Flux<UserCotChatResponse> getCotChat(UserCotChatCommand command) {
		return Flux.create(sink -> {
			String userRequest = command.request();
			LlmModel llmModel = command.llmModel();

			String establishingThoughtChainPrompt = String.format(
				"""
					다음은 사용자의 입력입니다: "%s"
					사용자에게 체계적으로 답변하기 위해 어떤 단계들이 필요할지 정리해주세요.
					""", userRequest
			);

			String establishingThoughtChainSystemPrompt =
				"""
					아래처럼 List<String> answerList 형태를 가지는 JSON FORMAT 으로 응답주세요.
					<JSON SCHEMA>
					{
						"answerList": [ "", ...]
					}
					</JSON SCHEMA>
					""";

			LlmChatRequest llmChatRequest = new LlmChatRequest(establishingThoughtChainPrompt,
				establishingThoughtChainSystemPrompt, true, llmModel);
			LlmWebClientService llmWebClientService = llmServiceMap.get(llmModel.getLlmType());

			// LLM 이 정리한 체계적 답변을 위한 단계
			Mono<AnswerListResponseDto> cotStepListMono = llmWebClientService
				.getChatCompletion(llmChatRequest)
				.map(response -> {
					String llmResponse = response.llmResponse();
					log.info(">>>>>>> here: {}", llmResponse);
					String extractedJsonString = ChatUtils.extractJsonString(llmResponse);

					try {
						// sink.next(new UserCotChatResponse("필요한 작업 단계 분석", answerListResponseDto.toString()));
						return objectMapper.readValue(extractedJsonString, AnswerListResponseDto.class);
					} catch (JsonProcessingException e) {
						throw new BaseException(
							"[JsonParseError] json parse error. extractedJsonString: %s".formatted(extractedJsonString),
							ErrorCode.JSON_PARSE_ERROR);
					}
				})
				.doOnNext(publishedData ->
					sink.next(new UserCotChatResponse("필요한 작업 단계 분석", publishedData.toString())));

			Flux<String> cotStepFlux = cotStepListMono.flatMapMany(cotStep -> Flux.fromIterable(cotStep.answerList()));

			// LLM 이 사용자의 입력을 체계적인 단계로 정리한 결과
			Flux<String> analyzedCotStep = cotStepFlux.flatMapSequential(cotStep -> {
				String cotStepRequestPrompt =
					"""
							다음은 사용자의 입력입니다: %s
						
							사용자의 요구를 다음 단계에 따라 분석해주세요: %s
						""".formatted(userRequest, cotStep);
				return llmWebClientService
					.getChatCompletionWithCatchException(new LlmChatRequest(cotStepRequestPrompt, "", false, llmModel))
					.map(LlmChatResponse::llmResponse);
			}).doOnNext(publishedData -> sink.next(new UserCotChatResponse(publishedData, "단계별 분석")));

			// LLM 이 체계적으로 정리한 사용자의 입력을 LLM 에게 다시 보내서 답변을 수신
			Mono<String> finalAnswerMono = analyzedCotStep.collectList().flatMap(stepPromptList -> {
				String concatStepPrompt = String.join("/n", stepPromptList);
				String finalAnswerPrompt = """
					다음은 사용자의 입력입니다 : %s
					아래 사항들을 참고, 분석하여 사용자의 입력에 대한 최종 답변을 해주세요:
					%s
					""".formatted(userRequest, concatStepPrompt);

				return llmWebClientService
					.getChatCompletionWithCatchException(new LlmChatRequest(finalAnswerPrompt, "", false, llmModel))
					.map(LlmChatResponse::llmResponse);
			});

			finalAnswerMono.subscribe(finalAnswer -> {
				sink.next(new UserCotChatResponse(finalAnswer, "최종 응답"));
				sink.complete();
			}, error -> {
				log.error("[COT] cot response error", error);
				sink.error(error);
			});
		});
	}
}
