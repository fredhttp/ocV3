package com.tripolay.svc.htl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.tripolay.homerest.resp.HotelResp;
import com.tripolay.homerest.util.Helper;

public class HotwireHotel 
{
	
	public static final String urlDeal  = "http://api.hotwire.com/v1/deal/hotel?dest=";
	public static final String urlHotel	= "http://api.hotwire.com/v1/search/hotel";
	public static final String key =   "xh665y4kyb7vbsr6bwt3bywe";
	
	private String dst;
	private int limit;
	private int rooms;
	private int adults;
	private int children;
	private String startdate;
	private String enddate;
	
	final static Logger LOGGER = Logger.getLogger(HotwireHotel.class.getName()); 
	
	public HotwireHotel(String dst,int limit,int rooms,int adults,int children,String startdate,String enddate)
	{
		this.dst = dst;
		this.limit = limit;
		this.rooms = rooms;
		this.adults = adults;
		this.children = children;
		this.startdate = startdate;
		this.enddate = enddate;	
	}
	
	
	public List<HotelResp> Search()
	{
		List<HotelResp> list = new ArrayList<HotelResp>();
		
		//	Using Apache UriBuilder instead of javax-ws-rs-core jersey implementation	
		
		URIBuilder builder = new URIBuilder();	
		try
		{
			builder.setPath(urlHotel)
			.setParameter("dest", dst)
		    .setParameter("apikey", key)
		    .setParameter("limit", String.valueOf(limit))
		    .setParameter("rooms", String.valueOf(rooms))
		    .setParameter("adults", String.valueOf(adults))
		    .setParameter("children", String.valueOf(children))
		    .setParameter("startdate", startdate)
		    .setParameter("enddate", enddate)
		    .setParameter("format", "json").build();
			
			
			URI url = builder.build();
			
			LOGGER.log(Level.INFO,url.toString());
			
			String[] resp  = null;
			resp = Helper.sendGetRequest(url.toString(),"");
			if(resp[1].length() > 1 )
				list = processSearch(resp[1]);
		}
		catch(Exception ex )
		{  
			LOGGER.log(Level.WARNING, ex.getMessage());
		}	
		return list;
	}
	
	private List<HotelResp> processSearch(String strResp)
	{
		List<HotelResp> list = new ArrayList<HotelResp>();
		//LOGGER.log(Level.INFO,strResp);
		try 
		{
			JSONObject obj = new JSONObject(strResp);
			if(obj.has("Result"))
			{
				JSONArray resArr = obj.getJSONArray("Result");
				JSONArray ameArr = obj.getJSONObject("MetaData").getJSONObject("HotelMetaData").getJSONArray("Amenities");
				JSONArray nebArr = obj.getJSONObject("MetaData").getJSONObject("HotelMetaData").getJSONArray("Neighborhoods");
				int record = 0;	
				for (int i = 0; i < resArr.length(); i++)
				{
					JSONObject item = resArr.getJSONObject(i);
					HotelResp HT = new HotelResp();
					
					HT.setRec(record++);
					HT.setCurrencyCode(item.getString("CurrencyCode")); 	
					HT.setDeepLink(item.getString("DeepLink")); 		
					HT.setResultId(item.getString("ResultId"));		
					HT.setHWRefNumber(item.getString("HWRefNumber")); 	
					HT.setSubTotal(Double.parseDouble(item.getString("SubTotal")));		
					HT.setTaxesAndFees(Double.parseDouble(item.getString("TaxesAndFees"))); 	
					HT.setTotalPrice(Double.parseDouble(item.getString("TotalPrice")));	
					
					HT.setCheckInDate((item.getString("CheckInDate"))); 		
					HT.setCheckOutDate(item.getString("CheckOutDate"));		
					HT.setLodgingTypeCode(item.getString("LodgingTypeCode"));	
					HT.setNights(Integer.parseInt(item.getString("Nights")));		
					HT.setAveragePricePerNight(Double.parseDouble(item.getString("AveragePricePerNight"))); 
					if(item.has("RecomnendationPercentage"))
						HT.setRecommendationPercentage(Double.parseDouble(item.getString("RecomnendationPercentage")));
																			
					HT.setRooms(Integer.parseInt(item.getString("Rooms")));
					
					if(item.has("SavingsPercentage")) 
						HT.setSavingsPercentage(Double.parseDouble(item.getString("SavingsPercentage"))) ;
																		 
					HT.setStarRating(Double.parseDouble(item.getString("StarRating")));
					
					List<String> listamen = new ArrayList<String>();
					if( item.get("AmenityCodes") instanceof JSONArray)
					{
						JSONArray amenCode = item.getJSONArray("AmenityCodes");
						for (int j = 0; j < amenCode.length();  j++)
						{
							for (int k = 0; k < ameArr.length();  k++)
							{
								JSONObject temp = ameArr.getJSONObject(k);
								if( amenCode.get(j).equals(temp.get("Code") ))
								{
									listamen.add(temp.getString("Description"));
									break;
								}
							}
						}
					}
					if(listamen.size() > 0)
					{
						HT.setAmentiy(listamen);
					}
					for (int j = 0; j < nebArr.length();  j++)
					{
						JSONObject temp = nebArr.getJSONObject(j);
						if( item.getString("NeighborhoodId").equals(temp.get("Id") ))
						{
							HT.setNeighborhood(temp.getString("Name"));
							break;
						}
					}
					list.add(HT);
				}
			}
			else
				LOGGER.log(Level.INFO,"hotwire htl bad response..");
		}
		catch (Exception ex) 
		{ 
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return list;
	}
	
	

}
