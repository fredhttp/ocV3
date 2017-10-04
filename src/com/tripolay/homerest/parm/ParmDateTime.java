package com.tripolay.homerest.parm;

import com.tripolay.homerest.util.MyDate;

public class ParmDateTime 
{
	String startDate = "";
	String endDate = "";
	String startTime = "";
	String endTime = "";
	
	public ParmDateTime(String sdatetime,String edatetime)
	{
		init(sdatetime,edatetime);
	}
	private void init(String sdatetime,String edatetime)
	{
		String[] startArray = MyDate.parse(sdatetime);
		String[] endArray   = MyDate.parse(edatetime);
	
		// both start datetime and end datetime are available
		if(startArray[0].length() > 1 && endArray[0].length() > 1 )
		{
			startDate = startArray[0];
			startTime = startArray[1];
			endDate   = endArray[0];
			endTime   = endArray[1];
		}
		else if(startArray[0].length() > 1)   // only start date is filled 
		{
			startDate = startArray[0];
			startTime = startArray[1]; 
			
			endDate = MyDate.addDays(startDate, 2);
			endTime = "13:00";
		}
		else if(endArray[0].length() > 1)	// only end date is filled
		{
			startDate = MyDate.Now();
			startTime = "22:00";
			endDate = endArray[0];
			endTime = endArray[1]; 
		}
		else
		{
			startDate = MyDate.Now();	// default or if date is not filled up 
			endDate = MyDate.Next(2);
			startTime = "22:00";
			endTime = "13:00";
			
		}
	}
	public String getStartDate() 	{ return startDate;}
	public String getEndDate()		{ return endDate;}
	public String getStartTime()	{ return startTime;}
	public String getEndTime()		{ return endTime;}

}
