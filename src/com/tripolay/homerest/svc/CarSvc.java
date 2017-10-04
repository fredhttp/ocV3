package com.tripolay.homerest.svc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tripolay.homerest.resp.CarResp;
import com.tripolay.svc.car.HotwireCar;


public class CarSvc 
{
	private String dst;
	private String sdate;
	private String edate;
	private String picktime;
	private String droptime;
	
	final static Logger LOGGER = Logger.getLogger(CarSvc.class.getName()); 
	
	public CarSvc(String dst,String startdate,String enddate,String picktime,String droptime)
	{
		this.dst = dst;
		this.sdate = startdate;
		this.edate = enddate;
		this.picktime = picktime;
		this.droptime = droptime;
	}
	public List<CarResp> Search()
	{
		List<CarResp> carlist = new ArrayList<CarResp>();
		try
		{
			carlist = new HotwireCar(this.dst,this.sdate,this.edate,this.picktime,this.droptime).Search();
			sortCarList(carlist);
			LOGGER.log(Level.INFO,"car list size: " + String.valueOf(carlist.size()));
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING, ex.getMessage());
		}
		return carlist;

	}
	public void Reserve()
	{
		
	}
	public void sortCarList(List<CarResp> carlist)
    {
        try {
            Collections.sort(carlist, new Comparator<CarResp>() {
                public int compare(CarResp o1, CarResp o2) {
                    double f1 = Double.valueOf(o1.getTotalPrice());
                    double f2 = Double.valueOf(o2.getTotalPrice());
                    int n1 = (int) f1;
                    int n2 = (int) f2;
                    if (n1 == n2)
                        return 0;
                    else
                        return n1 < n2 ? -1 : 1;
                }
            });
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
