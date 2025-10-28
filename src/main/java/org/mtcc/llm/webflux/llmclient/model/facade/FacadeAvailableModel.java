package org.mtcc.llm.webflux.llmclient.model.facade;

import org.mtcc.llm.webflux.user.controller.dto.LlmModel;

public record FacadeAvailableModel(
	String displayName,
	String codeName
) {
	public static FacadeAvailableModel from(LlmModel llmModel) {
		return new FacadeAvailableModel(llmModel.name(), llmModel.getCode());
	}
}
