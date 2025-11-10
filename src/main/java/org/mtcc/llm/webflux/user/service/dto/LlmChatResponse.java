package org.mtcc.llm.webflux.user.service.dto;

import java.io.Serializable;
import java.util.Optional;

import org.mtcc.llm.webflux.exception.CommonError;
import org.mtcc.llm.webflux.llmclient.model.gemini.GeminiChatResponse;
import org.mtcc.llm.webflux.llmclient.model.gpt.GptChatResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record LlmChatResponse(
	String llmResponse,
	CommonError error
) implements Serializable {
	public LlmChatResponse(CommonError error) {
		this("", error);
	}

	public LlmChatResponse(String llmResponse) {
		this(llmResponse, null);
	}

	public static LlmChatResponse from(GptChatResponse gptChatResponse) {
		return new LlmChatResponse(gptChatResponse.getSingleChoice().message().content());
	}

	public static LlmChatResponse fromDelta(GptChatResponse gptChatResponse) {
		return new LlmChatResponse(gptChatResponse.getSingleChoice().delta().content());
	}

	public static LlmChatResponse from(GeminiChatResponse geminiChatResponse) {
		return new LlmChatResponse(geminiChatResponse.getSingleText());
	}

	public static LlmChatResponse of(CommonError error, Throwable throwable) {
		log.error("[LlmResponseError] error: {}", error, throwable);
		return new LlmChatResponse(error);
	}

	public boolean isValid() {
		return Optional.ofNullable(error).isEmpty();
	}
}
