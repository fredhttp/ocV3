package com.tripolay.homerest.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tripolay.homerest.app.Config;
import com.tripolay.homerest.parm.ParmCar;
import com.tripolay.homerest.parm.ParmCity;
import com.tripolay.homerest.parm.ParmFly;
import com.tripolay.homerest.parm.ParmHotel;
import com.tripolay.homerest.parm.ParmParser;
import com.tripolay.homerest.resp.CarResp;
import com.tripolay.homerest.resp.FlyResp;
import com.tripolay.homerest.resp.HotelResp;
import com.tripolay.homerest.svc.CarSvc;
import com.tripolay.homerest.svc.FlySvc;
import com.tripolay.homerest.svc.HotelSvc;
import com.tripolay.homerest.user.UserOp;
import com.tripolay.homerest.user.UserSession;
import com.tripolay.homerest.util.TextParser;



@Path("req")
public class RequestRes 
{
	
	@Context
	ServletContext context;
	
	final static Logger LOGGER = Logger.getLogger(RequestRes.class.getName()); 
	
	public RequestRes()
	{ }
	
	/*
	 * 		TO DO:
	 * 			check user session.
	 * 			check if user has login session
	 * 		
	 * 			in final code, conceal all errors send to client for API security
	 * 			Remove excessive logging.
	 * 
	 */

	
	
	@Path("fly")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public String getFlights(String input) 
    {	
		LOGGER.log(Level.INFO,"fly " + input);
		ParmFly parmFly = ParmParser.INSTANCE.ParseFly(input);
		if(parmFly.Error())
		{
			LOGGER.log(Level.INFO,"parmFly error: " + parmFly.getErrorMsg());
			return Error(parmFly.getErrorMsg());	
		}
	
		// Context only available when servlet makes the call. Not in the constructor 
		Config config = (Config) context.getAttribute("config");
		UserSession US = new UserSession(config.getSessionColFly(),config.getSessionColHtl(),config.getSessionColCar());
			
		UserOp UOP = new UserOp(config.getDBUsers());
		if(!UOP.hasLoginSession(parmFly.getSessionID(),true))
		 		return Error("no log in.");
		
			
		TextParser TP = new TextParser(config);
		ParmCity parmCity = TP.findMatch(parmFly.getDst());
		if(parmCity.Error())
		{
			return Error(parmCity.getErrorMsg());	
		}
		if(parmCity.getOneParm()) 
		{
			// user has specified dst only
			// parmFly.src is good 
			
			// get city from lat and lon instead of geoIP2.city.en java-script file.
			String lat = parmFly.getLat();
			String lon = parmFly.getLon();
			String city = "";
						
			if(!lat.isEmpty() && !lon.isEmpty())
			{
				try{
						double lat2 = Double.valueOf(lat).doubleValue();
				        double lon2 = Double.valueOf(lon).doubleValue();
				        city = TP.getCity(lat2,lon2);
				}
				catch (Exception ex){
								
				}
			}
			if(!city.isEmpty())
				parmFly.setSrc(city); 	// over-ride city generated name with lat/lon major airport city name.
			
			parmFly.setDst(parmCity.getArrvCity());
		} 
		else 
		{
			// user has specified src and dst.
			parmFly.setSrc(parmCity.getDeptCity()); 
			parmFly.setDst(parmCity.getArrvCity());
		}
		LOGGER.log(Level.INFO,"fly src: " + parmFly.getSrc() + " " + "dst: " + parmFly.getDst());
			
		List<FlyResp> fltlist = new FlySvc(parmFly.getSrc(),parmFly.getDst()).Search(); 
		if(fltlist == null || fltlist.size() < 1 )
			return Error("fltlist null or size.");
		
		LOGGER.log(Level.INFO, String.valueOf(fltlist.size()));
			
		US.insertFlyPrice(parmFly.getSessionID(), mapFlyPrice(fltlist));
			
		Gson gson = new Gson();	
		JsonObject myObj = new JsonObject();
		myObj.addProperty("success", true);
		myObj.add("fltinfo", gson.toJsonTree(fltlist));
		myObj.add("msg", gson.toJsonTree("flight list processed."));
	    return myObj.toString();
    }
	
