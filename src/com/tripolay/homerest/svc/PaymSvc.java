package com.tripolay.homerest.svc;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;


public class PaymSvc 
{
	final static Logger LOGGER = Logger.getLogger(PaymSvc.class.getName()); 
	
	public static String processPaym(String total, String nonceFromTheClient)
	{
		URI baseURI = UriBuilder.fromUri("http://svc-tripolay.rhcloud.com/svcrest/").build();
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(baseURI);
		
		JSONObject input = new JSONObject();
		input.put("total", total);
		input.put("nonce", nonceFromTheClient);
		
		String retVal = "";
		try
		{
			Response resp = target.path("svc").path("paym").request().post(Entity.entity(input.toString(),MediaType.APPLICATION_JSON),Response.class);
			retVal = resp.readEntity(String.class);
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING, ex.getMessage());
		}
		return retVal ;
	}

}
