package com.thoughtmechanix.licenses.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserContextFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(UserContextFilter.class);

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		logger.debug("doFilter# ---------------");

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

		UserContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID));
		UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
		UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
		UserContextHolder.getContext().setOrgId(httpServletRequest.getHeader(UserContext.ORG_ID));

		logger.debug("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

		// add by yhli begin: -----------------------
		UserContextHolder.getContext().setTestId(httpServletRequest.getHeader(UserContext.TEST_ID));
		try {
			UserContextHolder.getContext()
					.setTestSleep(Integer.parseInt(httpServletRequest.getHeader(UserContext.TEST_SLEEP)));
		} catch (Exception e) {
			UserContextHolder.getContext().setTestSleep(0);
		}
		logger.debug("doFilter# TEST_ID: {}, TEST_SLEEP: {}", UserContextHolder.getContext().getTestId(),
				UserContextHolder.getContext().getTestSleep());
		// add by yhli end: -------------------------

		filterChain.doFilter(httpServletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}