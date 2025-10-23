package org.mtcc.llm.webflux.user.controller.dto;

import java.io.Serial;
import java.io.Serializable;

public record UserChatResponse(
	String response
) implements Serializable {
	@Serial
	private static final long serialVersionUID = -2479640483541743119L;

}
