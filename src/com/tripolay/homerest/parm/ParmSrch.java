package com.tripolay.homerest.parm;


public class ParmSrch 
{
	String sessionID ="";
	String src = "";
	String dst = "";
	String sdate = "";
	String edate = "";
	String stime = "";
	String etime = "";
	boolean error = false;
	String errmsg = "";
	
	public String getSessionID() 	{ return sessionID;}
	public String getSrc() 			{ return src;}
	public String getDst() 			{ return dst;}
	public String getStartDate() 	{ return sdate;}
	public String getEndDate() 		{ return edate;}
	public String getStartTime() 	{ return stime;}
	public String getEndTime() 		{ return etime;}
	public boolean Error() 			{ return error;}
	public String getErrorMsg() 	{ return errmsg;}
	
	public void setSessionID(String s) 	{ sessionID = s;}
	public void setSrc(String s) 		{ src = s;}
	public void setDst(String s) 		{ dst = s;}
	public void setStartDate(String s) 	{ sdate = s;}
	public void setEndDate(String s) 	{ edate = s;}
	public void setStartTime(String s) 	{ stime = s;}
	public void setEndTime(String s) 	{ etime = s;}
	public void setError(boolean b) 	{ error = b;}
	public void setErrorMsg(String s) 	{ errmsg = s;}
	
	
}
