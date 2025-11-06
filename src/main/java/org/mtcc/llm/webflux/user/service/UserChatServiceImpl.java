package org.mtcc.llm.webflux.user.service;

import java.util.Map;

import org.mtcc.llm.webflux.llmclient.service.LlmWebClientService;
import org.mtcc.llm.webflux.user.controller.dto.LlmType;
import org.mtcc.llm.webflux.user.controller.dto.UserChatResponse;
import org.mtcc.llm.webflux.user.service.dto.LlmChatRequest;
import org.mtcc.llm.webflux.user.service.dto.LlmChatResponse;
import org.mtcc.llm.webflux.user.service.dto.UserChatCommand;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserChatServiceImpl implements UserChatService {
	private final Map<LlmType, LlmWebClientService> llmServiceMap;

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
}
