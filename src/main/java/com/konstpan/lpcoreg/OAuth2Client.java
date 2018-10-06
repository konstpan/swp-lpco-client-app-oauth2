package com.konstpan.lpcoreg;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konstpan.lpcoreg.dto.WebServiceServiceResult;

public class OAuth2Client {

	private final Logger logger = LoggerFactory.getLogger(OAuth2Client.class);

	private ResteasyClient client;
	private ResteasyWebTarget authResource;
	private ResteasyWebTarget portalResource;

	private String username;
	private String password;
	private String clientSecret = "331cee99-bee0-45f0-83a2-1904797eb9b5";
	private String businessID;
	private String lpcoType;

	public OAuth2Client() throws GeneralSecurityException {
		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(createAllTrustingClient());
		client = new ResteasyClientBuilder().httpEngine(engine).build();
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
		String accessToken = getAccessToken(username, password, clientSecret);

		Response response = portalResource.path("/lpco/state/{lpcoType}/{businessID}")
				.resolveTemplate("lpcoType", lpcoType).resolveTemplate("businessID", businessID)
				.request(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken).get();
		String jsonResp = response.readEntity(String.class);
		logger.info("Response from service for {} {} {}", lpcoType, businessID, jsonResp);
		response.close();

		ObjectMapper objectMapper = new ObjectMapper();
		WebServiceServiceResult<Map<String, String>> objResp;
		objResp = objectMapper.readValue(jsonResp, new TypeReference<WebServiceServiceResult<Map<String, String>>>() {
		});

		return objResp.getItem();
	}

	private String getAccessToken(String username, String password, String clientSecret)
			throws JsonParseException, JsonMappingException, IOException {
		Form form = new Form().param("grant_type", "password").param("username", username).param("password", password)
				.param("client_id", "swp.trader.core").param("client_secret", clientSecret);
		Response response = authResource.request().post(Entity.form(form));
		String jsonResp = response.readEntity(String.class);
		response.close();

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> objResp = new HashMap<String, String>();
		objResp = objectMapper.readValue(jsonResp, new TypeReference<Map<String, String>>() {
		});

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
		switch (lpcoType) {
		case "Veterinary":
			lpcoType = "LVT15";
			break;
		case "Phytosanitary":
			lpcoType = "LPS15";
			break;
		case "Temporary Storage":
			lpcoType = "LTS15";
			break;
		case "Simplified Procedure":
			lpcoType = "LSP15";
			break;
		default:
			lpcoType = "";
		}

		this.lpcoType = lpcoType;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	/**
	 * Workaround to solve self signed certificates exception.
	 * 
	 * @return
	 * @throws GeneralSecurityException
	 */
	private DefaultHttpClient createAllTrustingClient() throws GeneralSecurityException {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

		TrustStrategy trustStrategy = new TrustStrategy() {
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}

			@Override
			public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
					throws java.security.cert.CertificateException {
				return true;
			}
		};

		SSLSocketFactory factory = new SSLSocketFactory(trustStrategy, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		registry.register(new Scheme("https", 443, factory));

		ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
		mgr.setMaxTotal(1000);
		mgr.setDefaultMaxPerRoute(1000);

		DefaultHttpClient client = new DefaultHttpClient(mgr, new DefaultHttpClient().getParams());
		return client;
	}

}
