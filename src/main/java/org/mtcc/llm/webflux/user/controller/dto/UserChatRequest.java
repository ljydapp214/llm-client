package org.mtcc.llm.webflux.user.controller.dto;

import java.io.Serial;
import java.io.Serializable;

import org.mtcc.llm.webflux.user.service.dto.UserChatCommand;

public record UserChatRequest(String request, LlmModel llmModel) implements Serializable {
	@Serial
	private static final long serialVersionUID = 4995931142869792388L;

	public UserChatCommand toCommand() {
		return new UserChatCommand(this.request, this.llmModel);
	}
}
