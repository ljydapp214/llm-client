package org.mtcc.llm.webflux.llmclient.controller;

import org.mtcc.llm.webflux.llmclient.model.facade.FacadeHomeResponse;
import org.mtcc.llm.webflux.llmclient.service.FacadeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/facade")
public class FacadeController {
	private final FacadeService facadeService;

	@PostMapping("/home")
	public Mono<FacadeHomeResponse> homeFacade() {
		return facadeService.getFacadeHomeResponse();
	}
}
