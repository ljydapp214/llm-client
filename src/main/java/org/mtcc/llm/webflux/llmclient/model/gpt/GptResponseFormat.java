package org.mtcc.llm.webflux.llmclient.model.gpt;

import java.io.Serializable;

public record GptResponseFormat(
	String type
) implements Serializable {
}
