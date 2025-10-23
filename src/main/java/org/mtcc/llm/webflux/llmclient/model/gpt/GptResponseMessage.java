package org.mtcc.llm.webflux.llmclient.model.gpt;

import java.io.Serializable;

public record GptResponseMessage(
	String content
) implements Serializable {
}
