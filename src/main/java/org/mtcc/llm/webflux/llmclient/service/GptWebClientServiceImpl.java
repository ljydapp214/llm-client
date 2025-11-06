package org.mtcc.llm.webflux.llmclient.service;

import org.mtcc.llm.webflux.exception.BaseException;
import org.mtcc.llm.webflux.exception.ErrorCode;
import org.mtcc.llm.webflux.llmclient.model.gpt.GptChatRequest;
import org.mtcc.llm.webflux.llmclient.model.gpt.GptChatResponse;
import org.mtcc.llm.webflux.user.controller.dto.LlmType;
import org.mtcc.llm.webflux.user.service.dto.LlmChatRequest;
import org.mtcc.llm.webflux.user.service.dto.LlmChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptWebClientServiceImpl implements LlmWebClientService {
	private final WebClient webClient;

	@Value("${llm.gpt.api-key}")
	private String apiKey;

	@Override
	public Mono<LlmChatResponse> getChatCompletion(LlmChatRequest request) {
		return webClient.post()
			.uri("https://api.openai.com/v1/chat/completions")
			.header("Authorization", "Bearer " + apiKey)
			.bodyValue(request.toGptChatRequest())
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse
				.bodyToMono(String.class)
				.flatMap(body -> {
					log.error("Error response: {}", body);
					return Mono.error(
						new BaseException("API 요청 실패: %s".formatted(body), ErrorCode.GPT_RESPONSE_ERROR)
					);
				}))
			.bodyToMono(GptChatResponse.class)
			.map(LlmChatResponse::from);
	}

	@Override
	public LlmType getLlmType() {
		return LlmType.GPT;
	}

	/**
	 * ChatGPT 는
	 * 마지막 response 에서 delta 가 null 로 세팅됨
	 * 마지막 response 를 제외한 나머지 response 에서 finish_reason 이 null 임
	 * takeWhile(): 조건에 만족하지 않을 때까지 데이터를 방출, 만족하지 않는 순간 데이터 방출을 끝냄.
	 */
	@Override
	public Flux<LlmChatResponse> getChatCompletionStream(LlmChatRequest request) {
		GptChatRequest gptChatRequest = request.toGptChatRequest();
		GptChatRequest streamRequest = gptChatRequest.convertToStream();

		return webClient.post()
			.uri("https://api.openai.com/v1/chat/completions")
			.header("Authorization", "Bearer " + apiKey)
			.bodyValue(streamRequest)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, clientResponse -> clientResponse
				.bodyToMono(String.class)
				.flatMap(body -> {
					log.error("Error response: {}", body);
					return Mono.error(
						new BaseException("API 요청 실패: %s".formatted(body), ErrorCode.GPT_RESPONSE_ERROR)
					);
				}))
			.bodyToFlux(GptChatResponse.class)
			.takeWhile(GptChatResponse::isNotFinish)
			.map(LlmChatResponse::fromDelta);
	}
}
