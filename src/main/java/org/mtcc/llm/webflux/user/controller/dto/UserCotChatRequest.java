package org.mtcc.llm.webflux.user.controller.dto;

import java.io.Serial;
import java.io.Serializable;

import org.mtcc.llm.webflux.user.service.dto.UserCotChatCommand;

public record UserCotChatRequest(
	String request,
	LlmModel llmModel,
	String title
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 4995931142869792388L;

	public UserCotChatCommand toCommand() {
		return new UserCotChatCommand(this.request, this.title, this.llmModel);
	}
}
