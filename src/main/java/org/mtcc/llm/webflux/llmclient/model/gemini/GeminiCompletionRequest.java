package org.mtcc.llm.webflux.llmclient.model.gemini;

import java.io.Serializable;

/**
 * @param content 채팅 내용
 */
public record GeminiCompletionRequest(
	GeminiMessageRole role,
	String content
) implements Serializable {
	public GeminiCompletionRequest from(String content) {
		return new GeminiCompletionRequest(role, content);
	}
}