	@Path("htl")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public String getHotel(String input) 
    {
		LOGGER.log(Level.INFO, "htl " + input);
		ParmHotel parmHotel = ParmParser.INSTANCE.ParseHotel(input);
		if(parmHotel.Error())
		{
			LOGGER.log(Level.WARNING, parmHotel.getErrorMsg());
			return Error(parmHotel.getErrorMsg());	
		}
	
		Config config = (Config) context.getAttribute("config");
		UserSession US = new UserSession(config.getSessionColFly(),config.getSessionColHtl(),config.getSessionColCar());	
		
		UserOp UOP = new UserOp(config.getDBUsers());
		if(!UOP.hasLoginSession(parmHotel.getSessionID(),true))
			return Error("no log in.");
		
		TextParser TP = new TextParser(config);
		ParmCity parmCity = TP.findMatch(parmHotel.getDst());
		if(parmCity.Error())
		{
			return Error(parmCity.getErrorMsg());	
		}	
		parmHotel.setDst(parmCity.getArrvCity());
		LOGGER.log(Level.INFO,"htl parms: " + parmHotel.getDst() + " " +  parmHotel.getStartDate() + " " + parmHotel.getEndDate());
			
		List<HotelResp> htllist = new HotelSvc(parmHotel.getDst(),parmHotel.getStartDate(),parmHotel.getEndDate()).Search();
		if(htllist == null || htllist.size() < 1)
			return Error("htllist null or size.");
			
		LOGGER.log(Level.INFO, String.valueOf(htllist.size()));
			
		US.insertHtlPrice(parmHotel.getSessionID(), mapHotelPrice(htllist));
			
		Gson gson = new Gson();
		JsonObject myObj = new JsonObject();
		myObj.addProperty("success", true);
		myObj.add("htlinfo",  gson.toJsonTree(htllist));
		myObj.add("msg", gson.toJsonTree("hotel list processed."));
		return myObj.toString();			
    }
	
	@Path("car")
	@POST
    @Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public String getCar(String input) 
    {
		LOGGER.log(Level.INFO,"car " + input);
		ParmCar parmCar = ParmParser.INSTANCE.ParseCar(input);
		if(parmCar.Error())
		{
			LOGGER.log(Level.INFO,"parm search error: " + parmCar.getErrorMsg());
			return Error(parmCar.getErrorMsg());	
		}
		
		Config config = (Config) context.getAttribute("config");
		UserSession US = new UserSession(config.getSessionColFly(),config.getSessionColHtl(),config.getSessionColCar());
		
		UserOp UOP = new UserOp(config.getDBUsers());
		if(!UOP.hasLoginSession(parmCar.getSessionID(),true))
			return Error("no log in.");
		
		TextParser TP = new TextParser(config);
		
		ParmCity parmCity = TP.findMatch(parmCar.getDst());
		if(parmCity.Error())
		{
			return Error(parmCity.getErrorMsg());	
		}
		parmCar.setDst(parmCity.getArrvCity());
		LOGGER.log(Level.INFO,"car parms: " + parmCar.getDst() + " " +  parmCar.getStartDate() + " " + parmCar.getEndDate() + " " + 
												parmCar.getPickTime() + "  " + parmCar.getDropTime());
		
		
		List<CarResp> carlist = new CarSvc(parmCar.getDst(),parmCar.getStartDate(),parmCar.getEndDate(),parmCar.getPickTime(),parmCar.getDropTime()).Search();		
		if(carlist == null || carlist.size() < 1)
			Error("carlist null or size");
        
		US.insertCarPrice(parmCar.getSessionID(), mapCarPrice(carlist));
		
		Gson gson = new Gson();
		JsonObject myObj = new JsonObject();
		myObj.addProperty("success", true);
		myObj.add("carinfo", gson.toJsonTree(carlist));
		myObj.add("msg", gson.toJsonTree("car list processed."));
		return myObj.toString();
    }
		
	
	
	
	
	
	private Map<String,Object> mapFlyPrice(List<FlyResp> FR)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		for(FlyResp item : FR)
			map.put(String.valueOf(item.getRec()), item.getTotalPrice());
		return map;
	}
	private Map<String,Object> mapHotelPrice(List<HotelResp> HT)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		for(HotelResp item : HT)
			map.put(String.valueOf(item.getRec()), item.getTotalPrice());
		return map;
	}
	private Map<String,Object> mapCarPrice(List<CarResp> CR)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		for(CarResp item : CR)
			map.put(String.valueOf(item.getRec()), item.getTotalPrice());
		return map;
	}
	private String Error(String msg)
	{
		JsonObject obj = new JsonObject();
		obj.addProperty("success", false);
		obj.add("errmsg" , new Gson().toJsonTree(msg));
		return obj.toString();
	}
	
	
	

}
