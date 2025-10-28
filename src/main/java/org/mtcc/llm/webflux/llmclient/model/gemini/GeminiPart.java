package org.mtcc.llm.webflux.llmclient.model.gemini;

import java.io.Serializable;

public record GeminiPart(
	String text
) implements Serializable {
}
