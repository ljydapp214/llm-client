package org.mtcc.llm.webflux.llmclient.model.jsonformat;

import java.util.List;

public record AnswerListResponseDto(
	List<String> answerList
) {
}
