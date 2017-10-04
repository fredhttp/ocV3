package com.tripolay.homerest.resp;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PaymentCheck 
{
	int recnum;
	int itinclass;
	double price;
	
	public PaymentCheck(int recnum, int itinclass, double price)
	{
		this.recnum = recnum;
		this.itinclass = itinclass;
		this.price = price;
	}
	public int getRec() 		{ return recnum; }
	public int getItinClass() 	{ return itinclass;}
	public double getPrice() 	{ return price;}

}
