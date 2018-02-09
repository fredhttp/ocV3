package com.tripolay.homerest.svc;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tripolay.svc.pay.braintree.TransactionX;


public class PaymSvc 
{
	final static Logger LOGGER = Logger.getLogger(PaymSvc.class.getName()); 
	
	public static String processPaym(String total, String nonceFromTheClient)
	{	
		String retVal = "";
		Gson gson = new Gson(); 
		JsonObject rootObj = new JsonObject();
		try
		{
			Result<Transaction> result = TransactionX.Apply(total, nonceFromTheClient);
			LOGGER.log(Level.INFO,"transaction result: " + result.isSuccess());
			if(result.isSuccess())
			{
				rootObj.addProperty("success", true);
				rootObj.add("result", gson.toJsonTree("transaction complete"));
			}
			else
			{
				rootObj.addProperty("success", false);
				rootObj.add("result", gson.toJsonTree("transaction failed"));
			}
			retVal = rootObj.toString();
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING, ex.getMessage());
		}
		return retVal ;
	}

}
