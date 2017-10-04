package com.tripolay.homerest.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tripolay.homerest.app.Config;
import com.tripolay.homerest.parm.ParmLogin;
import com.tripolay.homerest.parm.ParmParser;
import com.tripolay.homerest.parm.ParmPay;
import com.tripolay.homerest.parm.ParmReg;
import com.tripolay.homerest.parm.ParmSignUp;
import com.tripolay.homerest.svc.PaymSvc;
import com.tripolay.homerest.user.Address;
import com.tripolay.homerest.user.User;
import com.tripolay.homerest.user.UserContact;
import com.tripolay.homerest.user.UserOp;
import com.tripolay.homerest.user.UserSession;
import com.tripolay.homerest.util.Helper;
import com.tripolay.homerest.util.HelperPaym;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;


@Path("usr")
public class UserRes 
{
	@Context
	ServletContext context;
	
	final static Logger LOGGER = Logger.getLogger(UserRes.class.getName());
	
	
	/////////////////   for testing only    //////////////////
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String getSession() 
	{
	String sess = UUID.randomUUID().toString();
	System.out.println(sess);
	Gson gson = new Gson(); 
	JsonObject myObj = new JsonObject();
	JsonElement obj = gson.toJsonTree(sess);
	myObj.addProperty("success", true);
	myObj.add("sess", obj);
	LOGGER.log(Level.INFO, myObj.toString());
	return myObj.toString();	
	}
	/////////////////////////////////////////////////////////// 
	
