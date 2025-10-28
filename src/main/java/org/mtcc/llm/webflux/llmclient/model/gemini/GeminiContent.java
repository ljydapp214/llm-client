package org.mtcc.llm.webflux.llmclient.model.gemini;

import java.io.Serializable;
import java.util.List;

public record GeminiContent(
	List<GeminiPart> parts,
	GeminiMessageRole role
) implements Serializable {
}
