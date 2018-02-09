package com.tripolay.homerest.parm;

public class ParmCity 
{
	
	String deptCity = "";
	String arrvCity = "";
	String errorMsg = "";
	
	boolean oneParm = false;
	boolean error = false;
	
	public String getDeptCity() { return deptCity;}
	public String getArrvCity() { return arrvCity;}
	public String getErrorMsg() { return errorMsg;}
	public boolean getOneParm() { return oneParm;}
	public boolean Error() { return error;}
	
	public void setDeptCity(String s) {deptCity = s;}
	public void setArrvCity(String s) {arrvCity = s;}
	public void setErrorStr(String s) {errorMsg = s;}
	public void setError(boolean b)   { error = b;}
	public void setOneParm(boolean b) {oneParm = b;}
	
	public String toString()
	{
		if(error)
			return errorMsg  + " oneParm: " + oneParm ;
		else
			return "deptCity: " + deptCity + " arrvCity: " + arrvCity +  " oneParm: " + oneParm ; 
	}

}
