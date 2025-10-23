package org.mtcc.llm.webflux.llmclient.model.gpt;

import java.io.Serial;
import java.io.Serializable;

public record GptCompletionRequest(
	GptMessageRole role,
	String content
) implements Serializable {
	@Serial
	private static final long serialVersionUID = 6979078039218146747L;

}
