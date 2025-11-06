package org.mtcc.llm.webflux.llmclient.model.gpt;

import java.io.Serializable;
import java.util.List;

import org.mtcc.llm.webflux.user.controller.dto.LlmModel;

public record GptChatRequest(
	List<GptCompletionRequest> messages,
	LlmModel model,
	Boolean stream,
	GptResponseFormat response_format
) implements Serializable {

	public GptChatRequest convertToStream() {
		return new GptChatRequest(this.messages, this.model, true, this.response_format);
	}
}
