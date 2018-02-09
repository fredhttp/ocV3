package com.tripolay.homerest.svc;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.tripolay.homerest.resp.FlyResp;
import com.tripolay.svc.fly.Rome2Rio;



public class FlySvc
{
	String src;
	String dst;
	String deptDateTime;
	int numPasseng;
	boolean oneWay;
	
	final static Logger LOGGER = Logger.getLogger(FlySvc.class.getName()); 
	
	public FlySvc(String deptCity, String arrvCity)
	{
		this.src = deptCity;
		this.dst = arrvCity;
	}
	public List<FlyResp> Search()
	{
		URI baseURI = UriBuilder.fromUri("http://svc-tripolay.rhcloud.com/svcrest/").build();
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(baseURI);
		
		JSONObject input = new JSONObject();
		input.put("src", src);
		input.put("dst", dst);
		
		List<FlyResp> fltlist = new ArrayList<FlyResp>();
		try
		{
			fltlist = new Rome2Rio(this.src, this.dst).Search(); 
			sortFlyList(fltlist);
			LOGGER.log(Level.INFO,"flt list size: " + String.valueOf(fltlist.size()));
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return fltlist;
	}
	public void Reserve()
	{
		
	}
	public void sortFlyList(List<FlyResp> fltlist)
    {
        try {
            Collections.sort(fltlist, new Comparator<FlyResp>() {
                public int compare(FlyResp o1, FlyResp o2) {
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
