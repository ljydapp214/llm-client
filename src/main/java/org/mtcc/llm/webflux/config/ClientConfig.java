package org.mtcc.llm.webflux.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration
public class ClientConfig {
	@Bean
	public WebClient webClient() {
		HttpClient httpClient = HttpClient.create()
			.responseTimeout(Duration.ofMillis(10_000));

		return WebClient.builder()
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.build();
	}
}
