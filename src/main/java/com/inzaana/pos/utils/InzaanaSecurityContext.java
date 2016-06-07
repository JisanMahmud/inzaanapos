package com.inzaana.pos.utils;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

import com.inzaana.pos.models.User;

public class InzaanaSecurityContext implements SecurityContext {

	private User user;
	private String scheme;

	public InzaanaSecurityContext(User user, String scheme) {
		this.user = user;
		this.scheme = scheme;
	}

	@Override
	public Principal getUserPrincipal() {
		return this.user;
	}

	@Override
	public boolean isUserInRole(String s) {
		if (user.GetUserRole() != null) {
			return user.GetUserRole().equals(s);
		}
		return false;
	}

	@Override
	public boolean isSecure() {
		return "https".equals(this.scheme);
	}

	@Override
	public String getAuthenticationScheme() {
		return SecurityContext.BASIC_AUTH;
	}

}
