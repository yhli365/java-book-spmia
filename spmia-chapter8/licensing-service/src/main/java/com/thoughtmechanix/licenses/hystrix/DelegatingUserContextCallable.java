package com.thoughtmechanix.licenses.hystrix;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.thoughtmechanix.licenses.utils.UserContext;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

public final class DelegatingUserContextCallable<V> implements Callable<V> {
	private static final Logger logger = LoggerFactory.getLogger(DelegatingUserContextCallable.class);
	private final Callable<V> delegate;

	// private final UserContext delegateUserContext;
	private UserContext originalUserContext;

	public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
		Assert.notNull(delegate, "delegate cannot be null");
		Assert.notNull(userContext, "userContext cannot be null");
		this.delegate = delegate;
		this.originalUserContext = userContext;
	}

	public DelegatingUserContextCallable(Callable<V> delegate) {
		this(delegate, UserContextHolder.getContext());
	}

	public V call() throws Exception {
		UserContextHolder.setContext(originalUserContext);
		logger.info("call# set CORRELATION_ID: {}", UserContextHolder.getContext().getCorrelationId());

		try {
			return delegate.call();
		} finally {

			this.originalUserContext = null;
		}
	}

	public String toString() {
		return delegate.toString();
	}

	public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext) {
		return new DelegatingUserContextCallable<V>(delegate, userContext);
	}
}