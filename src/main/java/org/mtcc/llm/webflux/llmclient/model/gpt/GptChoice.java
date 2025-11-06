package org.mtcc.llm.webflux.llmclient.model.gpt;

public record GptChoice(
	String finish_reason,
	GptResponseMessage message,
	GptResponseMessage delta
) {
}
