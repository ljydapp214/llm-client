package org.mtcc.llm.webflux.llmclient.model.gpt;

import com.fasterxml.jackson.annotation.JsonIgnore;

public enum GptMessageRole {
	SYSTEM,
	USER,
	ASSISTANT,
	;

	@JsonIgnore
	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
