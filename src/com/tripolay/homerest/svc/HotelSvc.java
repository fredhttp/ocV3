package com.tripolay.homerest.svc;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.json.JSONObject;

import com.tripolay.homerest.resp.HotelResp;
import com.tripolay.svc.htl.HotwireHotel;


public class HotelSvc 
{
	private String dst;
	private int limit;
	private int rooms;
	private int adults;
	private int children;
	private String sdate;
	private String edate;
	
	final static Logger LOGGER = Logger.getLogger(HotelSvc.class.getName()); 
	
	public HotelSvc(String dst)
	{
		this.dst = dst;
		this.rooms = 1;
		this.adults = 1;
		this.children = 0;
		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		this.sdate = sdf.format(dt);
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(dt); 
		c.add(Calendar.DATE, 4);
		dt = c.getTime();
		this.edate = sdf.format(dt);
	}
	
	public HotelSvc(String dst, String sdate, String edate)
	{
		this.dst = dst;
		this.sdate = sdate;
		this.edate = edate;
		this.limit = 20;
		this.rooms = 1;
		this.adults = 1;
		this.children = 0;
	}
	public List<HotelResp> Search()
	{
		List<HotelResp> hotellist = new ArrayList<HotelResp>();
		try
		{
			hotellist = new HotwireHotel(this.dst,this.limit,this.rooms,this.adults,this.children,
							this.sdate,this.edate).Search();
			
			sortHotelList(hotellist);
			LOGGER.log(Level.INFO,"htl list size: " + String.valueOf(hotellist.size()));
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.INFO,ex.getMessage());
		}
		return hotellist;
	}
	public void Reserve()
	{
		
	}
	public void sortHotelList(List<HotelResp> hotellist)
    {
        try {
            Collections.sort(hotellist, new Comparator<HotelResp>() {
                public int compare(HotelResp o1, HotelResp o2) {
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
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
