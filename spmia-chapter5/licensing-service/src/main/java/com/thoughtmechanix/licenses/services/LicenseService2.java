package com.thoughtmechanix.licenses.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.thoughtmechanix.licenses.clients.OrganizationRestTemplateClient;
import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.model.Organization;
import com.thoughtmechanix.licenses.repository.LicenseRepository;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

// Hystrix让你在类级别设置默认参数，以便在一个特定的类共享相同配置的所有Hystrix命令。类级属性通过一个叨做@DefaultProperties的类级别注解迕行设置。
// @DefaultProperties(commandProperties = {
// @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
// value = "10000") })
@Service
public class LicenseService2 {
	private static final Logger logger = LoggerFactory.getLogger(LicenseService2.class);
	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	ServiceConfig config;

	@Autowired
	OrganizationRestTemplateClient organizationRestClient;

	@HystrixCommand
	public License getLicense(String organizationId, String licenseId) {
		logger.debug("getLicense# Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);

		Organization org = getOrganization(organizationId);

		return license.withOrganizationName(org.getName()).withContactName(org.getContactName())
				.withContactEmail(org.getContactEmail()).withContactPhone(org.getContactPhone())
				.withComment(config.getExampleProperty());
	}

	@HystrixCommand
	private Organization getOrganization(String organizationId) {
		logger.debug("getOrganization# Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
		return organizationRestClient.getOrganization(organizationId);
	}

	public void saveLicense(License license) {
		license.withId(UUID.randomUUID().toString());

		licenseRepository.save(license);
	}

	public void updateLicense(License license) {
		licenseRepository.save(license);
	}

	public void deleteLicense(License license) {
		licenseRepository.delete(license.getLicenseId());
	}

	/**
	 * 给你三分之一的机会数据库调用长期运行.
	 */
	private void randomlyRunLong(int sleep) {
		if (sleep < 0) {
			return;
		} else if (sleep > 0) {
			sleep();
			return;
		}

		Random rand = new Random();

		int randomNum = rand.nextInt((3 - 1) + 1) + 1;

		if (randomNum == 3)
			sleep();
	}

	private void sleep() {
		try {
			// 你休眠了11000毫秒（11秒）。Hystrix默认行为是1秒后调用。
			logger.debug("sleep: 11s");
			Thread.sleep(11000);
		} catch (InterruptedException e) {
			// e.printStackTrace();
			logger.warn("***** {}", e.toString());
		}
	}

	public List<License> getLicensesByOrgTestNormal(String organizationId, int sleep) {
		logger.debug("getLicensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

		return licenseRepository.findByOrganizationId(organizationId);
	}

	@HystrixCommand
	public List<License> getLicensesByOrgTestCircuitBreaker(String organizationId, int sleep) {
		logger.debug("getLicensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

		randomlyRunLong(sleep);

		return licenseRepository.findByOrganizationId(organizationId);
	}

	@HystrixCommand(//
			commandProperties = { //
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000") // 设置断路器的超时时间长度（以毫秒为单位）。
			} //
	)
	public List<License> getLicensesByOrgTestTimeout(String organizationId, int sleep) {
		logger.debug("getLicensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

		randomlyRunLong(sleep);

		return licenseRepository.findByOrganizationId(organizationId);
	}

	@HystrixCommand(fallbackMethod = "buildFallbackLicenseList")
	public List<License> getLicensesByOrgTestFallback(String organizationId, int sleep) {
		logger.debug("getLicensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

		randomlyRunLong(sleep);

		return licenseRepository.findByOrganizationId(organizationId);
	}

	public List<License> buildFallbackLicenseList(String organizationId, int sleep) {
		List<License> fallbackList = new ArrayList<>();
		License license = new License().withId("0000000-00-00000").withOrganizationId(organizationId)
				.withProductName("Sorry no licensing information currently available");

		fallbackList.add(license);
		return fallbackList;
	}

	@HystrixCommand(fallbackMethod = "buildFallbackLicenseList", //
			threadPoolKey = "licenseByOrgThreadPool", // 定义了线程池唯一的名称。
			threadPoolProperties = { //
					@HystrixProperty(name = "coreSize", value = "30"), // 定义线程池中线程的最大数量。
					@HystrixProperty(name = "maxQueueSize", value = "10") // 让你在线程池前面定义一个队列，它能使进入的请求排队。
			} //
	)
	public List<License> getLicensesByOrgTestBulkhead(String organizationId, int sleep) {
		logger.debug("getLicensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

		randomlyRunLong(sleep);

		return licenseRepository.findByOrganizationId(organizationId);
	}

	@HystrixCommand(//
			commandProperties = { // 注意：默认情冴下，Hystrix团队建议你为大部分命令使用默认的线程隑离策略。
					@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE") // 设置使用信号隑离设置隑离级别，
			} //
	)
	public List<License> getLicensesByOrgTestIsolation(String organizationId, int sleep) {
		logger.debug("getLicensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

		randomlyRunLong(sleep);

		return licenseRepository.findByOrganizationId(organizationId);
	}

	@HystrixCommand(fallbackMethod = "buildFallbackLicenseList", //
			threadPoolKey = "licenseByOrgThreadPool", //
			threadPoolProperties = { //
					@HystrixProperty(name = "coreSize", value = "30"), //
					@HystrixProperty(name = "maxQueueSize", value = "10") }, //
			commandProperties = { //
					@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //
					@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"), //
					@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"), //
					@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"), //
					@HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5") } //
	)
	public List<License> getLicensesByOrgTestConfig(String organizationId, int sleep) {
		logger.debug("getLicensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

		randomlyRunLong(sleep);

		return licenseRepository.findByOrganizationId(organizationId);
	}

}
