package org.mtcc.llm.webflux.user.service.dto;

import java.io.Serializable;
import java.util.List;

import org.mtcc.llm.webflux.llmclient.model.gpt.GptChatRequest;
import org.mtcc.llm.webflux.llmclient.model.gpt.GptCompletionRequest;
import org.mtcc.llm.webflux.llmclient.model.gpt.GptMessageRole;
import org.mtcc.llm.webflux.llmclient.model.gpt.GptResponseFormat;
import org.mtcc.llm.webflux.user.controller.dto.LlmModel;

public record LlmChatRequest(
	String userRequest,
	String systemPrompt,
	boolean useJson,
	LlmModel llmModel
) implements Serializable {
	public static LlmChatRequest of(UserChatCommand userChatRequestDto, String systemPrompt) {
		return new LlmChatRequest(userChatRequestDto.request(),
			systemPrompt,
			false,
			userChatRequestDto.llmModel());
	}

	public GptChatRequest toGptChatRequest() {
		List<GptCompletionRequest> requests = List.of(
			new GptCompletionRequest(GptMessageRole.SYSTEM, this.systemPrompt),
			new GptCompletionRequest(GptMessageRole.USER, this.userRequest)
		);

		GptResponseFormat format = null;
		if (this.useJson) {
			format = new GptResponseFormat("json_object");
		}
		return new GptChatRequest(requests, this.llmModel, false, format);
	}
}
