package org.mtcc.llm.webflux.user.controller.dto;

import java.io.Serial;
import java.io.Serializable;

import org.mtcc.llm.webflux.exception.CommonError;

public record UserCotChatResponse(
	String response,
	String title,
	CommonError error
) implements Serializable {
	@Serial
	private static final long serialVersionUID = -2479640483541743119L;

	public UserCotChatResponse(String response, String title) {
		this(response, title, null);
	}
}
