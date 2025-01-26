package com.sage.csa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.server.servlet.OAuth2AuthorizationServerJwtAutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication(exclude = {OAuth2AuthorizationServerJwtAutoConfiguration.class})
public class CsaApplication {

	public static void main(String[] args) {
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
		SpringApplication.run(CsaApplication.class, args);
	}

}
