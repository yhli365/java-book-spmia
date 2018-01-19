package com.thoughtmechanix.licenses.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

@Component
public class OrganizationRestTemplateClient {
	@Autowired
	RestTemplate restTemplate;

	private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

	public Organization getOrganization(String organizationId) {
		logger.debug("In Licensing Service.getOrganization: {}", UserContextHolder.getContext().getCorrelationId());
		logger.debug("getOrganization# token: {}", UserContextHolder.getContext().getAuthToken());

		ResponseEntity<Organization> restExchange = //
				restTemplate.exchange( //
						// "http://zuulserver:5555/api/organization/v1/organizations/{organizationId}",
						"http://zuulservice/api/organization/v1/organizations/{organizationId}", //
						HttpMethod.GET, null, //
						Organization.class, organizationId);

		return restExchange.getBody();
	}
}
