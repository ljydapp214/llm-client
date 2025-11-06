package org.mtcc.llm.webflux.llmclient.model.facade;

import java.util.List;

public record FacadeHomeResponse(
	List<FacadeAvailableModel> availableModelList
) {
}
