package org.mtcc.llm.webflux.llmclient.service;

import java.util.Arrays;

import org.mtcc.llm.webflux.llmclient.model.facade.FacadeAvailableModel;
import org.mtcc.llm.webflux.llmclient.model.facade.FacadeHomeResponse;
import org.mtcc.llm.webflux.user.controller.dto.LlmModel;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class FacadeServiceImpl implements FacadeService {
	@Override
	public Mono<FacadeHomeResponse> getFacadeHomeResponse() {
		return Mono.fromCallable(() -> new FacadeHomeResponse(
			Arrays.stream(LlmModel.values())
				.map(FacadeAvailableModel::from)
				.toList()
		));
	}
}
