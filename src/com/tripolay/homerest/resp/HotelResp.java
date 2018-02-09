package com.tripolay.homerest.resp;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * 		must add @XmlRootElement to both client and server side code.
 * 		server had @XmlRootElement but client was missing @XmlRootElement
 * 		lots of errors. 
 * 		org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException:
 * 		MessageBodyReader not found for media type=application/xml, 
 * 	    type=class ...........
 * 
 */
@XmlRootElement
public class HotelResp 
{
	String currencycode;
	String deeplink;
	String resultid;
	String hwrefnumber;
	double subtotal;
	double taxesandfees;
	double totalprice;
	List<String> amenity;
	String checkindate;
	String checkoutdate; 
	String neighborhood;
	String lodgingtypecode; 
	int nights; 
	double averagepricepernight; 
	double recommendationpercentage; 
	int rooms; 
	double savingspercentage; 
	List<String> specialtaxitems; 
	double starrating; 
	int rec;
	
	
	
	
	
	public HotelResp()
	{
		this.currencycode 	= "N/A";
		this.deeplink     	= "N/A";
		this.resultid  		= "N/A";
		this.hwrefnumber 	= "N/A";
		this.subtotal 		= 0;
		this.taxesandfees 	= 0;
		this.totalprice 	= 0;
		this.amenity 		= new ArrayList<String>();
		this.checkindate 	= "N/A";
		this.checkoutdate 	= "N/A";
		this.neighborhood 	= "N/A";
		this.lodgingtypecode = "N/A";
		this.nights 		= 0; 
		this.averagepricepernight = 0; 
		this.recommendationpercentage = 0; 
		this.rooms 			= 0; 
		this.savingspercentage =0 ; 
		this.specialtaxitems = new ArrayList<String>(); 
		this.starrating 	= 0; 
	}
	
	
	public String getCurrencyCode() { return currencycode; }
	public String getDeepLink() 	{ return deeplink; }
	public String getResultId() 	{ return resultid; }
	public String gettHWRefNumber() { return hwrefnumber; }
	public double  getSubTotal() 	{ return subtotal; }
	public double  getTaxesAndFees() { return taxesandfees; }
	public double  getTotalPrice()	{ return totalprice; }
	
	
	public List<String> getAmenity()	{ return amenity; }
	public String getCheckInDate()		{ return checkindate;}
	public String getCheckOutDate()		{ return checkoutdate;}
	public String getNeighborhood() 	{ return neighborhood;}
	public String getLodgingTypeCode() 	{ return lodgingtypecode;}
	public int    getNights() 			{ return nights; }
	public double  getAveragePricePerNight() 	{ return averagepricepernight;}
	public double  getRecommendationPercentage() { return recommendationpercentage; }
	public int    getRooms() 			{ return rooms; }
	public double  getSavingsPercentage() { return savingspercentage; }
	public List<String> getSpecialTaxItems()	{ return specialtaxitems; }
	public double  getStarRating() 		{ return starrating; }
	public int getRec()					{ return rec; }
	
	
	public void setCurrencyCode(String s) 	{ this.currencycode = s; }
	public void setDeepLink(String s) 		{ this.deeplink = s;}
	public void setResultId(String s) 		{ this.resultid = s;}
	public void setHWRefNumber(String s) 	{ this.hwrefnumber = s;}
	public void setSubTotal(double d) 		{ this.subtotal = d; }
	public void setTaxesAndFees(double d) 	{ this.taxesandfees = d; }
	public void setTotalPrice(double d)		{ this.totalprice = d; }
	
	
	public void setAmentiy(List<String> s) 		{ this.amenity = s;}
	public void setCheckInDate(String s)		{ this.checkindate = s;}
	public void setCheckOutDate(String s)		{ this.checkoutdate = s;}
	public void setNeighborhood(String s)		{ this.neighborhood = s;}
	public void setLodgingTypeCode(String s) 	{ this.lodgingtypecode = s;}
	public void setNights(int n) 				{ this.nights = n;}
	public void setAveragePricePerNight(double d) { this.averagepricepernight = d; }
	public void setRecommendationPercentage(double d) { this.recommendationpercentage = d;}
	public void setRooms(int n) 				{ this.rooms = n;}
	public void setSavingsPercentage(double d) 	{ this.savingspercentage = d;}
	public void setSpecialTaxItems(List<String> s) 	{ this.specialtaxitems= s;}
	public void setStarRating(double d) 			{ this.starrating = d;}
	public void setRec(int n)					{ this.rec = n;}
	

}
