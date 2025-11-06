package org.mtcc.llm.webflux.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(BaseException.class)
	public Mono<ResponseEntity<ErrorResponse>> handleBaseException(BaseException exception,
		ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		log.error("[BaseException] Request URI: {}, Method: {}, Error: {}",
			request.getURI(),
			request.getMethod(),
			exception.getMessage(),
			exception
		);

		return Mono.just(
			ResponseEntity
				.internalServerError()
				.body(new ErrorResponse(CommonError.toInternalServerError(exception.getMessage())))
		);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public Mono<ErrorResponse> handleException(Exception exception, ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		log.error("[GeneralException] Request URI: {}, Method: {}, Error: {}",
			request.getURI(),
			request.getMethod(),
			exception.getMessage(),
			exception
		);

		return Mono.just(new ErrorResponse(CommonError.toInternalServerError(exception.getMessage())));
	}

}
