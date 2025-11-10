package org.mtcc.llm.webflux.user.service;

import org.mtcc.llm.webflux.user.controller.dto.UserChatResponse;
import org.mtcc.llm.webflux.user.controller.dto.UserCotChatResponse;
import org.mtcc.llm.webflux.user.service.dto.UserChatCommand;
import org.mtcc.llm.webflux.user.service.dto.UserCotChatCommand;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserChatService {
	Mono<UserChatResponse> getOneShotChat(UserChatCommand request);

	Flux<UserChatResponse> getOneShotChatStream(UserChatCommand command);

	Flux<UserCotChatResponse> getCotChat(UserCotChatCommand command);
}
