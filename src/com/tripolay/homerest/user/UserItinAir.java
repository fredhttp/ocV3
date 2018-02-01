package com.tripolay.homerest.user;

import java.util.ArrayList;
import java.util.List;

public class UserItinAir 
{
	String src;
	String dst;
	String airline;
	String cabin;
	String price;
	List<String> listStops = new ArrayList<String>();
	String date;
	
	public UserItinAir(String src,String dst,String airline,String cabin,String price,List<String> listStops,String date)
	{
		this.src = src;
		this.dst = dst;
		this.airline = airline;
		this.cabin = cabin;
		this.price = price;
		this.listStops = listStops;
		this.date = date;
	}
	
	public String getSrc() 		{ return src;}
	public String getDest() 	{ return dst;}
	public String getAirline() 	{ return airline;}
	public String getCabin() 	{ return cabin;}
	public String getPrice()	{ return price;}
	public List<String> getStops() { return listStops;}
	public String getDate()		{ return date;}

}
