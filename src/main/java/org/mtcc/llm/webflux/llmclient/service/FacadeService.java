package org.mtcc.llm.webflux.llmclient.service;

import org.mtcc.llm.webflux.llmclient.model.facade.FacadeHomeResponse;

import reactor.core.publisher.Mono;

public interface FacadeService {
	Mono<FacadeHomeResponse> getFacadeHomeResponse();
}
