package org.mtcc.llm.webflux.exception;

public record CommonError(
	String errorCode,
	String errorMessage
) {
	public static CommonError toInternalServerError(String message) {
		return new CommonError("500", message);
	}
}
