package org.mtcc.llm.webflux.user.controller.dto;

import java.io.Serial;
import java.io.Serializable;

import org.mtcc.llm.webflux.exception.CommonError;

public record UserChatResponse(
	String response,
	CommonError error
) implements Serializable {
	@Serial
	private static final long serialVersionUID = -2479640483541743119L;

	public UserChatResponse(String response) {
		this(response, null);
	}
}