	@Path("token")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public String getToken(String input)
    {
		String sessionID = ParmParser.INSTANCE.getSessionID(input);
		if(sessionID.isEmpty())
			return Error("bad req");
		
		Config config = (Config) context.getAttribute("config");
		UserOp UOP = new UserOp(config.getDBUsers());
		if(!UOP.hasLoginSession(sessionID,true))
			return Error("no log in.");
		
		String token = Helper.clientToken();
		if(token == null || token.isEmpty())
			return Error("no token");
		
		LOGGER.log(Level.INFO, "client token len:" + token.length());
		
		Gson gson = new Gson(); 
		JsonObject myObj = new JsonObject();
		
		myObj.addProperty("success", true);
		myObj.add("tokeninfo", gson.toJsonTree(token));
		myObj.add("msg", gson.toJsonTree("client token sent."));
		
		return myObj.toString();
    }
	@Path("pay")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public String Pay(String input)
    {
		ParmPay PP = ParmParser.INSTANCE.ParsePay(input);
		if(PP.Error())
			return Error(PP.getErrorMsg());
		
		Config config = (Config) context.getAttribute("config");
		UserOp UOP = new UserOp(config.getDBUsers());
		if(!UOP.hasLoginSession(PP.getSessionID(),true))
			return Error("no log in.");
		
		UserSession US = new UserSession(config.getSessionColFly(),config.getSessionColHtl(),config.getSessionColCar());
		
		if(HelperPaym.verifyPayment(US,PP))
		{
			String retVal =  PaymSvc.processPaym(PP.getTotal(), PP.getNonce());
			LOGGER.log(Level.INFO, "retVal from processPaym: " + retVal);
			if(retVal.isEmpty())
				return Error("retval empty");
			
			JSONObject obj = new JSONObject(retVal);
			if(obj.has("success") && obj.has("result"))
			{
				if(!obj.getBoolean("success"))
				{
					return Error(obj.getString("result"));
				}
			}
			else
				return Error("cound not parse return value");
		}
		else
		{
			LOGGER.log(Level.INFO, "could not verify payment");
			return Error("can not proceed with payment");
		}
		Gson gson = new Gson(); 
		JsonObject myObj = new JsonObject();
		myObj.addProperty("success", true);
		myObj.add("msg", gson.toJsonTree("payment processed."));
		LOGGER.log(Level.INFO, myObj.toString());
		return myObj.toString();
    }
	
	
	@Path("login")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public String login(String input)
    {	
		ParmLogin PL = ParmParser.INSTANCE.ParseLogin(input);
		if(PL.Error())
			return Error(PL.getErrorMsg());
		
		LOGGER.log(Level.INFO,"userid: " +  PL.getUserID());
		LOGGER.log(Level.INFO,"passwd: " +  PL.getPasswd());
		
		// one login per all browser tab. 
		// when tab is closed client should call logout. To do.
		
		Config config = (Config) context.getAttribute("config");
		UserOp UOP = new UserOp(config.getDBUsers());
		
		// for repeated logins. must logout first.
		// not using at this time. user can have up to 10 login sessions
		//if(UOP.hasLoginSession(PL.getUserID(),false))
		//	return Error("logged in.");
		
		User user = new User(PL.getUserID(),PL.getPasswd());
		if( !UOP.hasLogin(user))
			return Error("Could not verify login.")	;
		
		String sess = UUID.randomUUID().toString();
		
		if(!UOP.signIn(user,sess))
		{
			return Error("Could not sign-in user.")	;
		}
		
		Gson gson = new Gson(); 
		JsonObject myObj = new JsonObject();
		myObj.addProperty("success", true);
		myObj.add("sess", gson.toJsonTree(sess));
		myObj.add("msg", gson.toJsonTree("login validated."));
		LOGGER.log(Level.INFO, myObj.toString());
		
		return myObj.toString();		
    }
	
	
	@Path("reg")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public String register(String input)
    {
		ParmReg PR = ParmParser.INSTANCE.ParseReg(input);
		if(PR.Error())
			return Error(PR.getErrorMsg());
		
		String email = PR.getEmail();
		boolean mobile = PR.getMobile();
		
		Config config = (Config) context.getAttribute("config");
		UserOp UOP = new UserOp(config.getDBUsers());
		
		if(checkEmail(email))
			if(!UOP.userExists(email))
			{
				String regcode = UUID.randomUUID().toString().substring(0, 8);
				
				if(!UOP.tempUserNew(email, regcode))
					return Error("could not register temp user");
				
				if(!Helper.sendMail(email,regcode,mobile))
					return Error("could not send email");
			}
			else
				return Error("user already registered");
		else
			return Error("invalid email format");
		
		JsonObject myObj = new JsonObject();
		myObj.addProperty("success", true);
		myObj.add("msg", new Gson().toJsonTree("email sent."));
		LOGGER.log(Level.INFO, myObj.toString());
		
		return myObj.toString();
		
    }
	@Path("signup")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public String signup(String input)
    {
		
		Config config = (Config) context.getAttribute("config");
		UserOp UOP = new UserOp(config.getDBUsers());
		
		ParmSignUp PS = ParmParser.INSTANCE.ParseSignUp(input);
		if(PS.Error())
			return Error(PS.getErrorMsg());
		
		String email = UOP.tempUserEmail(PS.getRegCode());
		System.out.println(email);
		
		if(!email.isEmpty())  							// to do also check if not expired.
			if(!UOP.userExists(email))
				if(validateCaptcha(PS.getGresp(),PS.getMobile()))
				{
					if( !addNewUser(UOP,PS.getFirstName(),PS.getLastName(),email,"",PS.getPhone(),"",PS.getHouseNum(),
							PS.getStreet(),PS.getCity(),PS.getState(),PS.getZip(),PS.getCountry(), PS.getPasswd()) )
					
						return Error("add user failed");
				} 
				else 
					return Error("captcha was not set or empty");
			else 
				return Error("user already exists");
		else
			return Error("bad reg code");
	
		JsonObject myObj = new JsonObject();
		myObj.addProperty("success", true);
		myObj.add("msg", new Gson().toJsonTree("user registered."));
		LOGGER.log(Level.INFO, myObj.toString());
		
		return myObj.toString();	
			
    }
	private boolean addNewUser(UserOp UOP,String fname,String lname,String email,String altemail,String phone,String altphone,
			String hnumber,String street,String city,String state,String zip,String country,String passwd)
	{
		User user = new User(email,passwd);
		user.setFirstName(fname);
		user.setLastName(lname);
		user.setPhone(phone);
		user.setAddress(new Address(hnumber,street,city,state,zip,country));
		UserContact contact = new UserContact();
		List<String> list = new ArrayList<String>();
		if(!altemail.isEmpty()) {
			list.add(altemail);
			contact.setEmail(list);
		}
		list = new ArrayList<String>();
		if(!altphone.isEmpty()) {
			list.add(altphone);
			contact.setPhone(list);
		}
		user.setContact(contact);
		return UOP.addUser(user);
	}
	private boolean validateCaptcha(String gresp,boolean mobile)
	{
		if(mobile)
			return true;
		
		if(gresp.isEmpty())
			return false;
		// reCAPTCHA post these parms
		String URL = "https://www.google.com/recaptcha/api/siteverify";
		//String remoteip	="127.0.0.1";
		// To send POST parameters. Use List not strParms. 
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("secret", "6LcP0hoTAAAAABZVba4TcldZD1IqwoPfgEED-Vo8"));
		urlParameters.add(new BasicNameValuePair("response", gresp));
		//urlParameters.add(new BasicNameValuePair("remoteip", remoteip));
		
		String[] result = Helper.sendPostRequest(URL, "", urlParameters);
		LOGGER.log(Level.INFO,result[0]);
		LOGGER.log(Level.INFO,result[1]);
			
		JsonObject obj = (JsonObject) new JsonParser().parse(result[1]);
		if(obj.has("success") && obj.get("success").getAsBoolean())
			return true;
		else
			return false;
	}
	private boolean checkEmail(String email)
	{
		boolean a = Helper.validateEmailFormat(email);
		if(a)
			LOGGER.log(Level.INFO,"email format good " + email);
		else
			LOGGER.log(Level.INFO,"email format bad " + email);
		
		boolean b = Helper.validateEmailAddress(email);
		if(b)
			LOGGER.log(Level.INFO,"email validated: " + email);
		else
			LOGGER.log(Level.INFO,"email not validated: " + email);
		
		return a && b;
	}
	
	private String Error(String msg)
	{
		JsonObject obj = new JsonObject();
		obj.addProperty("success", false);
		obj.add("errmsg" , new Gson().toJsonTree(msg));
		return obj.toString();
	}
	
	

}
