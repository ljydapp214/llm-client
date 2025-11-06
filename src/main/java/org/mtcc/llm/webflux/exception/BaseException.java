package org.mtcc.llm.webflux.exception;

import java.io.Serial;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -5181307199942773996L;

	private final ErrorCode errorCode;

	public BaseException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return "Code: %s, Message: %s".formatted(errorCode.getCode(), super.getMessage());
	}
}
