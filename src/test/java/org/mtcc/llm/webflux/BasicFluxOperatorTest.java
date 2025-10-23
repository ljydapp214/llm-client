package org.mtcc.llm.webflux;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.util.context.Context;

public class BasicFluxOperatorTest {
	@Test
	void testFluxFromFunction() {
		Flux.defer(() -> Flux.just(1, 2, 3, 4, 5)
		).subscribe(data -> System.out.println("Received: " + data));

		Flux.create(sink -> {
			sink.next(1);
			sink.next(3);
			sink.next(5);
			sink.complete();
		}).subscribe(data -> System.out.println("Created: " + data));
	}

	@Test
	void testSinkDetail() {
		Flux.<String>create(sink -> {
			AtomicInteger count = new AtomicInteger(0);
			recursiveFunc(sink);
		})
			.contextWrite(Context.of("count", new AtomicInteger(0)))
			.subscribe(data -> System.out.println("Sink Data: " + data));
	}

	private void recursiveFunc(FluxSink<String> sink) {
		AtomicInteger count = sink.contextView().get("count");
		if (count.incrementAndGet() < 10) {
			sink.next("sink count: " + count);
			recursiveFunc(sink);
		} else {
			sink.complete();
		}
	}
}
