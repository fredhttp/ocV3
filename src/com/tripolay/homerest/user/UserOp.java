package com.tripolay.homerest.user;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class UserOp 
{
	
	MongoCollection<Document> colUser;
	MongoCollection<Document> colTempUser;
	MongoCollection<Document> colLoginSession;
	
	final static Logger LOGGER = Logger.getLogger(UserOp.class.getName()); 
	
	public UserOp(MongoDatabase dbUser)
	{
		this.colUser = dbUser.getCollection("users");
		this.colTempUser = dbUser.getCollection("tempreg");	
		this.colLoginSession = dbUser.getCollection("loginsession");
	}
	
	public boolean tempUserNew(String email, String regcode)
	{
		boolean retval = false;
		if(userIDExists(colTempUser,email))
		{
			LOGGER.log(Level.INFO,"temp user already exists..");
			
			tempUserUpdate(email,regcode);
		}
		else {
			Document root = new Document();
			root.put("email", email);
			root.put("regcode", regcode);
			root.put("timestamp", Calendar.getInstance().getTime());
			colTempUser.insertOne(root);
			System.out.println("email: " + email + "  with reg code: " + regcode + " added to temp reg.");
			retval = true;
		}
		return retval;
	}
	public boolean tempUserUpdate(String email, String regcode)
	{
		Document doc = new Document();
		doc.put("regcode", regcode);
		doc.put("timestamp",  Calendar.getInstance().getTime());
		UpdateResult res = colTempUser.updateOne(new Document("email", email),new Document("$set", doc));
		
		if(res.getMatchedCount() > 0)
			return true;
		else
			return false;
	}
	
	public boolean tempUserExists(String email)
	{
		return userIDExists(colTempUser,email);
	}
	public String tempUserEmail(String regcode)
	{
		String email = "";
		FindIterable<Document> it = colTempUser.find(new Document("regcode", regcode));
		try (MongoCursor<Document> cursor = it.iterator()) 
		{
			if (cursor.hasNext()) {
				Document doc = cursor.next();
				email = (String) doc.get("email");
			}
		}
		return email;
	}
	
	// User OPS
	public boolean addLoginSession(String userid, String sessionid)
	{
		Document doc = new Document();
		doc.put("userid", userid);
		doc.put("sessionid", sessionid);
		doc.put("count", 1);
		doc.put("timestamp", Calendar.getInstance().getTime());
		colLoginSession.insertOne(doc);
		LOGGER.log(Level.INFO,"login session set: " + sessionid );
		return false;
	}
	public boolean hasLoginSession(String id, boolean session)
	{
		FindIterable<Document> it;
		if(session)
			it = colLoginSession.find(new Document("sessionid", id));
		else
			it = colLoginSession.find(new Document("userid", id));
		
		return it.iterator().hasNext();
	}
	// called during registration
	public boolean userExists(String strUserID)
	{
		return userIDExists(colUser,strUserID);
	}
	private boolean userIDExists(MongoCollection<Document> col, String strUserID)
	{
		FindIterable<Document> it = col.find(new Document("userid", strUserID));
		return it.iterator().hasNext();
	}
	// user registered ?
	public boolean hasLogin(User user)
	{
		String userid = user.getID();
		String passwd = user.getPasswd();
		FindIterable<Document> it = colUser.find(and(eq("userid", userid), eq("passwd", passwd)));
		boolean ret = it.iterator().hasNext();
		return ret;
	}
	public boolean signIn(User user,String sessionid)
	{
		boolean ret = false;
		String userid = user.getID();
		String passwd = user.getPasswd();
		
		if(hasLoginSession(userid,false))
		{
			// update all sessions for this user
			colLoginSession.updateMany(new Document("userid", userid),new Document("$inc", new Document("count", 1)));
			FindIterable<Document> it2 = colLoginSession.find(new Document("userid", userid));
			try (MongoCursor<Document> cursor = it2.iterator() )
			{
				while(cursor.hasNext()) 
				{
					Document d = cursor.next();
					int count = (int) d.get("count");
					if(count > 10 )
					{
						LOGGER.log(Level.WARNING, "exceded session count for: "  + userid);
						return ret;
					}
					LOGGER.log(Level.INFO, "session count for: "  + userid + " is: " + count);
				}
			}
		}
		
		
		FindIterable<Document> it = colUser.find(and(eq("userid", userid), eq("passwd", passwd)));
		if(it.iterator().hasNext())
		{
			updateVisits(userid);
			addLoginSession(userid,sessionid);
			ret = true;
		}
		return ret;
	}
	public boolean signOut(String sessionid)
	{
		boolean ret = false;
		
		FindIterable<Document> it = colLoginSession.find(new Document("sessionid", sessionid));
		if(it.iterator().hasNext())
		{
			colLoginSession.deleteOne(new Document("sessionid",sessionid));
			ret = true;
		}
		return ret;
	}
	private void updateVisits(String strUserID)
	{
		// changed $currentDate ( introduced in 3.0)  with $set for 2.4
		colUser.updateOne(new Document("userid", strUserID),
				new Document("$inc", new Document("visits", 1))
					.append("$set", new Document("lastlogin", Calendar.getInstance().getTime())));
	}
	public boolean addUser(User user)
	{
		if(userIDExists(colUser,user.getID()))
		{
			System.out.println("user already exists..");
			return false;
		}
		Document root = new Document();
		root.put("userid", user.getID());
		root.put("passwd", user.getPasswd());
		root.put("fname", user.getFirstName());
		root.put("lname", user.getLastName());
		root.put("phone", user.getPhone());
		root.put("lastlogin", Calendar.getInstance().getTime());
		root.put("visits", 1);
		
		Document doc = new Document();
		List<Document> list = new ArrayList<Document>();
		list.add(air());
		doc.append("air", list);
		
		list = new ArrayList<Document>();
		list.add(htl());
		doc.append("htl", list);
		
		list = new ArrayList<Document>();
		list.add(car());
		doc.append("car", list);
		
		root.append("itin", doc);
		
		String phone = "";	// alt phone off reg form
		String email = "";	// alt email off reg form
		List<String> listphone = user.getContact().getPhone();
		List<String> listemail = user.getContact().getEmail();
		if(listphone.size() > 0)
			phone = listphone.get(0);
		if(listemail.size() > 0)
			email = listemail.get(0);
		
		root.append("contact", contact(phone,email,"",""));
		root.append("payment", payment());
		root.append("address", address(user.getAddress().getNumber(),user.getAddress().getStreet(),
										user.getAddress().getCity(),user.getAddress().getState(),
										user.getAddress().getZip(),user.getAddress().getCountry()));
		
		colUser.insertOne(root);
		System.out.println("userid: " + user.getID()+ "  with passwd: " + user.getPasswd() + " added.");
		return true;
	}
	
	
	// Adding itineraries for user with upper bound logic
	public boolean addItinAir(FindIterable<Document> it, User user, UserItinAir itin)
	{
		int count = countItin(it, "air");
		System.out.println("air itinairy count is: " + count);
		if ( count > 100 )
			System.out.println("air itin > 100 records. to do: apply some logic for upper bound " );
		
		Document doc = air(itin.getSrc(),itin.getDest(),itin.getAirline(),itin.getCabin(),itin.getPrice(),itin.getStops(),itin.getDate());
		UpdateResult res = colUser.updateOne(new Document("userid", user.getID()),
				new Document("$push", new Document("itin.air", doc)));
		System.out.println("update result matched: " + res.getMatchedCount() + " modified: " + res.getModifiedCount());
		if(res.getMatchedCount() > 0)
			return true;
		else
			return false;
	}
	public boolean addItinCar(FindIterable<Document> it, User user,UserItinCar itin)
	{
		int count = countItin(it, "car");
		System.out.println("car itinairy count is: " + count);
		if ( count > 100 )
			System.out.println("car itin > 100 records. to do: apply some logic for upper bound " );
		
		String strAddress = getAddress(itin.getAddress());					
		Document doc = car(itin.getCity(),itin.getName(),strAddress, itin.getDailyRate(),itin.getTotalPrice(),
				itin.getPickupDate(),itin.getDropoffDate(),itin.getModel(),itin.getMake());
		UpdateResult res = colUser.updateOne(new Document("userid", user.getID()),
				new Document("$push", new Document("itin.car", doc)));
		System.out.println("update result matched: " + res.getMatchedCount() + " modified: " + res.getModifiedCount());
		if(res.getMatchedCount() > 0)
			return true;
		else
			return false;
	}
	public boolean addItinHotel(FindIterable<Document> it, User user,UserItinHotel itin)
	{
		int count = countItin(it, "htl");
		System.out.println("htl itinairy count is: " + count);
		if ( count > 100 )
			System.out.println("htl itin > 100 records. to do: apply some logic for upper bound " );
		
		String strAddress = getAddress(itin.getAddress());	
		Document doc = htl(itin.getCity(),itin.getName(),strAddress,itin.getNights(),itin.getDateIN(),itin.getDateOUT(),
						itin.getDailyRate(),itin.getTotalPrice());
		UpdateResult res = colUser.updateOne(new Document("userid", user.getID()),
				new Document("$push", new Document("itin.htl", doc)));
		System.out.println("update result matched: " + res.getMatchedCount() + " modified: " + res.getModifiedCount());
		if(res.getMatchedCount() > 0)
			return true;
		else
			return false;
	}
	// Adding contact and other info for user. To be completed...
	public void addContactEmail(FindIterable<Document> it, User user)
	{
		int count = countContact(it, "email");
		System.out.println("contact email count is: " + count);
		if ( count > 5 )
			System.out.println("contact email > 5 records. to do: apply some logic for upper bound " );
		
		String userID = user.getID();
		UserContact contact = user.getContact();
		if(contact != null)
		{
			List<String> listemail = user.getContact().getEmail();
			if(listemail.size() > 0)
			{
				String email = listemail.get(0);
				UpdateResult res = colUser.updateOne(new Document("userid", userID),
					new Document("$push", new Document("contact.email", email)));
				System.out.println("update result matched: " + res.getMatchedCount() + " modified: " + res.getModifiedCount());
			}
		}
	}
	public void addContactPhone(FindIterable<Document> it, User user)
	{
		int count = countContact(it, "phone");
		System.out.println("contact phone count is: " + count);
		if ( count > 5 )
			System.out.println("contact phone > 5 records. to do: apply some logic for upper bound " );
		
		String userID = user.getID();
		UserContact contact = user.getContact();
		if(contact != null)
		{
			List<String> listphone = user.getContact().getPhone();
			if(listphone.size() > 0)
			{
				String phone = listphone.get(0);
				UpdateResult res = colUser.updateOne(new Document("userid", userID),
					new Document("$push", new Document("contact.phone", phone)));
				System.out.println("update result matched: " + res.getMatchedCount() + " modified: " + res.getModifiedCount());
			}
		}
	}
	public boolean addContact(User user,UserContact contact)
	{
		return false;
	}
	public boolean addAddress(User user,Address address)
	{
		return false;
	}
	public boolean addPayment(User user, UserPayment payment)
	{
		return false;
	}
	private int countContact(FindIterable<Document> it, String type)
	{
		List<String> list = (List<String>) ( (Document)((Document)it.iterator().next()).get("contact")).get(type);
		if(list != null)
			return list.size();
		else 
			return 0;
	}
	private int countItin(FindIterable<Document> it, String type)
	{
		List<Document> list =  (List<Document>) ((Document) ((Document)it.iterator().next()).get("itin") ).get(type) ;
		if(list != null)
			return list.size();
		else 
			return 0;
	}
	private String getAddress(Address ad)
	{
		return ad.getNumber() + " ," + ad.getStreet() + " ," + ad.getCity() + " ," + ad.getZip() + " ," + ad.getState() + " ," + ad.getCountry();
	}
	
	
	// Following creates the structure 
	private Document payment(String strTotal, String strLastPay, String strMethod)
	{
		Document root = new Document();
		root.put("total", strTotal);
		root.put("lastpay", strLastPay);
		root.put("method", asList(strMethod));
		return root;
	}
	private Document payment() 
	{ 
		return payment("","","");
	}
	private Document contact(String strPhone,String strEmail, String strTwitter, String strFB)
	{
		Document root = new Document();
		root.put("phone", asList(strPhone));
		root.put("email", asList(strEmail));
		root.put("twitter", asList(strTwitter));
		root.put("FB", asList(strFB));
		return root;
	}
	private Document contact()
	{
		return contact("","","","");
	}
	private Document address(String strNum, String strStreet, String strCity, String strZip, String strState,String strCountry)
	{
		Document root = new Document();
		root.put("number", strNum);
		root.put("street", strStreet);
		root.put("city", strCity);
		root.put("zip", strZip);
		root.put("state", strState);
		root.put("country", strCountry);
		return root;
	}
	private Document address()
	{
		return address("","","","","","");
	}
	private Document air(String strSrc, String strDst, String strAirline, String strCabin, String strPrice, List<String> liststops, String strDate)
	{
		Document doc = new Document();
		doc.put("src", strSrc);
		doc.put("dst", strDst);
		doc.put("airline", strAirline);
		doc.put("cabin", strCabin);
		doc.put("price", strPrice);
		doc.put("stops", liststops); 
		doc.put("date", strDate);	
		return doc;
	}
	private Document air()
	{
		return air("","","","","",new ArrayList<String>(),""); 
	}
	private Document htl(String strCity, String strName, String strAddress, String strNights, String strDatein, String strDateout,String strDailyRate, String strTotalPrice)
	{
		Document doc = new Document();
		doc.put("city", strCity);
		doc.put("name", strName);
		doc.put("address", strAddress);
		doc.put("nights", strNights);
		doc.put("datein", strDatein);
		doc.put("dateout", strDateout);
		doc.put("dailyrate",strDailyRate);
		doc.put("totalprice", strTotalPrice);
		return doc;
	}
	private Document htl()
	{
		return htl("","","","","","","","");
	}
	private Document car(String strCity, String strName, String strAddress, String strDailyRate, 
			String strTotalPrice, String strPickupdate, String strDropoffdate, String strModel, String strMake)
	{
		Document doc = new Document();
		doc.put("city", strCity);
		doc.put("name", strName);
		doc.put("address", strAddress);
		doc.put("dailyrate", strDailyRate);
		doc.put("totalprice", strTotalPrice);
		doc.put("pickupdate", strPickupdate);
		doc.put("dropoffdate", strDropoffdate);
		doc.put("model", strModel);
		doc.put("make", strMake);
		return doc;
	}
	private Document car()
	{
		return car("","","","","","","","","");
	}

}





