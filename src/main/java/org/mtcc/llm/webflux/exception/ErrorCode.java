package org.mtcc.llm.webflux.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	GEMINI_RESPONSE_ERROR(1),
	GPT_RESPONSE_ERROR(2),
	;

	private final int code;

	ErrorCode(int code) {
		this.code = code;
	}
}
