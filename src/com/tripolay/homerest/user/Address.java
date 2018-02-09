package com.tripolay.homerest.user;

public class Address 
{
	String number;
	String street;
	String city;
	String zip;
	String state;
	String country;
	
	
	public Address(String number,String street,String city,String zip,String state,String country)
	{
		this.number = number;
		this.street = street;
		this.city = city;
		this.zip = zip;
		this.state = state;
		this.country = country;
	}
	
	public String getNumber() 	{ return number;}
	public String getStreet() 	{ return street;}
	public String getCity() 	{ return city;}
	public String getZip() 		{ return zip;}
	public String getState() 	{ return state;}
	public String getCountry() 	{ return country;}

}
