package org.mtcc.llm.webflux.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mtcc.llm.webflux.llmclient.service.LlmWebClientService;
import org.mtcc.llm.webflux.user.controller.dto.LlmType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {
	@Bean
	public Map<LlmType, LlmWebClientService> serviceMap(List<LlmWebClientService> services) {
		return services.stream()
			.collect(Collectors.toMap(LlmWebClientService::getLlmType, Function.identity()));
	}
}
