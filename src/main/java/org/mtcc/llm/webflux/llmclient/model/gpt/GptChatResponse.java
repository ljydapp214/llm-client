package org.mtcc.llm.webflux.llmclient.model.gpt;

import java.io.Serializable;
import java.util.List;

import org.mtcc.llm.webflux.exception.BaseException;
import org.mtcc.llm.webflux.exception.ErrorCode;

public record GptChatResponse(
	List<GptChoice> choices
) implements Serializable {
	public GptChoice getSingleChoice() {
		return choices.stream().findFirst().orElseThrow(() ->
			new BaseException("[GptResponse] There is no choices", ErrorCode.GPT_RESPONSE_ERROR));
	}

	public boolean isNotFinish() {
		return getSingleChoice().finish_reason() == null;
	}
}
