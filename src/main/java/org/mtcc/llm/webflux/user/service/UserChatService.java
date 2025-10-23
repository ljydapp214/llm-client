package org.mtcc.llm.webflux.user.service;

import org.mtcc.llm.webflux.user.controller.dto.UserChatResponse;
import org.mtcc.llm.webflux.user.service.dto.UserChatCommand;

import reactor.core.publisher.Mono;

public interface UserChatService {
	Mono<UserChatResponse> getOneShotChat(UserChatCommand request);
}
