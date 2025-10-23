package org.mtcc.llm.webflux;

import org.junit.jupiter.api.Test;
import org.w3c.dom.CDATASection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class BasicFluxMonoTest {
	@Test
	void testBasicFluxMono() {
		Flux.just(1, 2, 3, 4, 5)
			.map(date -> date * 2)
			.filter(data -> data % 4 == 0)
			.subscribe(data -> System.out.println("Received: " + data));
	}

	@Test
	void testFluxCollectList() {
		Flux.<Integer>just(1,2,3,4,5)
			.collectList()
			.subscribe(list -> System.out.println("Collected List: " + list));
	}

	@Test
	void testScheduler() {
		Mono.<Integer>just(2)
			.map(data-> {
				System.out.println("map thread name: "+ Thread.currentThread().getName());
				return data * 2;
			})
			.publishOn(Schedulers.parallel())
			.filter(data-> {
				System.out.println("filter map thread name: "+ Thread.currentThread().getName());
				return data % 4 == 0;
			})
			.subscribeOn(Schedulers.boundedElastic())
			.subscribe(data-> {
				System.out.println("subscribe map thread name: "+ Thread.currentThread().getName());
				System.out.println("Final Received: " + data);
			});
	}
}
