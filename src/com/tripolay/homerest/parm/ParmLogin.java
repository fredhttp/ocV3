package com.tripolay.homerest.parm;



public class ParmLogin 
{
	String userid = "";
	String passwd = "";
	boolean error = false;
	String errmsg = "";
	
	public String getUserID() 		{ return userid;}
	public String getPasswd() 		{ return passwd;}
	public boolean Error() 			{ return error;}
	public String getErrorMsg() 	{ return errmsg;}

	
	public void setUserID(String s) 	{ userid = s;}
	public void setPasswd(String s) 	{ passwd = s;}
	public void setError(boolean b) 	{ error = b;}
	public void setErrorMsg(String s) 	{ errmsg = s;}

}
