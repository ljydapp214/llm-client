package org.mtcc.llm.webflux.user.controller.dto;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum LlmModel {
	GPT_4O("gpt-4o", LlmType.GPT),
	GEMINI_2_0_FLASH("gemini-2.0-flash", LlmType.GEMINI);

	private final String code;
	private final LlmType llmType;

	LlmModel(String code, LlmType llmType) {
		this.code = code;
		this.llmType = llmType;
	}

	@JsonValue
	@Override
	public String toString() {
		return code;
	}
}
