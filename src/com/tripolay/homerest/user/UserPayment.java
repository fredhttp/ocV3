package com.tripolay.homerest.user;

import java.util.ArrayList;
import java.util.List;

public class UserPayment 
{
	List<String> listPayPal = new ArrayList<String>();
	List<String> listVisa = new ArrayList<String>();
	List<String> listMaster = new ArrayList<String>();
	 
	public List<String> getPayPal() { return listPayPal;}
	public List<String> getVisa() 	{ return listVisa;}
	public List<String> getMaster() { return listMaster;}
		
	public void setPayPal(List<String> list) 	{ this.listPayPal = list;}
	public void setVisa(List<String> list) 		{ this.listVisa = list;}
	public void setMaster(List<String> list) 	{ this.listMaster = list;}

	 	  
}
