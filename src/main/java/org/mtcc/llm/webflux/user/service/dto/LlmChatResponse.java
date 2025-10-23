package org.mtcc.llm.webflux.user.service.dto;

import java.io.Serial;
import java.io.Serializable;

public record LlmChatResponse(String llmResponse) implements Serializable {
	@Serial
	private static final long serialVersionUID = -2479640483541743119L;
}
