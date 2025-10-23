package org.mtcc.llm.webflux.user.controller;

import org.mtcc.llm.webflux.user.controller.dto.UserChatRequest;
import org.mtcc.llm.webflux.user.controller.dto.UserChatResponse;
import org.mtcc.llm.webflux.user.service.UserChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class UserChatController {
	private final UserChatService userChatService;

	@PostMapping("/one-shot")
	public Mono<UserChatResponse> oneShotChat(
		@RequestBody UserChatRequest request
	) {
		return userChatService.getOneShotChat(request.toCommand());
	}
}
