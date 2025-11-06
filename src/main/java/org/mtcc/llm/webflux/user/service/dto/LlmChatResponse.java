package org.mtcc.llm.webflux.user.service.dto;

import java.io.Serializable;

import org.mtcc.llm.webflux.exception.CommonError;
import org.mtcc.llm.webflux.llmclient.model.gemini.GeminiChatResponse;
import org.mtcc.llm.webflux.llmclient.model.gpt.GptChatResponse;

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
}
