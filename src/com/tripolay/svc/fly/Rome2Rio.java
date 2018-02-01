package com.tripolay.svc.fly;


import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tripolay.homerest.app.Config;
import com.tripolay.homerest.resp.FlyResp;
import com.tripolay.homerest.util.Helper;

public class Rome2Rio 
{
		
	public static final String urlFly =  "http://free.rome2rio.com/api/1.2/json/Search";
	public static final String apiKey  =  "RcnXjmqF";
	
    private String src;
    private String dst;
    
    final static Logger LOGGER = Logger.getLogger(Rome2Rio.class.getName()); 
	
    public Rome2Rio(String src, String dst)
   	{
   		this.src = src;
   		this.dst = dst;
   	}
	public List<FlyResp> Search()
	{
		List<FlyResp> fltlist = new ArrayList<FlyResp>();
		
		URIBuilder builder = new URIBuilder();
		try
		{
			builder.setPath(urlFly)
			.setParameter("key", apiKey)
		    .setParameter("oName", src)
		    .setParameter("dName", dst).build();
		
			URI url = builder.build();
			
			LOGGER.log(Level.INFO,url.toString());
			
			String[] resp = null;
			resp = Helper.sendGetRequest(url.toString(),"");
		
			if(resp[1].length() > 1 )
			{
				List<JSONArray> listItin = getItin(resp[1]);
				fltlist = getFlights(listItin);
			}
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING, ex.getMessage());
		}
		return fltlist;
	}
	private List<FlyResp> getFlights(List<JSONArray> listItin)
	{
		List<FlyResp> fltlist = new ArrayList<FlyResp>();
		try
		{
			int record = 0;
			for(int i = 0;  i < listItin.size() ; i++)
			{
				JSONArray legsArray = listItin.get(i);
				
				for(int j = 0;  j < legsArray.length(); j++)
				{
					JSONObject temp = legsArray.getJSONObject(j);
					String price = temp.getJSONObject("indicativePrice").get("price").toString();
					String currency = temp.getJSONObject("indicativePrice").get("currency").toString();
					JSONArray hopArray = temp.getJSONArray("hops");
					for(int k=0; k < hopArray.length(); k++)
					{
						JSONObject hop = hopArray.getJSONObject(k);
						String deptAirpCode = hop.get("sCode").toString();
						String arvAirpCode  = hop.get("tCode").toString();
						//String deptcity = Helper.mapAPcodeUS.get(deptAirpCode);
						//String arivcity = Helper.mapAPcodeUS.get(arvAirpCode);
						String deptcity = Config.mapAPcodeUS.get(deptAirpCode);
						String arivcity = Config.mapAPcodeUS.get(arvAirpCode);
						if(deptcity == null || arivcity == null)
							continue;
						
							FlyResp FR = new FlyResp();
							FR.setRec(record++);
							FR.setTotalPrice(Double.parseDouble(price));
							FR.setCurrency(currency);
							FR.setdeptAirport(src);
							FR.setdeptAirportCode(deptAirpCode);
							FR.setarrvAirport(dst);
							FR.setarrvAirportCode(arvAirpCode);
							if(hop.has("sTerminal"))
								FR.setdeptGate(hop.get("sTerminal").toString());
							if(hop.has("tTerminal"))
								FR.setarrvGate(hop.get("tTerminal").toString());
							FR.setFlightNum(hop.get("flight").toString());
							FR.setdeptTime(hop.get("sTime").toString());
							FR.setarrvTime(hop.get("tTime").toString());
							String airlinecode = hop.get("airline").toString();
							//String airlinename = Helper.mapALcode.get(airlinecode);
							String airlinename = Config.mapALcode.get(airlinecode);
							FR.setAirlineCode(airlinecode);
							FR.setAirline(airlinename);
							FR.setFlightDur(hop.get("duration").toString());
							FR.setAircraft(hop.get("aircraft").toString());
							fltlist.add(FR);
					}
				}
			}
		}
		catch(Exception e)
		{
			LOGGER.log(Level.WARNING, e.getMessage());
		}
		return fltlist;
	}
	
	private List<JSONArray> getItin(String strResp)
	{
		List<JSONArray> list = new ArrayList<JSONArray>();
		try
		{
			JSONObject obj = new JSONObject(strResp);
			JSONArray routesArray = obj.getJSONArray("routes");
			
			for (int i = 0;  i < routesArray.length() ; i++)
			{
				JSONObject item = routesArray.getJSONObject(i);
				if( item.get("name").toString().contains("Fly"))
				{
					JSONArray segArray = item.getJSONArray("segments");
					for(int j = 0;  j < segArray.length(); j++)
					{
						JSONObject temp = segArray.getJSONObject(j);
						if(temp.getString("kind").toString().contains("flight"))
						{
							JSONArray itinArray = temp.getJSONArray("itineraries");
							for(int k=0; k < itinArray.length(); k++)
							{
								JSONObject temp2 = itinArray.getJSONObject(k);
								list.add(temp2.getJSONArray("legs"));
							}
						}
					}
				}
			}
		}
		catch (Exception ex) 
		{ 
			LOGGER.log(Level.WARNING, ex.getMessage());
		}
		return list;
	}
}
