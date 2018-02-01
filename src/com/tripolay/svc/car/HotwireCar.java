package com.tripolay.svc.car;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tripolay.homerest.resp.CarResp;
import com.tripolay.homerest.util.Helper;


/*
 * 		Notes:
 * 			http://developer.hotwire.com/docs/read/Hotel_Deals_API
 * 			The &limit= parameter is not supported by the Rental Car Shopping API.
 * 			local pick up time must be more than 2 hours from local time
 * 
 */
public class HotwireCar 
{	
	public static final String urlCar = "http://api.hotwire.com/v1/search/car";
	public static final String key =   "xh665y4kyb7vbsr6bwt3bywe";
	
	private String dst;
	private String startdate;
	private String enddate;
	private String picktime;
	private String droptime;
	
	final static Logger LOGGER = Logger.getLogger(HotwireCar.class.getName()); 

	public HotwireCar(String dst,String startdate,String enddate,String picktime,String droptime)
	{
		this.dst = dst;
		this.startdate = startdate;
		this.enddate = enddate;
		this.picktime = picktime;
		this.droptime = droptime;
	}
	public List<CarResp> Search()
	{
		List<CarResp> carlist = new ArrayList<CarResp>();
		
		URIBuilder builder = new URIBuilder();
		try 
		{
			builder.setPath(urlCar)
				.setParameter("dest", dst)
			    .setParameter("apikey", key)
			    .setParameter("pickuptime", picktime)
			    .setParameter("dropofftime", droptime)
			    .setParameter("startdate", startdate)
			    .setParameter("enddate", enddate)
			    .setParameter("format", "json").build();
			
			URI url = builder.build();
			
			LOGGER.log(Level.INFO,url.toString());
			
			String[] resp = null;
			resp = Helper.sendGetRequest(url.toString(),"");
			if(resp[1].length() > 1 )
				carlist = processSearch(resp[1]);
		}
		catch(Exception ex )
		{ 
			LOGGER.log(Level.WARNING, ex.getMessage());
		} 
		return carlist;
	}
	private List<CarResp> processSearch(String strResp)
	{
		List<CarResp> list = new ArrayList<CarResp>();
		//LOGGER.log(Level.INFO,strResp);
		try
		{
			//LOGGER.log(Level.INFO,strResp);
			JSONObject obj = new JSONObject(strResp);
			if(obj.has("Result"))
			{
				JSONArray resArr = obj.getJSONArray("Result");
				JSONArray typeArr = obj.getJSONObject("MetaData").getJSONObject("CarMetaData").getJSONArray("CarTypes");
				int record = 0;		
				for (int i = 0; i < resArr.length(); i++)
				{
					JSONObject item = resArr.getJSONObject(i);
					CarResp car = new CarResp();
					car.setRec(record++); 
					if(item.has("CurrencyCode"))
						car.setCurrencyCode(item.getString("CurrencyCode"));
					else
						car.setCurrencyCode("N/A");
					
					if(item.has("DeepLink"))
						car.setDeepLink(item.getString("DeepLink")); 	
					else
						car.setDeepLink("N/A");
					car.setResultId(item.getString("ResultId"));		
					car.setHWRefNumber(item.getString("HWRefNumber")); 	
					car.setSubTotal(Double.parseDouble(item.getString("SubTotal")));		
					car.setTaxesAndFees(Double.parseDouble(item.getString("TaxesAndFees"))); 	
					car.setTotalPrice(Double.parseDouble(item.getString("TotalPrice")));		
					car.setDailyRate(Double.parseDouble(item.getString("DailyRate"))); 		
					car.setDropoffDay(item.getString("DropoffDay"));		
					car.setDropoffTime(item.getString("DropoffTime"));	
					car.setPickupDay(item.getString("PickupDay"));		
					car.setPickupTime(item.getString("PickupTime")); 	
					car.setLocationDescription(item.getString("LocationDescription"));
					car.setMileageDescription(item.getString("MileageDescription"));
					try
					{
						if(item.has("PickupAirport") )
							car.setPickupAirport(item.getString("PickupAirport"));
						else
							car.setPickupAirport("N/A");
					}
					catch(JSONException ex)
					{
						ex.printStackTrace();
						LOGGER.log(Level.WARNING, ex.getMessage());
						car.setPickupAirport("N/A");
					}
					
					//car.setPickupAirport("exception"); 
					car.setRentalDays(item.getString("RentalDays")); 
					
					for (int j = 0; j < typeArr.length();  j++)
					{
						JSONObject item2 = typeArr.getJSONObject(j);
						if( item.getString("CarTypeCode").equals( item2.getString("CarTypeCode")) )
						{
							car.setTypicalSeating(item2.getString("TypicalSeating")); 
							car.setCarTypeName(item2.getString("CarTypeName")); 	
							car.setPossibleFeatures(item2.getString("PossibleFeatures"));	
							car.setPossibleModels(item2.getString("PossibleModels"));
						    break;
						}
					}
					list.add(car);
				}
			}
			else
				LOGGER.log(Level.INFO,"hotwire car bad response..");
		}
		catch(Exception ex)
		{ 
			ex.printStackTrace();
			LOGGER.log(Level.WARNING, ex.getMessage());
		}
		return list;
	}
	
}
