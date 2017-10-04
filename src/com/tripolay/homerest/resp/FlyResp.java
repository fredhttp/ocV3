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
public class FlyResp 
{
	
	private String deptAirport;
	private String deptAirportCode;
	private String deptGate;
	private String deptDate;
	private String deptTime;
	private String arrvAirport;
	private String arrvAirportCode;
	private String arrvGate;
	private String arrvDate;
	private String arrvTime;
	private String airline;
	private String airlineCode;
	private String aircraft;
	private String flightNum;
	private String flightDur;
	private String currency;
	private double totalprice;
	private int rec;
	
	
	public FlyResp()
	{
		deptAirport = "N/A";
		deptAirportCode = "N/A";
		deptGate	= "N/A";
		deptDate 	= "N/A";
		deptTime	= "N/A";
		arrvAirport = "N/A";
		arrvAirportCode = "N/A";
		arrvGate 	= "N/A";
		arrvDate 	= "N/A";
		arrvTime 	= "N/A";
		airline 	= "N/A";
		airlineCode = "N/A";
		aircraft 	= "N/A";
		flightNum 	= "N/A";
		flightDur 	= "N/A";
		currency 	= "N/A";
		totalprice 	= 0;
	}
	
	public String getdeptAirport() 	{ return deptAirport; }
	public String getdeptAirportCode() 	{ return deptAirportCode; }
	public String getdeptGate()    	{ return deptGate; 	  }
	public String getdeptDate()    	{ return deptDate; 	  }
	public String getdeptTime() 	{ return deptTime;    }
	public String getarrvAirport() 	{ return arrvAirport; }
	public String getarrvAirportCode() 	{ return arrvAirportCode; }
	public String getarrvGate() 	{ return arrvGate;    }
	public String getarrvDate() 	{ return arrvDate;    }
	public String getarrvTime() 	{ return arrvTime;    }
	public String getAirline() 		{ return airline;     }
	public String getAirlineCode() 	{ return airlineCode; }
	public String getAircraft() 	{ return aircraft;    }
	public String getFlightNum() 	{ return flightNum;   }
	public String getFlightDur() 	{ return flightDur;   }
	public String getCurrency() 	{ return currency;    }
	public double getTotalPrice()   { return totalprice;       }
	public int getRec()				{ return rec;		}
	
	
	public void setdeptAirport(String s) 	{ deptAirport = s; }
	public void setdeptAirportCode(String s) 	{ deptAirportCode = s; }
	public void setdeptGate(String s)    	{ deptGate    = s; }
	public void setdeptDate(String s)    	{ deptDate    = s; }
	public void setdeptTime(String s) 		{ deptTime    = s; }
	public void setarrvAirport(String s) 	{ arrvAirport = s; }
	public void setarrvAirportCode(String s) 	{ arrvAirportCode = s; }
	public void setarrvGate(String s) 		{ arrvGate    = s; }
	public void setarrvDate(String s) 		{ arrvDate   = s; }
	public void setarrvTime(String s) 		{ arrvTime    = s; }
	public void setAirline(String s) 		{ airline     = s; }
	public void setAirlineCode(String s) 	{ airlineCode = s; }
	public void setAircraft(String s) 		{ aircraft    = s; }
	public void setFlightNum(String s) 		{ flightNum   = s; }
	public void setFlightDur(String s) 		{ flightDur   = s; }
	public void setCurrency(String s) 		{ currency    = s; }
	public void setTotalPrice(double d)     { totalprice  = d; }
	public void setRec(int n)				{ rec = n;}
	
		
}
