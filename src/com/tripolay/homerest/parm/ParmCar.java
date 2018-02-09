package com.tripolay.homerest.parm;

public class ParmCar 
{
	String sessionID ="";
	String dst = "";
	String startdate = "";
	String enddate = "";
	String picktime = "";
	String droptime = "";
	
	boolean error = false;
	String errmsg = "";
	
	public String getSessionID() 	{ return sessionID;}
	public String getDst() 			{ return dst;}
	public String getStartDate()	{ return startdate;}
	public String getEndDate()		{ return enddate;}
	public String getPickTime()		{ return picktime;}
	public String getDropTime()		{ return droptime;}
	public boolean Error() 			{ return error;}
	public String getErrorMsg() 	{ return errmsg;}

	
	public void setSessionID(String s) 	{ sessionID = s;}
	public void setDst(String s) 		{ dst = s;}
	public void setStartDate(String s)	{ startdate = s;}
	public void setEndDate(String s)	{ enddate =s;}
	public void setPickTime(String s)	{ picktime = s;}
	public void setDropTime(String s)	{ droptime = s;}
	public void setError(boolean b) 	{ error = b;}
	public void setErrorMsg(String s) 	{ errmsg = s;}

}
