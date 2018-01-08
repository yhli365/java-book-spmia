package com.thoughtmechanix.licenses.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.services.LicenseService2;
import com.thoughtmechanix.licenses.utils.UserContextHolder;

@RestController
@RequestMapping(value = "v2/organizations/{organizationId}/licenses")
public class LicenseService2Controller {
	private static final Logger logger = LoggerFactory.getLogger(LicenseService2Controller.class);
	@Autowired
	private LicenseService2 licenseService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {
		logger.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

		String testId = UserContextHolder.getContext().getTestId();
		int sleep = UserContextHolder.getContext().getTestSleep();
		logger.debug("getLicensesByOrg Test id: {}, sleep={}", testId, sleep);

		switch (testId) {
		case "circuitbreaker":
			return licenseService.getLicensesByOrgTestCircuitBreaker(organizationId, sleep);
		case "timeout":
			return licenseService.getLicensesByOrgTestTimeout(organizationId, sleep);
		case "fallback":
			return licenseService.getLicensesByOrgTestFallback(organizationId, sleep);
		case "bulkhead":
			return licenseService.getLicensesByOrgTestBulkhead(organizationId, sleep);
		case "isolation":
			return licenseService.getLicensesByOrgTestIsolation(organizationId, sleep);
		case "config":
			return licenseService.getLicensesByOrgTestConfig(organizationId, sleep);
		default:
			return licenseService.getLicensesByOrgTestNormal(organizationId, sleep);
		}
	}

	@RequestMapping(value = "/{licenseId}", method = RequestMethod.GET)
	public License getLicenses(@PathVariable("organizationId") String organizationId,
			@PathVariable("licenseId") String licenseId) {
		logger.debug("getLicenses# Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
		
		return licenseService.getLicense(organizationId, licenseId);
	}

	@RequestMapping(value = "{licenseId}", method = RequestMethod.PUT)
	public void updateLicenses(@PathVariable("licenseId") String licenseId, @RequestBody License license) {
		licenseService.updateLicense(license);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public void saveLicenses(@RequestBody License license) {
		licenseService.saveLicense(license);
	}

	@RequestMapping(value = "{licenseId}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteLicenses(@PathVariable("licenseId") String licenseId, @RequestBody License license) {
		licenseService.deleteLicense(license);
	}
}
