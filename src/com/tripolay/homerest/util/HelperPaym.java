package com.tripolay.homerest.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tripolay.homerest.parm.ParmPay;
import com.tripolay.homerest.resp.PaymentCheck;
import com.tripolay.homerest.user.UserSession;

public class HelperPaym 
{
	final static Logger LOGGER = Logger.getLogger(HelperPaym.class.getName()); 
	
	public static boolean verifyPayment(UserSession US,ParmPay PP)
	{
		try
		{
			Map<Integer,Double> mapfly = US.getFlyMap(PP.getSessionID());
			Map<Integer,Double> maphtl = US.getHtlMap(PP.getSessionID());
			Map<Integer,Double> mapcar = US.getCarMap(PP.getSessionID());
			
			LOGGER.log(Level.INFO,"payment flt map size " + mapfly.size());
			LOGGER.log(Level.INFO,"payment htl map size " + maphtl.size());
			LOGGER.log(Level.INFO,"payment car map size " + mapcar.size());
			
			List<PaymentCheck> list = getPayment(PP.getData());
			
			if(list.size() == 0)
			{
				LOGGER.log(Level.INFO, "bad pay load");
				return false;
			}
			
			// verify user submitted total with our own records
			BigDecimal Total = new BigDecimal(PP.getTotal());
			BigDecimal sum = getPaymentTotal(list,mapfly,maphtl,mapcar);
					
			int res = sum.compareTo(Total);
			LOGGER.log(Level.INFO,"sum: " + sum.toString() + "  total: " + Total.toString() + " res: " + res + " session: " + PP.getSessionID());
					
			float diff = sum.subtract(Total).abs().floatValue();
			LOGGER.log(Level.INFO,"diff: " + diff);
					
			if( diff > 1 )
			{
				LOGGER.log(Level.INFO,"can not proceed with payment diff > 1");
				return false;
			}
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING, ex.getMessage());
			return false;
		}
		return true;
	}
	
	private static BigDecimal getPaymentTotal(List<PaymentCheck> list,Map<Integer,Double> mapfly,Map<Integer,Double> maphtl,Map<Integer,Double> mapcar)
	{
		BigDecimal sum = new BigDecimal(0.0);
		for(PaymentCheck item: list)
		{
			int itinClass = item.getItinClass();
			int rec = item.getRec();
			double temp = 0;
			switch(itinClass)
			{
				case 0:
					if(mapfly.size() > 0)
						temp = getPrice(mapfly,rec);
					break;
				case 1:
					if(maphtl.size() > 0)
						temp = getPrice(maphtl,rec);
					break;
				case 2:
					if(mapcar.size() > 0)
						temp = getPrice(mapcar,rec);
					break;
			}
			if(temp != 0)
			{
				sum = sum.add(new BigDecimal(String.valueOf(temp)));
				LOGGER.log(Level.INFO,"sum is " + sum.toString());
			}
		}
		sum = sum.setScale(2, RoundingMode.CEILING);
		return sum;
	}
	private static double getPrice(Map<Integer,Double> map, int rec)
	{
		for(Map.Entry<Integer,Double> kv : map.entrySet())
		{
			if(kv.getKey() == rec) 
				return kv.getValue(); 
		}
		return 0;
	}
	private static List<PaymentCheck> getPayment(String jsonData)
	{
		List<PaymentCheck> list = new ArrayList<PaymentCheck>();
		try
		{
			JsonArray arr = (JsonArray) new JsonParser().parse(jsonData);
			for(int i = 0 ; i < arr.size(); i++) 
			{
				JsonObject obj = arr.get(i).getAsJsonObject();
				if(obj.has("rec"))  
				{
					PaymentCheck p = new PaymentCheck(obj.get("rec").getAsInt(),obj.get("itn").getAsInt(),obj.get("price").getAsDouble());
					list.add(p);
				}
			}
		}
	    catch (Exception ex)
		{ 
	    	LOGGER.log(Level.INFO,ex.getMessage()); 
	    }
		return list;
	}

}
