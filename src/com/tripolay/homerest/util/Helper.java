package com.tripolay.homerest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class Helper 
{
    static String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    static String username = "jenny@tripolay.com";
    static String password = "jobs9999";
    
    final static Logger LOGGER = Logger.getLogger(Helper.class.getName()); 
    
    private static BraintreeGateway gateway = new BraintreeGateway(
			  Environment.SANDBOX,
			  "tfvht3gs2wnt5y95",
			  "pj899txj8qbm5rnc",
			  "7018d331e087012c63c88b4035d858f1"
			);
	
	public static boolean validateEmailFormat(String email)
	{
		return email.matches(EMAIL_REGEX);
	}
	public static boolean validateEmailAddress(String email)
	{
		// Using QuickEmailVerification service
		String apiKey = "24b7946e2b5fff3bad48bf5314f21f0ae495b92dac99ca7bfde8b90b8e1d";
		String endpoint = "http://api.quickemailverification.com/v1/verify";
		String strURL = endpoint + "?email=" + email + "&apikey=" + apiKey;
		
		LOGGER.log(Level.INFO,"Email verify url: " + strURL);
		
		String[] res = sendGetRequest(strURL,"");
		boolean retval = false;
		try
		{
			JsonObject root = (JsonObject) new JsonParser().parse(res[1]);
			String result = root.get("result").getAsString();
			boolean accept_all = root.get("accept_all").getAsBoolean();
			
			LOGGER.log(Level.INFO,"result is " + result + " accept_all " + accept_all);
			
			if(result.compareToIgnoreCase("valid") == 0  && !accept_all)
				retval = true;
		}
		catch(Exception ex) 
		{ 
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return retval;
	}
	public static boolean sendMail(String email, String regcode,boolean mobile)
	{
		boolean retval = false;
	      
		String from = "support@tripolay.com";
	    String host = "smtp.zoho.com";
	    
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", "587");

	    // Get the Session object.
	    Session session = Session.getInstance(props,
	      new javax.mail.Authenticator() {
	         protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	         }
	    });
	     
	    try 
	    {
	        
	         Message message = new MimeMessage(session);
	         message.setFrom(new InternetAddress(from));
	         message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
	         message.setSubject("Tripolay Registeration");
	        
	         String strHTML = "<h3>From Tripolay support:</h3> " +
	         					"<p>your registration code is <b>" + regcode + "</b></p>" + 
	         					"<p>registration code will expire in 6 hours</p>";
	         					
	         
	         
	         if(!mobile)
	         {
	        	 strHTML += "<p>please follow this link to complete your registeration <a href='http://reg.tripolay.com'><h3 style='color:blue;'>http://reg.tripolay.com</h3></a></p>";
	         }
	         
	         // Send the actual HTML message, as big as you like
	  	   	 message.setContent(strHTML,"text/html");
	  	   	 
	         Transport.send(message);

	         LOGGER.log(Level.INFO,"Sent message successfully....");
	         retval = true;

	    } 
	    catch(MessagingException ex) 
	    { 
	    	LOGGER.log(Level.WARNING,ex.getMessage());
	    }
		return retval;
	}
	public static String clientToken()
	{
		try
		{
			return gateway.clientToken().generate();
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return "";
	}
	public static String[] sendGetRequest(String strURL, String strAuth, String cookie) 
	{
		String USER_AGENT = "Mozilla/5.0";
		try
		{
			HttpGet getreq = new HttpGet(strURL);
			
			// add request header
			getreq.addHeader("User-Agent", USER_AGENT);
			getreq.addHeader("Set-Cookie", cookie);
			System.out.println("cookie set " + cookie);
			if(!strAuth.isEmpty())
				getreq.addHeader("Authorization", strAuth);
			
			return sendRequest(getreq);
		}
		catch (Exception ex) 
		{ 
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return new String[] { "500", ""};
	}
	public static String[] sendGetRequest(String strURL, String strAuth) 
	{
		String USER_AGENT = "Mozilla/5.0";
		try
		{
			HttpGet getreq = new HttpGet(strURL);
			
			// add request header
			getreq.addHeader("User-Agent", USER_AGENT);
			if(!strAuth.isEmpty())
				getreq.addHeader("Authorization", strAuth);
			
			return sendRequest(getreq);
		}
		catch(Exception ex) 
		{ 
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return new String[] { "500", ""};
	}
	public static String[] sendPostRequestJSON(String strURL, String strAuth, String strParmJSON) 
	{
		String USER_AGENT = "Mozilla/5.0";
		try
		{
			HttpPost post = new HttpPost(strURL);
			
			StringEntity input = new StringEntity(strParmJSON);
			input.setContentType("application/json");

			post.setEntity(input);
			post.addHeader("User-Agent", USER_AGENT);
			post.addHeader("Authorization", strAuth);
			post.addHeader("Content-Type", "application/json");
		
			return sendRequest(post);
		}
		catch(Exception ex) 
		{
			LOGGER.log(Level.WARNING,ex.getMessage());
		}	
		return new String[] { "500", ""};
	}
	public static String[] sendPostRequest(String strURL, String strAuth, String strParm) 
	{
		String USER_AGENT = "Mozilla/5.0";
		try
		{
			HttpPost post = new HttpPost(strURL);
			HttpEntity entity = new ByteArrayEntity(strParm.getBytes("UTF-8"));   // parameter is added to body.
			post.setEntity(entity);
			post.addHeader("User-Agent", USER_AGENT);
			if(!strAuth.isEmpty())
				post.addHeader("Authorization", strAuth);
			
			return sendRequest(post);
		}
		catch(Exception ex) 
		{ 
			LOGGER.log(Level.WARNING,ex.getMessage());
		}	
		return new String[] { "500", ""};
	}
	public static String[] sendPostRequest(String strURL, String strAuth, List<NameValuePair> urlParameters) 
	{
		String USER_AGENT = "Mozilla/5.0";
		try
		{
			HttpPost post = new HttpPost(strURL);
			// UrlEncodedFormEntity in a form like param1=value1&param2=value2  
			HttpEntity entity = new UrlEncodedFormEntity(urlParameters);
			post.setEntity(entity); 
			post.addHeader("User-Agent", USER_AGENT);
			if(!strAuth.isEmpty())
				post.addHeader("Authorization", strAuth);
		
			return sendRequest(post); 
		}
		catch(Exception ex) 
		{ 
			LOGGER.log(Level.WARNING,ex.getMessage());
		}	
		return new String[] { "500", ""};
	}
	private static String[] sendRequest(HttpRequestBase request)
	{
		try
		{
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(request);
			int responseCode = response.getStatusLine().getStatusCode();
		
			BufferedReader BR = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer SB = new StringBuffer();
			String line = "";
			while ((line = BR.readLine()) != null) 
			{
				SB.append(line);
			}
			String[] result = {String.valueOf(responseCode), SB.toString() };
			
			BR.close();
			
			return result;
		}
		catch(IOException ex) 
		{
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return new String[] { "500", ""};
	}
	public static void printJsonObjectPretty(JSONObject obj)
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(obj);
		System.out.println(json);	
	}
	public static void printJsonObject(JSONObject obj)
	{
		// org.json
		Iterator<?> keys = obj.keys();
        while( keys.hasNext() )
        {
            String key = (String)keys.next();   
            System.out.println(key + " : " + obj.get(key));
        }
	}
	
}

