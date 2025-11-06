package org.mtcc.llm.webflux.llmclient.service;

import org.mtcc.llm.webflux.exception.BaseException;
import org.mtcc.llm.webflux.exception.ErrorCode;
import org.mtcc.llm.webflux.llmclient.model.gemini.GeminiChatRequest;
import org.mtcc.llm.webflux.llmclient.model.gemini.GeminiChatResponse;
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

@RequiredArgsConstructor
@Slf4j
@Service
public class GeminiWebClientService implements LlmWebClientService {

	private final WebClient webClient;

	@Value("${llm.gemini.key}")
	private String geminiApiKey;

	@Override
	public Mono<LlmChatResponse> getChatCompletion(LlmChatRequest request) {
		GeminiChatRequest geminiChatRequest = GeminiChatRequest.from(request);
		return webClient.post()
			.uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
				+ geminiApiKey)
			.bodyValue(geminiChatRequest)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, (clientResponse -> clientResponse
				.bodyToMono(String.class)
				.flatMap(body -> {
					log.error("Error Response: {}", body);
					return Mono.error(
						new BaseException("API 요청 실패: %s".formatted(body), ErrorCode.GEMINI_RESPONSE_ERROR));
				})
			))
			.bodyToMono(GeminiChatResponse.class)
			.map(LlmChatResponse::from);
	}

	@Override
	public LlmType getLlmType() {
		return LlmType.GEMINI;
	}

	/**
	 * ChatGPT 와 다르게 Gemini 는 요청 URL 을 변경해서 Stream 통신임을 나타냄
	 * 재미나이는 마지막 메시지에도 finish reason이랑 마지막 데이터를 같이 붙여서 보냄, 생성자에 들어가도 싱글 텍스트에서 NPE 가 발생하지 않음
	 * Delta 같은 필드를 따로 사용하지 않고 getText 필드를 그대로 사용
	 */
	@Override
	public Flux<LlmChatResponse> getChatCompletionStream(LlmChatRequest request) {
		GeminiChatRequest geminiChatRequest = GeminiChatRequest.from(request);
		return webClient.post()
			.uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:streamGenerateContent?key="
				+ geminiApiKey)
			.bodyValue(geminiChatRequest)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, (clientResponse -> clientResponse
				.bodyToMono(String.class)
				.flatMap(body -> {
					log.error("Error Response: {}", body);
					return Mono.error(
						new BaseException("API 요청 실패: %s".formatted(body), ErrorCode.GEMINI_RESPONSE_ERROR));
				})
			))
			.bodyToFlux(GeminiChatResponse.class)
			.map(LlmChatResponse::from);
	}
}
