package com.tripolay.homerest.user;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;

public class UserSession 
{
	MongoCollection<Document> colFly;
	MongoCollection<Document> colHtl;
	MongoCollection<Document> colCar;
	
	final static Logger LOGGER = Logger.getLogger(UserSession.class.getName()); 
	
	public UserSession(MongoCollection<Document> colFly,MongoCollection<Document> colHtl,MongoCollection<Document> colCar)
	{
		this.colFly = colFly;
		this.colHtl = colHtl;	
		this.colCar = colCar;	
	}
	
	public Map<Integer,Double> getFlyMap(String id) { return getmap(id,colFly);}
    public Map<Integer,Double> getHtlMap(String id) { return getmap(id,colHtl);}
    public Map<Integer,Double> getCarMap(String id) { return getmap(id,colCar);}
	
    public void insertFlyPrice(String sessionId, Map<String,Object> map)
	{
    	if(ExistsFly(sessionId))
    		update(sessionId,map,colFly);
    	else
    		insert(sessionId,map,colFly);
	}
    public void insertHtlPrice(String id, Map<String,Object> map)
	{
    	if(ExistsHtl(id))
    		update(id,map,colHtl);
    	else
    		insert(id,map,colHtl);
	}
    public void insertCarPrice(String id, Map<String,Object> map)
	{
    	if(ExistsCar(id))
    		update(id,map,colCar);
    	else
    		insert(id,map,colCar);
	}
	private void update(String id, Map<String,Object> map, MongoCollection<Document> col)
	{
		Document doc = new Document();
		doc.put("map", map);
		doc.put("timestamp",  Calendar.getInstance().getTime());
		UpdateResult res = col.updateOne(new Document("id", id),new Document("$set", doc));
		
	}
	private void insert(String id, Map<String,Object> map, MongoCollection<Document> col)
	{
		Document doc = new Document();
		doc.put("id", id);
		doc.put("map", map);
		doc.put("timestamp", Calendar.getInstance().getTime());
		col.insertOne(doc);
	}
	private boolean Exists(String id, MongoCollection<Document> col)
	{
		FindIterable<Document> it = col.find(new Document("id", id));
		if(it.iterator().hasNext())
			return true;
		else
			return false;
	}
	private boolean ExistsFly(String id) { return Exists(id,colFly);}
    private boolean ExistsHtl(String id) { return Exists(id,colHtl);}
    private boolean ExistsCar(String id) { return Exists(id,colCar);}
    
    
    private Map<Integer,Double> getmap(String id,MongoCollection<Document> col)
    {
    	Map<Integer,Double> mapGSON = new HashMap<Integer,Double>();
		try
		{
			FindIterable<Document> it = col.find(new Document("id",id));
			try (MongoCursor<Document> cursor = it.iterator()) 
			{
				if(cursor.hasNext()) 
				{
					Document docmap = (Document) cursor.next().get("map");
					mapGSON = new Gson().fromJson(docmap.toJson(), new TypeToken<HashMap<Integer, Double>>(){}.getType());
				}
			}
		}
		catch(Exception ex) { ex.printStackTrace();}
		return mapGSON;
    }
	

}
