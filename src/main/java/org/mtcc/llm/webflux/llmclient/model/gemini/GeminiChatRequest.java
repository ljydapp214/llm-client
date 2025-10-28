package org.mtcc.llm.webflux.llmclient.model.gemini;

import java.io.Serializable;
import java.util.List;

import org.mtcc.llm.webflux.user.service.dto.LlmChatRequest;

public record GeminiChatRequest(
	List<GeminiContent> contents,
	GeminiContent systemInstruction,
	GeminiGenerationConfig generationConfig
) implements Serializable {
	public static GeminiChatRequest from(LlmChatRequest llmChatRequest) {
		if (llmChatRequest.useJson()) {
			return new GeminiChatRequest(
				List.of(
					new GeminiContent(List.of(new GeminiPart(llmChatRequest.userRequest())), GeminiMessageRole.USER)),
				new GeminiContent(List.of(new GeminiPart(llmChatRequest.userRequest())), GeminiMessageRole.MODEL),
				GeminiGenerationConfig.asDefault()
			);
		}

		return new GeminiChatRequest(
			List.of(
				new GeminiContent(List.of(new GeminiPart(llmChatRequest.userRequest())), GeminiMessageRole.USER)),
			new GeminiContent(List.of(new GeminiPart(llmChatRequest.userRequest())), GeminiMessageRole.MODEL),
			null
		);
	}
}
