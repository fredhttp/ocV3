package com.tripolay.homerest.app;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



@WebListener
public class Config implements ServletContextListener
{
	
	static String fileAreacode 	= "config/areacode_to_city.txt";
	static String fileIataCity 	= "config/iata_to_city.txt";
	static String fileState 	= "config/state_to_city.txt";
	static String fileNfl  		= "config/nfl_to_city.txt"; 
	static String fileLatLon	= "config/iata_lat_lon_major_airport_US.txt"; 
    
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
    	LOGGER.log(Level.INFO,"homerest closing ....");     
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
		try 
		{
			File file1 = new File(System.getenv("OPENSHIFT_DATA_DIR")  + fileAreacode);
			File file2 = new File(System.getenv("OPENSHIFT_DATA_DIR")  + fileState);
			File file3 = new File(System.getenv("OPENSHIFT_DATA_DIR")  + fileNfl);
			File file4 = new File(System.getenv("OPENSHIFT_DATA_DIR")  + fileIataCity);
			File file5 = new File(System.getenv("OPENSHIFT_DATA_DIR")  + fileLatLon);
			
			String str = FileUtils.readFileToString(file1);
			objAreaCode = new JSONObject(str);
			
			str = FileUtils.readFileToString(file2);
			objState = new JSONObject(str);
			
			str = FileUtils.readFileToString(file3);
			objNfl = new JSONObject(str);
			
			str = FileUtils.readFileToString(file4);
			objIata = new JSONObject(str);
			
			str = FileUtils.readFileToString(file5);
			objLatLon = new JSONObject(str);
			
		} 
		catch (IOException ex) 
		{
			LOGGER.log(Level.WARNING,ex.getMessage());
			ex.printStackTrace();
		}
	
    }
    public void setupMongoDB()
    {
    	mongoClient = new MongoClient(new MongoClientURI(System.getenv("OPENSHIFT_MONGODB_DB_URL")));
		mongoDBUsers = mongoClient.getDatabase("tripusers");
		
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