package com.thoughtmechanix.licenses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.thoughtmechanix.licenses.utils.UserContextInterceptor;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class Application {
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		// return new RestTemplate();

		// add by yhli begin: -----------------------
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new UserContextInterceptor());
		return restTemplate;
		// add by yhli end: -------------------------
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
