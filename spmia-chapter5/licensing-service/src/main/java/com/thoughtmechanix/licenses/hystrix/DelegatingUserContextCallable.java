package com.thoughtmechanix.licenses.hystrix;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtmechanix.licenses.utils.UserContext;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

public final class DelegatingUserContextCallable<V> implements Callable<V> {
	private static final Logger logger = LoggerFactory.getLogger(DelegatingUserContextCallable.class);

	private final Callable<V> delegate;
	private UserContext originalUserContext;

	public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
		this.delegate = delegate;
		this.originalUserContext = userContext;
	}

	public V call() throws Exception {
		logger.info("call# before CORRELATION_ID: {}", UserContextHolder.getContext().getCorrelationId());
		UserContextHolder.setContext(originalUserContext);
		logger.info("call# set CORRELATION_ID: {}", UserContextHolder.getContext().getCorrelationId());

		try {
			return delegate.call();
		} finally {
			this.originalUserContext = null;
			logger.info("call# after CORRELATION_ID: {}", UserContextHolder.getContext().getCorrelationId());
		}
	}

	public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext) {
		return new DelegatingUserContextCallable<V>(delegate, userContext);
	}
}