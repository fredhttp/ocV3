package com.tripolay.homerest.parm;


public class ParmReg 
{
	String email = "";
	boolean error = false;
	String errmsg = "";
	boolean mobile = false;
	
	public ParmReg()
	{}
	
	public String getEmail() 		{ return email;}
	public boolean Error() 			{ return error;}
	public String getErrorMsg() 	{ return errmsg;}
	public boolean getMobile()		{ return mobile;}

	public void setEmail(String s) 		{ email = s;}
	public void setError(boolean b) 	{ error = b;}
	public void setErrorMsg(String s) 	{ errmsg = s;}
	public void setMobile(boolean b)		{ mobile = b;}

}
