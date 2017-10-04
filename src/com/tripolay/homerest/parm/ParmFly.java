package com.tripolay.homerest.parm;


public class ParmFly 
{
	String sessionID ="";
	String src = "";
	String dst = "";
	String startdate = "";
	String enddate = "";
	String starttime = "";
	String endtime = "";
	String lat = "";
	String lon = "";
	
	
	
	boolean error = false;
	String errmsg = "";
	
	public String getSessionID() 	{ return sessionID;}
	public String getSrc() 			{ return src;}
	public String getDst() 			{ return dst;}
	public String getStartDate()	{ return startdate;}
	public String getEndDate()		{ return enddate;}
	public String getStartTime()	{ return starttime;}
	public String getEndTime()		{ return endtime;}
	public String getLat()			{ return lat;}
	public String getLon()			{ return lon;}
	
	
	public boolean Error() 			{ return error;}
	public String getErrorMsg() 	{ return errmsg;}

	
	public void setSessionID(String s) 	{ sessionID = s;}
	public void setSrc(String s) 		{ src = s;}
	public void setDst(String s) 		{ dst = s;}
	public void setStartDate(String s)	{ startdate = s;}
	public void setEndDate(String s)	{ enddate =s;}
	public void setStartTime(String s)	{ starttime = s;}
	public void setEndTime(String s)	{ endtime = s;}
	public void setLat(String s)		{ lat = s;}
	public void setLon(String s)		{ lon = s;}
	
	public void setError(boolean b) 	{ error = b;}
	public void setErrorMsg(String s) 	{ errmsg = s;}
	
}
