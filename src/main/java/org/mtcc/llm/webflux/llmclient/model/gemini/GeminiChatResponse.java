package org.mtcc.llm.webflux.llmclient.model.gemini;

import java.io.Serializable;
import java.util.List;

public record GeminiChatResponse(List<GeminiCandidate> candidates) implements Serializable {
	public String getSingleText() {
		return candidates.stream().findFirst()
			.flatMap(candidate -> candidate.content().parts().stream().findFirst()
				.map(GeminiPart::text))
			.orElseThrow();
	}
}
