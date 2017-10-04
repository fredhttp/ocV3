package com.tripolay.homerest.parm;


public class ParmSignUp 
{
	String regcode = "";  
	String fname = "";   
    String lname = "";
    String phone = "";
    String hnumber = "";
    String street = "";    
    String city = "";
    String state = "";
    String zip = "";
    String country = "";
    String passwd = "";
    String gresp = "";
    boolean mobile = false;
    
    boolean error = false;
	String errmsg = "";
    
    public ParmSignUp()
    { }
    
	public String getRegCode() 		{ return regcode;}
	public String getFirstName() 	{ return fname;}
	public String getLastName() 	{ return lname;}
	public String getPhone() 		{ return phone;}
	public String getHouseNum() 	{ return hnumber;}
	public String getStreet() 		{ return street;}
	public String getCity() 		{ return city;}
	public String getState() 		{ return state;}
	public String getZip() 			{ return zip;}
	public String getCountry() 		{ return country;}
	public String getPasswd() 		{ return passwd;}
	public String getGresp() 		{ return gresp;}
	public boolean getMobile()		{ return mobile;}
	public boolean Error() 			{ return error;}
	public String getErrorMsg() 	{ return errmsg;}
	
	public void setRegCode(String s) 		{ regcode = s;}
	public void setFirstName(String s) 		{ fname = s;}
	public void setLastName(String s) 		{ lname = s;}
	public void setPhone(String s) 			{ phone = s;}
	public void setHouseNumber(String s) 	{ hnumber = s;} 
	public void setStreet(String s) 		{ street = s;}
	public void setCity(String s) 			{ city = s;}
	public void setState(String s) 			{ state = s;}
	public void setZip(String s) 			{ zip = s;}
	public void setCountry(String s) 		{ country = s;}
	public void setPasswd(String s) 		{ passwd = s;}
	public void setGresp(String s) 			{ gresp = s;}
	public void setMobile(boolean b)		{ mobile = b;}
	public void setError(boolean b) 	{ error = b;}
	public void setErrorMsg(String s) 	{ errmsg = s;}
	
	

}
