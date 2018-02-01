package com.tripolay.homerest.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

public class MyDate 
{
	
	public static String[] parse(String datetime)
	{
		String[] ret = {"",""};
		try
		{
			if(datetime != null && datetime.length() > 10 && datetime.length() < 17 )
			{	 
				 LocalDate localDate = fmt.parseLocalDate(datetime); 
				 LocalTime localTime = fmt.parseLocalTime(datetime); 
				 DateTimeFormatter FM = DateTimeFormat.forPattern("MM/dd/yyyy");
				 ret[0] = FM.print(localDate);
				 FM = DateTimeFormat.forPattern("HH");
				 ret[1] = FM.print(localTime)+":00"; 
			}
		}
		catch(Exception ex) {ex.printStackTrace();}	 
		return ret;
	}
	public static String Now()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance(); 
		return sdf.format(cal.getTime());
	}
	public static String addDays(String strDate , int days)
	{
		LocalDate localDate = fmt.parseLocalDate(strDate);
		DateTimeFormatter FM = DateTimeFormat.forPattern("MM/dd/yyyy");
		return FM.print(localDate.plusDays(days));
	}
	public static String Next(int days)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance(); 
		cal.add(Calendar.DATE, days);
		return sdf.format(cal.getTime());
	}
	
	
	static DateTimeFormatter fmt = new DateTimeFormatterBuilder()
			 .append(null, new DateTimeParser[]{
		                DateTimeFormat.forPattern("yyyy-MM-dd").getParser(),   // has a better visual than / / 
		                DateTimeFormat.forPattern("yyyy-MM-dd HH").getParser(),
		                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").getParser(),
		                DateTimeFormat.forPattern("MM/dd/yyyy").getParser()})
		        .toFormatter();
	

}
