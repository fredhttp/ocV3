package com.tripolay.homerest.parm;



public class ParmPay 
{
	String sessionID ="";
	String data = "";
	String total = "";
	String nonce = "";
	boolean error = false;
	String errmsg = "";
	
	public String getSessionID() 	{ return sessionID;}
	public String getData() 		{ return data;}
	public String getTotal() 		{ return total;}
	public String getNonce() 		{ return nonce;}
	public boolean Error() 			{ return error;}
	public String getErrorMsg() 	{ return errmsg;}

	
	public void setSessionID(String s) 	{ sessionID = s;}
	public void setData(String s) 		{ data = s;}
	public void setTotal(String s) 		{ total = s;}
	public void setNonce(String s) 		{ nonce = s;}
	public void setError(boolean b) 	{ error = b;}
	public void setErrorMsg(String s) 	{ errmsg = s;}

}
