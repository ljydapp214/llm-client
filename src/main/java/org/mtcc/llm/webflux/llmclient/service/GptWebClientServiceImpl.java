package org.mtcc.llm.webflux.llmclient.service;

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
					return Mono.error(new RuntimeException("요청 실패: " + body));
				}))
			.bodyToMono(GptChatResponse.class)
			.map(response ->
				new LlmChatResponse(response.getSingleChoice().message().content()));
	}

	@Override
	public LlmType getLlmType() {
		return LlmType.GPT;
	}
}
