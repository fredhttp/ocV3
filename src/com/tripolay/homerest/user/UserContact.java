package com.tripolay.homerest.user;

import java.util.ArrayList;
import java.util.List;

public class UserContact 
{
	List<String> listPhone 	= new ArrayList<String>();
	List<String> listEmail 	= new ArrayList<String>();
	List<String> listFB 	= new ArrayList<String>();
	List<String> listTwitter = new ArrayList<String>();
	
	public List<String> getPhone() 		{ return listPhone;}
	public List<String> getEmail() 		{ return listEmail;}
	public List<String> getFB() 		{ return listFB;}
	public List<String> getTwitter() 	{ return listTwitter;}
	
	public void setPhone(List<String> list) 	{ this.listPhone = list;}
	public void setEmail(List<String> list) 	{ this.listEmail = list;}
	public void setFB(List<String> list) 		{ this.listFB = list;}
	public void setTwitter(List<String> list) 	{ this.listTwitter = list;}
	
}
