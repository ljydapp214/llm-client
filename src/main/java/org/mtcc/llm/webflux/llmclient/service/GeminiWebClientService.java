package org.mtcc.llm.webflux.llmclient.service;

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
					return Mono.error(new RuntimeException("API 요청 실패: " + body));
				})
			))
			.bodyToMono(GeminiChatResponse.class)
			.map(LlmChatResponse::from);
	}

	@Override
	public LlmType getLlmType() {
		return LlmType.GEMINI;
	}
}
