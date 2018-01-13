package com.thoughtmechanix.zuulsvr.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;

@Component
public class TrackingFilter extends ZuulFilter {
	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;

	private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);

	@Autowired
	private FilterUtils filterUtils;

	@Override
	public String filterType() {
		return FilterUtils.PRE_FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		return FILTER_ORDER;
	}

	public boolean shouldFilter() {
		return SHOULD_FILTER;
	}

	private boolean isCorrelationIdPresent() {
		if (filterUtils.getCorrelationId() != null) {
			return true;
		}

		return false;
	}

	private String generateCorrelationId() {
		return java.util.UUID.randomUUID().toString();
	}

	public Object run() {

		// RequestContext ctx = RequestContext.getCurrentContext();

		if (isCorrelationIdPresent()) {
			logger.debug("tmx-correlation-id found in tracking filter: {}. ", filterUtils.getCorrelationId());
		} else {
			filterUtils.setCorrelationId(generateCorrelationId());
			logger.debug("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
		}

		return null;
	}
}
