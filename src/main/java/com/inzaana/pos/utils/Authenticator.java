package com.inzaana.pos.utils;
import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.internal.util.Base64;

public class Authenticator implements ClientRequestFilter {

	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";

	private final String user;
	private final String password;

	public Authenticator(String user, String passworld) {
		this.user = user;
		this.password = passworld;
	}

	public void filter(ClientRequestContext requestContext) throws IOException {
		MultivaluedMap<String, Object> headers = requestContext.getHeaders();
		final String basicAuthentication = GetBasicAuthentication();
		headers.add(AUTHORIZATION_PROPERTY, basicAuthentication);
	}

	private String GetBasicAuthentication() {
		String token = this.user + ":" + this.password;
		return AUTHENTICATION_SCHEME + " " + Base64.encodeAsString(token);
	}

}
