package com.tripolay.homerest.resp;

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
public class CarResp 
{
	String currencycode;
	String deeplink;
	String resultid;
	String hwrefnumber;
	double subtotal;
	double taxesandfees;
	double totalprice;
	double dailyrate;
	String dropoffday;
	String dropoffTime;
	String pickupday;
	String pickuptime;
	String locationdescription;
	String mileagedescription;
	String pickupairport;
	String rentaldays;
	String typicalseating;
	String typename;
	String typecode;
	String features;
	String model;
	int rec;
	
	public CarResp()
	{
		currencycode 	= "N/A";
		deeplink 		= "N/A";
		resultid 		= "N/A";
		hwrefnumber		= "N/A";
		subtotal 		= 0;
		taxesandfees 	= 0;
		totalprice 		= 0;
		dailyrate 		= 0;
		dropoffday 		= "N/A";
		dropoffTime 	= "N/A";
		pickupday 		= "N/A";
		pickuptime 		= "N/A";
		locationdescription = "N/A";
		mileagedescription = "N/A";
		pickupairport 	= "N/A";
		rentaldays 		= "N/A";
		typicalseating 	= "N/A";
		typename 		= "N/A";
		typecode 		= "N/A";
		features 		= "N/A";
		model 			= "N/A";
	}
	
	
	
	public String getCurrencyCode() { return currencycode; }
	public String getDeepLink() 	{ return deeplink; }
	public String getResultId() 	{ return resultid; }
	public String gettHWRefNumber() { return hwrefnumber; }
	public double getSubTotal() 	{ return subtotal; }
	public double getTaxesAndFees() { return taxesandfees; }
	public double getTotalPrice()	{ return totalprice; }
	public double getDailyRate() 	{ return dailyrate; }
	public String getDropoffDay()	{ return dropoffday; }
	public String gettDropoffTime()	{ return dropoffTime;}
	public String getPickupDay()	{ return pickupday;}
	public String getPickupTime() 	{ return pickuptime;}
	public String getLocationDescription() { return locationdescription;}
	public String getMileageDescription() { return mileagedescription; }
	public String getPickupAirport() 	{ return pickupairport ;}
	public String getRentalDays() { return rentaldays; }
	public String getTypicalSeating() 	{ return typicalseating; }
	public String getCarTypeName() 		{ return typename; }
	public String getPossibleFeatures() { return features; }
	public String getPossibleModels() 	{ return model; }
	public int getRec() 				{ return rec; }
	
	
	
	public void setCurrencyCode(String s) 	{ this.currencycode = s; }
	public void setDeepLink(String s) 		{ this.deeplink = s;}
	public void setResultId(String s) 		{ this.resultid = s;}
	public void setHWRefNumber(String s) 	{ this.hwrefnumber = s;}
	public void setSubTotal(double d) 		{ this.subtotal = d; }
	public void setTaxesAndFees(double d) 	{ this.taxesandfees = d; }
	public void setTotalPrice(double d)		{ this.totalprice = d; }
	public void setDailyRate(double d) 		{ this.dailyrate = d;}
	public void setDropoffDay(String s)		{ this.dropoffday = s;}
	public void setDropoffTime(String s)	{ this.dropoffTime = s;}
	public void setPickupDay(String s)		{ this.pickupday = s;}
	public void setPickupTime(String s) 	{ this.pickuptime = s;}
	public void setLocationDescription(String s) { this.locationdescription= s;}
	public void setMileageDescription(String s) { this.mileagedescription = s; }
	public void setPickupAirport(String s) 	{ this.pickupairport = s;}
	public void setRentalDays(String s) 	{ this.rentaldays = s;}
	
	public void setTypicalSeating(String s) { this.typicalseating = s; }
	public void setCarTypeName(String s) 	{ this.typename = s;}
	public void setPossibleFeatures(String s) 	{ this.features = s;}
	public void setPossibleModels(String s) 	{ this.model = s;}
	public void setRec(int n)					{ this.rec = n;}
	

}
