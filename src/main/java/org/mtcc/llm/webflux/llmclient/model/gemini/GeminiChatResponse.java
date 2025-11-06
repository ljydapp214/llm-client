package org.mtcc.llm.webflux.llmclient.model.gemini;

import java.io.Serializable;
import java.util.List;

import org.mtcc.llm.webflux.exception.BaseException;
import org.mtcc.llm.webflux.exception.ErrorCode;

public record GeminiChatResponse(List<GeminiCandidate> candidates) implements Serializable {
	public String getSingleText() {
		return candidates.stream().findFirst()
			.flatMap(candidate -> candidate.content().parts().stream().findFirst()
				.map(GeminiPart::text))
			.orElseThrow(
				() -> new BaseException("[GptResponse] There is no candidate", ErrorCode.GEMINI_RESPONSE_ERROR));
	}
}
