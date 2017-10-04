package com.tripolay.homerest.app;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



@WebListener
public class Config implements ServletContextListener
{
	public static Map<String,String> mapALcode  = new TreeMap<String,String>(); 
    public static Map<String,String> mapAPcodeUS  = new TreeMap<String,String>(); 
    
    public static JSONObject objAreaCode;
	public static JSONObject objIata;
	public static JSONObject objState;
	public static JSONObject objNfl;
	public static JSONObject objLatLon;
	public static List<String> listNFL = new ArrayList<String>();
	
	MongoClient mongoClient;
	
	MongoDatabase mongoDBSession;
	MongoDatabase mongoDBUsers;
	
	MongoCollection<Document> sessionColFly;
	MongoCollection<Document> sessionColHtl;
	MongoCollection<Document> sessionColCar;	
	
	final static Logger LOGGER = Logger.getLogger(Config.class.getName()); 
	
	
	public void contextInitialized(ServletContextEvent event) 
	{
		LOGGER.log(Level.INFO,"ServletContextListener started homerest");   
        ServletContext context = event.getServletContext();
		setup(context);
		setupMongoDB();
		setupNFL();
		event.getServletContext().setAttribute("config", this);	
    }
    public void contextDestroyed(ServletContextEvent event) 
    {	
    	mongoClient.close();
    	LOGGER.log(Level.INFO,"context destroyed, homerest closing ....");     
    }
    public MongoDatabase getDBUsers()
    {
    	return mongoDBUsers;
    }
    public MongoCollection<Document> getSessionColFly()
    {
    	return sessionColFly;
    }
    public MongoCollection<Document> getSessionColHtl()
    {
    	return sessionColHtl;
    }
    public MongoCollection<Document> getSessionColCar()
    {
    	return sessionColCar;
    }
    private void setup(ServletContext ctx) 
    {
    	InputStream in = null;
    	String str = "";
		try 
		{
			in = ctx.getResourceAsStream("/WEB-INF/areacode_to_city.txt");
			if(in != null)
			{
				str = IOUtils.toString( in ) ;
				objAreaCode = new JSONObject(str);
			}
			in = ctx.getResourceAsStream("/WEB-INF/state_to_city.txt");
			if(in != null)
			{
				str = IOUtils.toString( in ) ;
				objState = new JSONObject(str);
			}
			in = ctx.getResourceAsStream("/WEB-INF/nfl_to_city.txt");
			if(in != null)
			{
				str = IOUtils.toString( in ) ;
				objNfl = new JSONObject(str);
			}
			in = ctx.getResourceAsStream("/WEB-INF/iata_to_city.txt");
			if(in != null)
			{
				str = IOUtils.toString( in ) ;
				objIata = new JSONObject(str);
			}
			in = ctx.getResourceAsStream("/WEB-INF/iata_lat_lon_major_airport_US.txt");
			if(in != null)
			{
				str = IOUtils.toString( in ) ;
				objLatLon = new JSONObject(str);
			}
			in = ctx.getResourceAsStream("/WEB-INF/airlinecode_to_airlinename.txt");
			if(in != null)
			{
				str = IOUtils.toString( in ) ;
				mapALcode = new ObjectMapper().readValue(str, TreeMap.class);
			}
			in = ctx.getResourceAsStream("/WEB-INF/iata_to_city_US.txt");
			if(in != null)
			{
				str = IOUtils.toString( in ) ;
				mapAPcodeUS = new ObjectMapper().readValue(str, TreeMap.class);
			}
		} 
		catch (IOException ex) 
		{
			LOGGER.log(Level.WARNING,ex.getMessage());
			ex.printStackTrace();
		}
		
		
    }
    public void setupMongoDB()
    {
    	mongoClient = new MongoClient(new MongoClientURI(System.getenv("mongodb://admin:admin@mongodb/sampledb")));
		mongoDBUsers = mongoClient.getDatabase("tripusers");
    	// OPENSHIFT_MONGODB_DB_URL="mongodb://DB_USERNAME:DB_PASSWORD@LOCAL_DB_IP:LOCAL_DB_PORT"
		
		//mongoClient = new MongoClient("localhost" , 27017 );
		//mongoDBUsers = mongoClient.getDatabase("tripusers");
		
		
		// Mongo automatically delete record. Perfect for session
		try 
        {
			mongoDBSession = mongoClient.getDatabase("session");
			sessionColFly = mongoDBSession.getCollection("fly");
			sessionColHtl = mongoDBSession.getCollection("htl");	
			sessionColCar = mongoDBSession.getCollection("car");
			
   		 	com.mongodb.client.model.IndexOptions options = new com.mongodb.client.model.IndexOptions();
   		 	options.expireAfter(7200L,TimeUnit.SECONDS);
   		 	sessionColFly.createIndex(new Document("timestamp", 1),options);
   		 	sessionColHtl.createIndex(new Document("timestamp", 1),options);
   		 	sessionColCar.createIndex(new Document("timestamp", 1),options);
   		 	
   		 	// also for temporary registered users
   		 	MongoCollection<Document> colTempUser = mongoDBUsers.getCollection("tempreg");
   		 	com.mongodb.client.model.IndexOptions options2 = new com.mongodb.client.model.IndexOptions();
		 	options2.expireAfter(25200L,TimeUnit.SECONDS);	// 7 hours.
		 	colTempUser.createIndex(new Document("timestamp", 1),options2);
		 	
		 	// also for login session
   		 	MongoCollection<Document> colLoginSession = mongoDBUsers.getCollection("loginsession");
   		 	com.mongodb.client.model.IndexOptions options3 = new com.mongodb.client.model.IndexOptions();
		 	options3.expireAfter(7200L,TimeUnit.SECONDS);	
		 	colLoginSession.createIndex(new Document("timestamp", 1),options3);
        }
        catch ( Exception ex ) 
        {
            LOGGER.log(Level.INFO,ex.getMessage());
        }
    }
    public void setupNFL()
    {
    	listNFL.add("COWBOYS");
		listNFL.add("GIANTS");
		listNFL.add("EAGLES");
		listNFL.add("REDSKINS");
		listNFL.add("BEARS");
		listNFL.add("LIONS");
		listNFL.add("PACKERS");
		listNFL.add("VIKINGS");
		listNFL.add("FALCONS");
		listNFL.add("PANTHERS");
		listNFL.add("SAINTS");
		listNFL.add("BUCCANEERS");
		listNFL.add("CARDINALS");
		listNFL.add("49ERS");
		listNFL.add("SEAHAWKS");
		listNFL.add("RAMS");
		listNFL.add("BILLS");
		listNFL.add("DOLPHINS");
		listNFL.add("PATRIOTS");
		listNFL.add("JETS");
		listNFL.add("RAVENS");
		listNFL.add("BENGALS");
		listNFL.add("BROWNS");
		listNFL.add("STEELERS");
		listNFL.add("TEXANS");
		listNFL.add("COLTS");
		listNFL.add("JAGUARS");
		listNFL.add("TITANS");
		listNFL.add("BRONCOS");
		listNFL.add("CHIEFS");
		listNFL.add("RAIDERS");
		listNFL.add("CHARGERS");
    }

}