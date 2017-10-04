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

import com.tripolay.homerest.resp.CarResp;


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
		URI baseURI = UriBuilder.fromUri("http://svc-tripolay.rhcloud.com/svcrest/").build();
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target(baseURI);
		
		JSONObject input = new JSONObject();
		input.put("dst", dst);
		input.put("sdate", sdate);
		input.put("edate", edate);
		input.put("picktime", picktime);
		input.put("droptime", droptime);
		
		List<CarResp> carlist = new ArrayList<CarResp>();
		try
		{
			Response resp = target.path("svc").path("car").request().post(Entity.entity(input.toString(),MediaType.APPLICATION_JSON),Response.class);
			LOGGER.log(Level.INFO,String.valueOf(resp.getStatus()));
			carlist = resp.readEntity(new GenericType<List<CarResp>>(){});
			sortCarList(carlist);
			LOGGER.log(Level.INFO,"car list size: " + String.valueOf(carlist.size()));
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING,ex.getMessage());
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
