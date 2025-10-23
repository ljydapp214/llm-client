package org.mtcc.llm.webflux.llmclient.model.gpt;

import java.io.Serializable;
import java.util.List;

public record GptChatResponse(
	List<GptChoice> choices
) implements Serializable {
	public GptChoice getSingleChoice() {
		return choices.stream().findFirst().orElseThrow();
	}
}
