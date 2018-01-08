package com.thoughtmechanix.licenses.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
	public static final String CORRELATION_ID = "tmx-correlation-id";
	public static final String AUTH_TOKEN = "tmx-auth-token";
	public static final String USER_ID = "tmx-user-id";
	public static final String ORG_ID = "tmx-org-id";

	private String correlationId = new String();
	private String authToken = new String();
	private String userId = new String();
	private String orgId = new String();

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	// add by yhli begin: -----------------------
	/**
	 * 随机超时调用许可服务数据库
	 */
	public static final String TEST_ID = "my-test-id";
	public static final String TEST_SLEEP = "my-test-sleep";
	private String testId = new String();
	private Integer testSleep = new Integer(0);

	public String getTestId() {
		return testId;
	}

	public void setTestId(String testId) {
		this.testId = testId;
	}

	public Integer getTestSleep() {
		return testSleep;
	}

	public void setTestSleep(Integer testSleep) {
		this.testSleep = testSleep;
	}
	// add by yhli end: -------------------------

}