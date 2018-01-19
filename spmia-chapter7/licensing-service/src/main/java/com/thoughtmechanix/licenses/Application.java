package com.thoughtmechanix.licenses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.thoughtmechanix.licenses.utils.UserContextInterceptor;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableResourceServer
public class Application {

	// @Autowired
	// private ServiceConfig serviceConfig;

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@LoadBalanced // 不加入此注解无法通过Eureka调用
	@Bean
	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
			OAuth2ProtectedResourceDetails details) {
		logger.debug("oauth2RestTemplate# --------------");
		logger.debug("oauth2RestTemplate# ClientId={}", details.getClientId());

		OAuth2RestTemplate template = new OAuth2RestTemplate(details, oauth2ClientContext);
		template.getInterceptors().add(new UserContextInterceptor());
		return template;
	}

	// @Primary
	// @Bean
	// public RestTemplate getCustomRestTemplate() {
	// RestTemplate template = new RestTemplate();
	// List interceptors = template.getInterceptors();
	// if (interceptors == null) {
	// template.setInterceptors(Collections.singletonList(new
	// UserContextInterceptor()));
	// } else {
	// interceptors.add(new UserContextInterceptor());
	// template.setInterceptors(interceptors);
	// }
	//
	// return template;
	// }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
