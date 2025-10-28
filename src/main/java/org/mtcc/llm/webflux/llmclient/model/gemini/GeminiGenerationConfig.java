package org.mtcc.llm.webflux.llmclient.model.gemini;

import java.io.Serializable;

public record GeminiGenerationConfig(
	String responseMimeType
) implements Serializable {
	public static GeminiGenerationConfig asDefault() {
		return new GeminiGenerationConfig("application/json");
	}
}
