package com.tripolay.homerest.user;

public class UserItinCar 
{
	
	String city;
	String name;
	Address address; 
	String pickupdate;
	String dropoffdate;
	String model;
	String make;
	String dailyrate;
	String totalprice;  
	
	public UserItinCar(String city,String name,Address address,String pickupdate,String dropoffdate,
			String model, String make,String dailyrate,String totalprice)
	{
		this.city = city;
		this.name = name;
		this.address = address;
		this.pickupdate = pickupdate;
		this.dropoffdate = dropoffdate;
		this.model = model;
		this.make = make;
		this.dailyrate = dailyrate;
		this.totalprice = totalprice;
	}
	
	public String getCity() 	{ return city;}
	public String getName() 	{ return name;}
	public Address getAddress()	{ return address;}
	public String getPickupDate() 	{ return pickupdate;}
	public String getDropoffDate() 	{ return dropoffdate;}
	public String getModel()	{ return model;}
	public String getMake() 	{ return make;}
	public String getDailyRate(){ return dailyrate;}
	public String getTotalPrice() 	{ return totalprice;}
}
