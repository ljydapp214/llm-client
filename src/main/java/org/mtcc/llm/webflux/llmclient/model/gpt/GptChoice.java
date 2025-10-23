package org.mtcc.llm.webflux.llmclient.model.gpt;

public record GptChoice(
	String finishReason,
	GptResponseMessage message
) {
}
