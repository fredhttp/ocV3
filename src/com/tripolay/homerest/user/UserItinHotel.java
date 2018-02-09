package com.tripolay.homerest.user;

public class UserItinHotel 
{
	String city;
	String name;
	Address address;  
	String nights;
	String datein;
	String dateout;
	String dailyrate;
	String totalprice;  
	
	public UserItinHotel(String city,String name,Address address, String nights,String datein,String dateout,String dailyrate,String totalprice)
	{
		this.city = city;
		this.name = name;
		this.address = address;
		this.nights = nights;
		this.datein = datein;
		this.dateout = dateout;
		this.dailyrate = dailyrate;
		this.totalprice = totalprice;
	}
	
	public String getCity() 	{ return city;}
	public String getName() 	{ return name;}
	public Address getAddress()	{ return address;}
	public String getNights() 		{ return nights;}
	public String getDateIN() 	{ return datein;}
	public String getDateOUT() 	{ return dateout;}
	public String getDailyRate(){ return dailyrate;}
	public String getTotalPrice() 	{ return totalprice;}

}
