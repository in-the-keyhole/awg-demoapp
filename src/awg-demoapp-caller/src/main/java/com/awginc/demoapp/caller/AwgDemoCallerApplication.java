package com.awginc.demoapp.caller;

import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@RestController
public class AwgDemoCallerApplication {

	private WebClient webClient = WebClient.create();

	public static void main(String[] args) {
		SpringApplication.run(AwgDemoCallerApplication.class, args);
	}

	@GetMapping
	public Mono<String> call() {
		return webClient.get().uri("http://awg-demoapp-server/call")
			.retrieve()
			.toEntity(String.class)
			.map(entity -> entity.getBody());
	}
	
}
