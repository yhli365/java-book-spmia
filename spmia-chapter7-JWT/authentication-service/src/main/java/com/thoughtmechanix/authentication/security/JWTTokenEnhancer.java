package com.thoughtmechanix.authentication.security;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.thoughtmechanix.authentication.model.UserOrganization;
import com.thoughtmechanix.authentication.repository.OrgUserRepository;

public class JWTTokenEnhancer implements TokenEnhancer {
	private static final Logger logger = LoggerFactory.getLogger(JWTTokenEnhancer.class);

	@Autowired
	private OrgUserRepository orgUserRepo;

	private String getOrgId(String userName) {
		UserOrganization orgUser = orgUserRepo.findByUserName(userName);
		return orgUser.getOrganizationId();
	}

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> additionalInfo = new HashMap<>();
		String orgId = getOrgId(authentication.getName());

		additionalInfo.put("organizationId", orgId);
		logger.debug("enhance# user={}, orgId={}", authentication.getName(), orgId);

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		return accessToken;
	}
}
