package org.mtcc.llm.webflux.user.service.dto;

import org.mtcc.llm.webflux.user.controller.dto.LlmModel;

public record UserCotChatCommand(
	String request,
	String title,
	LlmModel llmModel
) {
}
