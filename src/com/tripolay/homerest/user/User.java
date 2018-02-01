package com.tripolay.homerest.user;

import java.util.ArrayList;
import java.util.List;

public class User 
{
	
	String userid;
	String passwd; 
	String fname;
	String lname;
	String phone;
	String lastlogin;
	int visits; 
	UserPayment payment;
	UserContact contact;
	Address address;
	List<UserItinAir> listItinAir = new ArrayList<UserItinAir>(); 
	List<UserItinHotel> listItinHotel = new ArrayList<UserItinHotel>(); 
	List<UserItinCar> listItinCar = new ArrayList<UserItinCar>(); 
	
	public User(String userid, String passwd)
	{
		this.userid = userid; 
		this.passwd = passwd;
	}
	
	public String getID() 			{ return userid;}
	public String getPasswd() 		{ return passwd;}
	public String getFirstName() 	{ return fname;}
	public String getLastName() 	{ return lname;}
	public String getPhone() 		{ return phone;}
	public String getLastlogin()	{ return lastlogin;}
	public int getVisits() 			{ return visits;}
	public UserPayment getPayment() { return payment;}
	public UserContact getContact() { return contact;}
	public Address getAddress() { return address;}
	public List<UserItinAir> getItinAir() 		{ return listItinAir;}
	public List<UserItinHotel> getItinHotel() 	{ return listItinHotel;}
	public List<UserItinCar> getItinCar() 		{ return listItinCar;}
	
	public void setUserid(String str) 		{ this.userid = str;}
	public void setPasswd(String str) 		{ this.passwd = str;}
	public void setFirstName(String str) 	{ this.fname = str;}
	public void setLastName(String str) 	{ this.lname = str;}
	public void setPhone(String str) 		{ this.phone = str;}
	public void setLastlogin(String str) 	{ this.lastlogin = str;}
	public void setVisits(int n) 			{ this.visits = n;}
	public void setPayment(UserPayment obj) { this.payment = obj;}
	public void setContact(UserContact obj) { this.contact = obj;}
	public void setAddress(Address obj) { this.address = obj;}
	public void setItinAir(List<UserItinAir> list) 		{ this.listItinAir = list;}
	public void setItinHotel(List<UserItinHotel> list) 	{ this.listItinHotel = list;}
	public void setItinCar(List<UserItinCar> list) 		{ this.listItinCar = list;}
	
	
	

}
