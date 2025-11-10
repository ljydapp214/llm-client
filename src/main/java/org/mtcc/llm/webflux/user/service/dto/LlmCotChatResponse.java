package org.mtcc.llm.webflux.user.service.dto;

import java.io.Serializable;

import org.mtcc.llm.webflux.exception.CommonError;

public record LlmCotChatResponse(
	String llmResponse,
	String title,
	CommonError error
) implements Serializable {
	public LlmCotChatResponse(CommonError error) {
		this("", "", error);
	}

	public LlmCotChatResponse(String llmResponse, String title) {
		this(llmResponse, title, null);
	}
}
