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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuth2Client {
	
	private final Logger logger = LoggerFactory.getLogger(OAuth2Client.class);

	private ResteasyClient client;
	private ResteasyWebTarget authResource;
	private ResteasyWebTarget portalResource;

	private String username;
	private String password;
	private String businessID;
	private String lpcoType;
	
	public OAuth2Client() {
		client = new ResteasyClientBuilder().build();
	}
	
	public void setAuthResource(String url) {
		logger.info("Setting auth server url {}", url);
		authResource = client.target(url);
	}
	
	public void setPortalResource(String url) {
		logger.info("Setting portal server url {}", url);
		portalResource = client.target(url);
	}

	public Map<String, String> retrieveState() throws JsonParseException, JsonMappingException, IOException {
		String accessToken = getAccessToken(username, password);
		
		Response response = portalResource.request().header("Authorization", "Bearer " + accessToken).get();
		String jsonResp = response.readEntity(String.class);
		response.close();
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> objResp = new HashMap<String, String>();
		objResp = objectMapper.readValue(jsonResp, new TypeReference<Map<String, String>>(){});
		
		return objResp;
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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setBusinessID(String businessID) {
		this.businessID = businessID;
	}

	public void setLpcoType(String lpcoType) {
		this.lpcoType = lpcoType;
	}

}
