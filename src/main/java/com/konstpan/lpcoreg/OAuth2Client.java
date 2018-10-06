package com.konstpan.lpcoreg;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuth2Client {

	private ResteasyClient client;
	private ResteasyWebTarget authResource;
	private ResteasyWebTarget portalResource;

	public OAuth2Client() {
		client = new ResteasyClientBuilder().build();
		
		authResource = client.target("http://localhost:8080/auth/realms/master/protocol/openid-connect/token");
		portalResource = client.target("http://demo1295438.mockable.io/");
	}

	private String getAccessToken(String username, String password) throws JsonParseException, JsonMappingException, IOException {
		Form form = new Form().param("grant_type", "password").param("username", username).param("password", password).param("client_id", "admin-cli");
		Response response = authResource.request().post(Entity.form(form));
		String jsonResp = response.readEntity(String.class);
		response.close();
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> objResp = new HashMap<String, String>();
		objResp = objectMapper.readValue(jsonResp, new TypeReference<Map<String, String>>(){});
		
		return objResp.get("access_token");
	}
	
	public Map<String, String> retrieveState(String username, String password) throws JsonParseException, JsonMappingException, IOException {
		String accessToken = getAccessToken(username, password);
		
		Response response = portalResource.request().header("Authorization", "Bearer " + accessToken).get();
		String jsonResp = response.readEntity(String.class);
		response.close();
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> objResp = new HashMap<String, String>();
		objResp = objectMapper.readValue(jsonResp, new TypeReference<Map<String, String>>(){});
		
		return objResp;
	}

}
