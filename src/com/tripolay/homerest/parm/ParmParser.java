package com.tripolay.homerest.parm;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;




public enum ParmParser 
{
	INSTANCE;
	
	Logger LOGGER;

    ParmParser()
    {
    	LOGGER = Logger.getLogger(ParmParser.class.getName()); 
    }
    
    static final int len_userid = 30;
    static final int len_passwd = 20;
    static final int len_sessionID = 40; 	// c50c9ff8-b9d1-43d5-a8e4-2677051043dd
    static final int len_regcode = 15;		// 0c867aa4
    static final int len_fname = 20;
    static final int len_lname = 40;
    static final int len_phone = 15;		// 408-000-0000
    static final int len_hnumber = 10;
    static final int len_street = 20;
    static final int len_city = 20;
    static final int len_state = 15;
    static final int len_zip = 10;
    static final int len_country = 20;
    static final int len_email = 40;
    static final int len_src = 50;
    static final int len_datetime = 20; 	// yyyy/mm/dd 12:20
    static final int len_srchinput = 50; 	// San Francisco to San Francisco
    static final int len_lat = 25;			// arbitrary precision but GeoIP service has only 4
    static final int len_lon = 25;
    
    
    
    // Static getter
    public static ParmParser getInstance()
    {
        return INSTANCE;
    }
    
    // TO DO: Check obj.length() on all methods.
    //        Check parms length too.
    
