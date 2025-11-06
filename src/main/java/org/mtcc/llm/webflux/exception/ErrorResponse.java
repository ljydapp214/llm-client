package org.mtcc.llm.webflux.exception;

public record ErrorResponse(
	CommonError error
) {
}
