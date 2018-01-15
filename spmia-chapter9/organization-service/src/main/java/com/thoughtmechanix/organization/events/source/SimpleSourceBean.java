package com.thoughtmechanix.organization.events.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.thoughtmechanix.organization.event.models.OrganizationChangeModel;

@Component
public class SimpleSourceBean {
	private Source source;

	private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);

	@Autowired
	public SimpleSourceBean(Source source) {
		this.source = source;
	}

	public void publishOrgChange(String action, String orgId) {
		logger.debug("Sending Kafka message {} for Organization Id: {}", action, orgId);
		OrganizationChangeModel change = new OrganizationChangeModel(OrganizationChangeModel.class.getTypeName(),
				action, orgId, "none");

		source.output().send(MessageBuilder.withPayload(change).build());
	}
}