    public String getSessionID(String input)
	{
    	String sessionID = "";
		try
		{
			JSONObject obj = new JSONObject(input);
			if(obj.length() == 1 && obj.has("id"))
			{
				sessionID = obj.getString("id");
				if(sessionID.trim().length() < len_sessionID)
					return sessionID;
			}
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return sessionID;
	}
   
    public ParmSignUp ParseSignUp(String parms)
    {
        ParmSignUp PS = new ParmSignUp();
    	try
		{
			JSONObject obj = new JSONObject(parms);
			
			if(obj.has("mobile"))
				PS.setMobile(true);
			
			if(obj.has("regcode"))
			{
				if(obj.getString("regcode").trim().length() < len_regcode)
					PS.setRegCode(obj.getString("regcode"));
				else
					return ErrorSignUp("error. length");
			}
			else
				return ErrorSignUp("error. no reg code");
			
			if(obj.has("fname"))
			{
				if(obj.getString("fname").trim().length() < len_fname)
					PS.setFirstName(obj.getString("fname"));
				else
					return ErrorSignUp("error. length");
			}
			else
				return ErrorSignUp("error. no fname");
			
			if(obj.has("lname"))
			{
				if(obj.getString("lname").trim().length() < len_lname)
					PS.setLastName(obj.getString("lname"));
				else
					return ErrorSignUp("error. length");
			}
			else
				return ErrorSignUp("error. no lname");
			
			if(obj.has("phone"))
			{
				if(obj.getString("phone").trim().length() < len_phone)
					PS.setPhone(obj.getString("phone"));
				else
					return ErrorSignUp("error. length");
			}
			else
				return ErrorSignUp("error. no phone");
			
			if(obj.has("hnumber"))
			{
				if(obj.getString("hnumber").trim().length() < len_hnumber)
					PS.setHouseNumber(obj.getString("hnumber"));
				else
					return ErrorSignUp("error. length");
			}
			if(obj.has("street"))
			{
				if(obj.getString("street").trim().length() < len_street)
					PS.setStreet(obj.getString("street"));
				else
					return ErrorSignUp("error. length");
			}
			if(obj.has("city"))
			{
				if(obj.getString("city").trim().length() < len_city)
					PS.setCity(obj.getString("city"));
				else
					return ErrorSignUp("error. length");
			}
			if(obj.has("state"))
			{
				if(obj.getString("state").trim().length() < len_state)
					PS.setState(obj.getString("state"));
				else
					return ErrorSignUp("error. length");
			}
			if(obj.has("zip"))
			{
				if(obj.getString("zip").trim().length() < len_zip)
					PS.setZip(obj.getString("zip"));
				else
					return ErrorSignUp("error. length");
			}
			if(obj.has("country"))
			{
				if(obj.getString("country").trim().length() < len_country)
					PS.setCountry(obj.getString("country"));
				else
					return ErrorSignUp("error. length");
			}
			if(obj.has("passwd"))
			{
				if(obj.getString("passwd").trim().length() < len_passwd)
					PS.setPasswd(obj.getString("passwd"));
				else
					return ErrorSignUp("error. length");
			}
			else
				return ErrorSignUp("error. no passwd");
			
			
			if(obj.has("gresp"))
				PS.setGresp(obj.getString("gresp"));
		}
		catch(JSONException ex)
		{ 
			return ErrorSignUp("error. sign up exception");
		}
    	return PS;
    }
    public ParmReg ParseReg(String parms)
    {
    	ParmReg PR = new ParmReg();
    	try
		{
			JSONObject obj = new JSONObject(parms);
			if(obj.has("email")  && obj.getString("email").trim().length() < len_email)
			{
				PR.setEmail(obj.getString("email"));
				if(obj.has("mobile"))
					PR.setMobile(true);	
			}
			else 
				return ErrorReg("error. num of parameters");
		}
		catch(JSONException ex)
		{ 
			return ErrorReg("error. parsing reg exception");
		}
    	return PR;
    }
    public ParmLogin ParseLogin(String parms)
    {
    	ParmLogin PL = new ParmLogin();
    	try
		{
			JSONObject obj = new JSONObject(parms);
			if(obj.length() == 2 && obj.has("userid") && obj.has("passwd") )
			{
				if( obj.getString("userid").trim().length() > len_userid || obj.getString("passwd").trim().length() > len_passwd)
					return ErrorLogin("error. num of parameters");
				PL.setUserID(obj.getString("userid"));
				PL.setPasswd(obj.getString("passwd"));
			}
			else 
				return ErrorLogin("error. num of parameters");
		}
		catch(JSONException ex)
		{ 
			ex.printStackTrace();
			return ErrorLogin("error. parsing login exception");
		}
    	return PL;
    }
    public ParmPay ParsePay(String parms)
    {
    	String data = "";
    	String total ="";
		String nonce ="";
		String sessionID = "";
    	try
		{
			JSONObject obj = new JSONObject(parms);
			if(obj.length() == 4 && obj.has("data") && obj.has("total") && obj.has("nonce") && obj.has("id"))
			{
				data = obj.getString("data");
				total = obj.getString("total");
				nonce = obj.getString("nonce");
				sessionID = obj.getString("id");
				double Total = 0;
				Total = Double.parseDouble(total);
				if( Total < 50 || Total > 20000  )
					return ErrorPay("error. total < or > or session");
			}
			else 
				return ErrorPay("error. num of parameters");
		}
		catch(JSONException ex)
		{ 
			return ErrorPay("error. parsing pay json exception");
		}
    	catch(NumberFormatException ex)
		{ 
			return ErrorPay("error. parsing total exception");
		}
    	if(sessionID.trim().length() > len_sessionID)
    	{
    		return ErrorPay("sessionID  >  " + len_sessionID);
    	}
		
    	ParmPay PP = new ParmPay();
    	PP.setSessionID(sessionID);
    	PP.setData(data);
    	PP.setTotal(total);
    	PP.setNonce(nonce);
    	return PP;
    }
    
    
   
    public ParmFly ParseFly(String parms)
    {
		String src = "";
		String input ="";
		String sdatetime = "";
		String edatetime = "";
		String sessionID = "";
		String lat = "";
		String lon = "";
		if(parms.trim().length() > 200)
		{
			LOGGER.log(Level.INFO,parms);
			return ErrorFly("error. parms len > 200");
		}
		try
		{
			JSONObject obj = new JSONObject(parms);
			
			if(obj.length() == 7 && obj.has("src") && obj.has("lat") && obj.has("lon") && obj.has("input") 
					&& obj.has("sdatetime") && obj.has("edatetime") && obj.has("id"))
			{
				src 	  = obj.getString("src").trim();
				input 	  = obj.getString("input").trim();
				sdatetime = obj.getString("sdatetime").trim();
				edatetime = obj.getString("edatetime").trim();
				sessionID = obj.getString("id").trim();
				lat = obj.getString("lat").trim();
				lon = obj.getString("lon").trim();
				
				LOGGER.log(Level.INFO,"in parser:  src: " + src + "   dst: " + input);
			}
			else 
				return ErrorFly("error. num of parameters");
			}
		catch(JSONException ex)
		{ 
			return ErrorFly("error. parsing exception");
		}
		if(lat.length() > len_lat || lon.length() > len_lon)
    	{
    		return ErrorFly("erorr. lat or lon > 20 ");
    	}
		if( src.length() > len_src)
		{
			return ErrorFly("erorr. src len > 20 ");
		}
		if(input.length() < 2 || input.length() > len_srchinput )
		{
			return ErrorFly("erorr. input len < 2 or >  "  + len_srchinput);
		}
		if( sdatetime.length() > len_datetime || edatetime.length() > len_datetime )
		{
			return ErrorFly("erorr. sdatetime or edatetime >   " + len_datetime);
		}
		if(sessionID.length() > len_sessionID)
		{
			return ErrorFly("sessionID  >  "  + len_sessionID);
		}
	
		// exception is caught and turned into todays date..
		ParmDateTime dtp = new ParmDateTime(sdatetime,edatetime);
	
		ParmFly PF = new ParmFly();
		PF.setSessionID(sessionID);
		PF.setSrc(src);
		PF.setDst(input);
		PF.setStartDate(dtp.getStartDate());
		PF.setEndDate(dtp.getEndDate());
		PF.setStartTime(dtp.getStartTime());
		PF.setEndTime(dtp.getEndTime());
		PF.setLat(lat);  
		PF.setLon(lon);
		return PF;
    }
    public ParmHotel ParseHotel(String parms)
    {
		String input ="";
		String sdatetime = "";
		String edatetime = "";
		String sessionID = "";
		if(parms.trim().length() > 200)
		{
			LOGGER.log(Level.INFO,parms);
			return ErrorHotel("error. parms len > 200");
		}
		try
		{
			JSONObject obj = new JSONObject(parms);
		
			if(obj.length() == 4 &&  obj.has("input") && obj.has("sdatetime") && obj.has("edatetime") && obj.has("id"))
			{
				input 	  = obj.getString("input").trim();
				sdatetime = obj.getString("sdatetime").trim();
				edatetime = obj.getString("edatetime").trim();
				sessionID = obj.getString("id").trim();
				LOGGER.log(Level.INFO,"in parser:  dst: " + input);
			}
			else 
				return ErrorHotel("error. num of parameters");
		}
		catch(JSONException ex)
		{ 
			return ErrorHotel("error. parsing exception");
		}
		if(input.length() < 2 || input.length() > len_srchinput )
		{
			return ErrorHotel("erorr. input len < 2 or >  " + len_srchinput);
		}
		if( sdatetime.length() > len_datetime || edatetime.length() > len_datetime )
		{
			return ErrorHotel("erorr. sdatetime or edatetime >   " + len_datetime);
		}
		if(sessionID.length() > len_sessionID)
		{
			return ErrorHotel("sessionID  >  " + len_sessionID);
		}
	
		// exception is caught and turned into todays date..
		ParmDateTime dtp = new ParmDateTime(sdatetime,edatetime);
		
		ParmHotel PH = new ParmHotel();
		PH.setSessionID(sessionID);
		PH.setDst(input);
		PH.setStartDate(dtp.getStartDate());
		PH.setEndDate(dtp.getEndDate());
		return PH;
    }
    public ParmCar ParseCar(String parms)
    {
		String input ="";
		String sdatetime = "";
		String edatetime = "";
		String sessionID = "";
		if(parms.trim().length() > 200)
		{
		LOGGER.log(Level.INFO,parms);
		return ErrorCar("error. parms len > 200");
		}
		try
		{
		JSONObject obj = new JSONObject(parms);
		
		if(obj.length() == 4 &&  obj.has("input") && obj.has("sdatetime") && obj.has("edatetime") && obj.has("id"))
		{
			input 	  = obj.getString("input").trim();
			sdatetime = obj.getString("sdatetime").trim();
			edatetime = obj.getString("edatetime").trim();
			sessionID = obj.getString("id").trim();
			LOGGER.log(Level.INFO,"in parser:  dst: " + input);
		}
		else 
			return ErrorCar("error. num of parameters");
		}
		catch(JSONException ex)
		{ 
			return ErrorCar("error. parsing exception");
		}
		if(input.length() < 2 || input.length() > len_srchinput )
		{
			return ErrorCar("erorr. input len < 2 or >   "  + len_srchinput);
		}
		if( sdatetime.length() > len_datetime || edatetime.length() > len_datetime )
		{
			return ErrorCar("erorr. sdatetime or edatetime >   " + len_datetime);
		}
		if(sessionID.length() > len_sessionID)
		{
			return ErrorCar("sessionID  >  "  + len_sessionID);
		}
	
		// exception is caught and turned into todays date..
		ParmDateTime dtp = new ParmDateTime(sdatetime,edatetime);
		
		ParmCar PC = new ParmCar();
		PC.setSessionID(sessionID);
		PC.setDst(input);
		PC.setStartDate(dtp.getStartDate());
		PC.setEndDate(dtp.getEndDate());
		PC.setPickTime(dtp.getStartTime());
		PC.setDropTime(dtp.getEndTime());
		return PC;
    }



    
    private ParmPay ErrorPay(String msg)
    {
    	LOGGER.log(Level.WARNING,msg);
    	ParmPay PP = new ParmPay();
		PP.setError(true);
		PP.setErrorMsg(msg);
		return PP;
    }
    private ParmSignUp ErrorSignUp(String msg)
    {
    	ParmSignUp PS = new ParmSignUp();
		PS.setError(true);
		PS.setErrorMsg(msg);
		return PS;
    }
    
    private ParmLogin ErrorLogin(String msg)
    {
    	ParmLogin PL = new ParmLogin();
		PL.setError(true);
		PL.setErrorMsg(msg);
		return PL;
    }
    private ParmReg ErrorReg(String msg)
    {
    	ParmReg PR = new ParmReg();
		PR.setError(true);
		PR.setErrorMsg(msg);
		return PR;
    }
    private ParmFly ErrorFly(String msg)
    {
    	ParmFly PF = new ParmFly();
		PF.setError(true);
		PF.setErrorMsg(msg);
		return PF;
    }
    private ParmHotel ErrorHotel(String msg)
    {
    	ParmHotel PH = new ParmHotel();
		PH.setError(true);
		PH.setErrorMsg(msg);
		return PH;
    }
    private ParmCar ErrorCar(String msg)
    {
    	ParmCar PC = new ParmCar();
		PC.setError(true);
		PC.setErrorMsg(msg);
		return PC;
    }
    
    
}
