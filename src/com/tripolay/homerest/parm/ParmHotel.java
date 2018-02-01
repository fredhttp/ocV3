package com.tripolay.homerest.parm;

public class ParmHotel 
{
	
	String sessionID ="";
	String dst = "";
	String startdate = "";
	String enddate = "";
	int limit = 20;
	int rooms = 1; 
	int adults = 1;
	int children = 1;
	
	boolean error = false;
	String errmsg = "";
	
	public String getSessionID() 	{ return sessionID;}
	public String getDst() 			{ return dst;}
	public String getStartDate()	{ return startdate;}
	public String getEndDate()		{ return enddate;}
	public int getLimit()			{ return limit;}
	public int getRooms()			{ return rooms;}
	public int getAdults()			{ return adults;}
	public int getChildren()		{ return children;}
	public boolean Error() 			{ return error;}
	public String getErrorMsg() 	{ return errmsg;}

	
	public void setSessionID(String s) 	{ sessionID = s;}
	public void setDst(String s) 		{ dst = s;}
	public void setStartDate(String s)	{ startdate = s;}
	public void setEndDate(String s)	{ enddate =s;}
	public void setLimit(int n)			{ limit = n;}
	public void setRooms(int n)			{ rooms = n;}
	public void setAdults(int n)		{ adults = n;}
	public void setChildren(int n)		{ children = n;}
	public void setError(boolean b) 	{ error = b;}
	public void setErrorMsg(String s) 	{ errmsg = s;}
	

}
