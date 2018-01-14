package com.thoughtmechanix.licenses;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.utils.UserContextInterceptor;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableBinding(Sink.class)
public class Application {

	@Autowired
	private ServiceConfig serviceConfig;

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		logger.debug("getRestTemplate#-----");
		RestTemplate template = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
		if (interceptors == null) {
			template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
		} else {
			interceptors.add(new UserContextInterceptor());
			template.setInterceptors(interceptors);
		}

		return template;
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		logger.debug("jedisConnectionFactory#-----");
		JedisConnectionFactory jedisConnFactory = new JedisConnectionFactory();
		jedisConnFactory.setHostName(serviceConfig.getRedisServer());
		jedisConnFactory.setPort(serviceConfig.getRedisPort());
		return jedisConnFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		logger.debug("redisTemplate#-----");
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}

	// @StreamListener(Sink.INPUT)
	// public void loggerSink(OrganizationChangeModel orgChange) {
	// logger.debug("Received an event for organization id {}",
	// orgChange.getOrganizationId());
	// }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
