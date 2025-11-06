package org.mtcc.llm.webflux.llmclient.model.gemini;

import java.io.Serializable;

public record GeminiCandidate(
	GeminiContent content,
	String finishReason
) implements Serializable {
}
