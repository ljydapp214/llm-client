package org.mtcc.llm.webflux.llmclient.service;

import org.mtcc.llm.webflux.exception.BaseException;
import org.mtcc.llm.webflux.exception.CommonError;
import org.mtcc.llm.webflux.user.controller.dto.LlmType;
import org.mtcc.llm.webflux.user.service.dto.LlmChatRequest;
import org.mtcc.llm.webflux.user.service.dto.LlmChatResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*
느슨한 결합
이 인터페이스를 사용하는 서비스는 내부 구현체에 대해 전혀 몰라도 된다.

유연성 및 확장성
다양한 LlmType을 계속해서 자유롭게 추가 가능

다형성
원하는 webclient구현체를 자유롭게 선택해서 사용할 수 있음.
 */
public interface LlmWebClientService {
	default Mono<LlmChatResponse> getChatCompletionWithCatchException(LlmChatRequest request) {
		return getChatCompletion(request)
			.onErrorResume(exception -> {
				if (exception instanceof BaseException ex) {
					return Mono.just(
						new LlmChatResponse(new CommonError(ex.getErrorCode().toString(), ex.getMessage())));
				}
				return Mono.just(new LlmChatResponse(new CommonError("500", exception.getMessage())));
			});
	}

	Mono<LlmChatResponse> getChatCompletion(LlmChatRequest requestDto);

	LlmType getLlmType();

	Flux<LlmChatResponse> getChatCompletionStream(LlmChatRequest request);
	//gptWebClientService, GeminiWebClientService
}
