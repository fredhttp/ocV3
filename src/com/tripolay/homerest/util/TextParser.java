package com.tripolay.homerest.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.tripolay.homerest.app.Config;
import com.tripolay.homerest.parm.ParmCity;


public class TextParser 
{
	final static Logger LOGGER = Logger.getLogger(TextParser.class.getName()); 
	
	Pattern pt1 = Pattern.compile("\\d{3}"); 																						// single area code
	Pattern pt2 = Pattern.compile("(\\d{3})(?:[\\s]{0,10}[-]?(?:to)?[\\s]{0,10})(\\d{3})",Pattern.CASE_INSENSITIVE);				// 2 area codes
	Pattern pt3 = Pattern.compile("([a-zA-Z]{3})");																					// single IATA
	Pattern pt4 = Pattern.compile("([a-zA-Z]{3}?[\\s-])(?:[\\s]{0,10}[-]?(?:to)?[\\s]{0,10})([a-zA-Z]{3})",Pattern.CASE_INSENSITIVE);		// 2 IATA
	Pattern pt5 = Pattern.compile("^([a-zA-Z]+)$"); 																				// 1 word no space
	Pattern pt6 = Pattern.compile("^([a-zA-Z]{0,4}(?:[\\s][a-zA-Z]+))$");																// 1 word with space
	Pattern pt7 = Pattern.compile("^([a-zA-Z]+)(?:[\\s]{0,10}[-]?(?:to)?[\\s]{0,10})([a-zA-Z]+)$",Pattern.CASE_INSENSITIVE);   		// 2 words no space in words
	Pattern pt8 = Pattern.compile("^([a-zA-Z]+(?:[\\s][a-zA-Z]+))([\\s]{0,10}[-]?(?:to)?[\\s]{0,10})([a-zA-Z]+)$",Pattern.CASE_INSENSITIVE);  // 2 words first with space
	Pattern pt9 = Pattern.compile("^([a-zA-Z]+)([\\s]{0,10}[-]?(?:to)?[\\s]{0,10})([a-zA-Z]+(?:[\\s+][a-zA-Z]+))$",Pattern.CASE_INSENSITIVE);	// 2 words second with space				
	Pattern pt10 = Pattern.compile("^([a-zA-Z]+(?:[\\s+][a-zA-Z]+))([\\s]{0,10}[-]?(?:to)?[\\s]{0,10})([a-zA-Z]+(?:[\\s+][a-zA-Z]+))$",Pattern.CASE_INSENSITIVE);	// 2 words both with space in them
	Pattern pt11 = Pattern.compile("([a-zA-Z]+)?(?:[\\s]{0,10}[-]?(?:to)?[\\s]{0,10})[\\s]{0,5}(\\b49'?ers\\b)(?:[\\s]{0,10}[-]?(?:to)?[\\s]{0,10})([a-zA-Z]+)?", Pattern.CASE_INSENSITIVE); 	 	
	
	
	Config config;
	 
	public TextParser(Config config)
	{
		this.config = config;
	}
		
	public ParmCity findMatch(String text)
	{
		ParmCity PC = new ParmCity();
		try
		{
			if(text.contains(","))
			{
				String[] tokens = text.split(",");  // for auto complete
				if( tokens[0].length() < 15)
					text = tokens[0];
				else
				{
					PC.setError(true);
					PC.setErrorStr("input len > 15");
					return PC;
				}
			}
			
			text = text.trim();
			
			Matcher mat1 = pt1.matcher(text);
			Matcher mat2 = pt2.matcher(text);
			Matcher mat3 = pt3.matcher(text);
			Matcher mat4 = pt4.matcher(text);
			Matcher mat5 = pt5.matcher(text);
			Matcher mat6 = pt6.matcher(text);
			Matcher mat7 = pt7.matcher(text);
			Matcher mat8 = pt8.matcher(text);
			Matcher mat9 = pt9.matcher(text);
			Matcher mat10 = pt10.matcher(text);
			Matcher mat11 = pt11.matcher(text);
			
			if(mat1.matches())
			{
				PC = areacode2City(mat1,false);
				LOGGER.log(Level.INFO,"mat1 matched: " + text);
			}
			else if(mat2.matches())
			{
				PC = areacode2City(mat2,true);
				LOGGER.log(Level.INFO,"mat2 matched: " + text);
			}
			else if(mat3.matches())
			{
				PC = iata2City(mat3,false);
				LOGGER.log(Level.INFO,"mat3 matched: " + text);
			}
			else if(mat4.matches())
			{
				PC = iata2City(mat4,true);
				LOGGER.log(Level.INFO,"mat4 matched: " + text);
			}
			else if(mat5.matches())
			{
				PC = oneWordNoSpace(mat5); 
				LOGGER.log(Level.INFO,"mat5 matched: " + text);
			}
			else if(mat6.matches())
			{
				PC = oneWordSpace(mat6); 
				LOGGER.log(Level.INFO,"mat6 matched: " + text);
			}
			else if(mat7.matches())
			{
				PC = twoWordNoSpace(mat7); 
				LOGGER.log(Level.INFO,"mat 7 matched: " + text);	
			}
			else if(mat8.matches())
			{
				PC = twoWordFirstWithSpace(mat8); 
				LOGGER.log(Level.INFO,"mat 8 matched: " + text);	
			}
			else if(mat9.matches())
			{
				PC = twoWordSecondWithSpace(mat9); 
				LOGGER.log(Level.INFO,"mat 9 matched: " + text);
			}
			else if(mat10.matches())
			{
				PC = twoWordBothWithSpace(mat10); 
				LOGGER.log(Level.INFO,"mat 10 matched: " + text);
			}
			else if (mat11.matches())
			{
				PC = nfl49er(mat11); 
				LOGGER.log(Level.INFO,"mat 11 matched: " + text);
			}
			else
			{
				PC.setError(true);
				PC.setErrorStr("non matched: " + text);
				LOGGER.log(Level.INFO,"non matched: " + text);
			}
		}
		catch(Exception ex)
		{
			PC.setError(true);
			PC.setErrorStr(ex.getMessage());
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return PC;
	}
	
	
	
	private ParmCity areacode2City(Matcher matcher, boolean bsrc)
	{
		ParmCity PC = new ParmCity();
		String src = "";
		String dst = "";
		String areaCode_1 = "";
		String areaCode_2 = "";
		if(bsrc)
		{
			areaCode_1 = matcher.group(1);
			areaCode_2 = matcher.group(2);
			src = areacode2City(areaCode_1);
		    dst = areacode2City(areaCode_2);
			if(src != null && dst != null)
			{
				PC.setDeptCity(src);
				PC.setArrvCity(dst);
			}
			else
			{
				PC.setError(true);
				PC.setErrorStr("1 or both area codes did not match: " + areaCode_1 + "  "  + areaCode_2 );
			}
		}
		else
		{
			areaCode_1 = matcher.group();
			dst = areacode2City(areaCode_1);
			if(dst != null)
			{
				PC.setArrvCity(dst);
				PC.setOneParm(true);
			}
			else
			{
				PC.setError(true);
				PC.setErrorStr("No match for this area code: " + areaCode_1);
			}
		}
		return PC;
	}
	private String areacode2City(String areacode)
	{
		String str = null;
		if(config.objAreaCode.has(areacode))
		{
			JSONObject obj = (JSONObject) config.objAreaCode.get(areacode);
			str =  obj.get("majorCity").toString();
		}
		return str;
	}
	private ParmCity iata2City(Matcher mat, boolean bsrc)
	{
		ParmCity PC = new ParmCity();
		String src = "";
		String dst = "";
		String IATA_1 = "";
		String IATA_2 = "";
		if(bsrc)
		{
			IATA_1 = mat.group(1);
			src = iata2City(IATA_1);
			if(mat.groupCount() == 3)
			{
				dst = iata2City(mat.group(3));
			}
			else
			{
				IATA_2 = mat.group(2);
				dst = iata2City(IATA_2);
			}
			if(src != null && dst != null)
			{
				PC.setDeptCity(src);
				PC.setArrvCity(dst);
			}
			else
			{
				PC.setError(true);
				PC.setErrorStr("1 or both IATA codes did not match: " + IATA_1 + "  " + IATA_2);
				LOGGER.log(Level.INFO,"1 or both IATA codes did not match: " + IATA_1 + "  " + IATA_2);
			}
		}
		else 
		{
			IATA_1 = mat.group();
			LOGGER.log(Level.INFO,"before calling iata2City: " + IATA_1);
			dst = iata2City(IATA_1);
			if(dst != null)
			{
				PC.setArrvCity(dst);
				PC.setOneParm(true);
			}
			else
			{
				PC.setError(true);
				PC.setErrorStr("No match for this IATA code: " + IATA_1);
				LOGGER.log(Level.INFO,"No match for this IATA code: " + IATA_1);
			}
		}
		return PC;
	}
	private String iata2City(String iata)
	{
		String str = null;
		iata = iata.toUpperCase().trim();
		try
		{
			if(config.objIata.has(iata))
			{
				JSONObject obj = (JSONObject) config.objIata.getJSONObject(iata);
				str = obj.get("majorCity").toString();
			}
		}
		catch(Exception ex)
		{
			LOGGER.log(Level.WARNING,ex.getMessage());
		}
		return str;
	}
	private ParmCity oneWordNoSpace(Matcher mat)
	{
		ParmCity PC = new ParmCity();
		String dst = mat.group(1);
		
		String dst2 = nfl2City(dst);
		if ( dst2 != null)
			PC.setArrvCity(dst2);
		else
			PC.setArrvCity(dst);
		
		PC.setOneParm(true);
		return PC;
	}
	private ParmCity oneWordSpace(Matcher mat)
	{
		ParmCity PC = new ParmCity();
	
		String dst = mat.group(1);
		PC.setArrvCity(dst);
		PC.setOneParm(true);
		return PC;
	}
	private ParmCity twoWordNoSpace(Matcher mat)
	{
		ParmCity PC = new ParmCity();
		String src = mat.group(1);
		String dst = mat.group(2);
		
		String src2 = nfl2City(src);
		String dst2 = nfl2City(dst);
		
		if(src2 != null && dst2 != null)
		{
			PC.setDeptCity(src2);
			PC.setArrvCity(dst2);
		}
		else
		{
			PC.setDeptCity(src);
			PC.setArrvCity(dst);	
		}
		return PC;
	}
	private ParmCity twoWordFirstWithSpace(Matcher mat)
	{
		ParmCity PC = new ParmCity();
		String src = "";
		String dst = "";
		if(mat.groupCount() == 3)
		{
			src = mat.group(1);
			dst = mat.group(3);
			PC.setDeptCity(src);
			PC.setArrvCity(dst);
		}
		return PC;
	}
	private ParmCity twoWordSecondWithSpace(Matcher mat)
	{
		ParmCity PC = new ParmCity();
		String src = "";
		String dst = "";
		if(mat.groupCount() == 3)
		{
			src = mat.group(1);
			dst = mat.group(3);
			PC.setDeptCity(src);
			PC.setArrvCity(dst);
		}
		return PC;
	}
	private ParmCity twoWordBothWithSpace(Matcher mat)
	{
		ParmCity PC = new ParmCity();
		String src = "";
		String dst = "";
		if(mat.groupCount() == 3)
		{
			src = mat.group(1);
			dst = mat.group(3);
			PC.setDeptCity(src);
			PC.setArrvCity(dst);
		}
		return PC;
	}
	private List<String> word2City(Matcher mat)
	{
		List<String> list = new ArrayList<String>();
		String src = mat.group(1);
		String dst = "";
		if(mat.groupCount() == 3)
		{
			dst = mat.group(3);
		}
		else
			dst = mat.group(2);
		LOGGER.log(Level.INFO,"in word2city src: " + src + " dst: " + dst);
		list.add(src);
		list.add(dst);
		return list;
	}
	private String nfl2City(String team)
	{
		team = team.toUpperCase().trim();
		String str = null;
		if(config.objNfl.has(team))
		{
			JSONObject obj = (JSONObject) config.objNfl.get(team);
			str = obj.get("majorCity").toString();
		}
		return str;
	}
	private ParmCity nfl49er(Matcher mat)
	{
		String str49 = "";
		String src = null;
		String dst = null;
		ParmCity PC = new ParmCity();
		if(mat.group(2).contains("'"))
		{
			int index = mat.group(2).indexOf("'");
			StringBuilder sb = new StringBuilder(mat.group(2));
			sb.deleteCharAt(index);
			str49 = sb.toString();
		}
		else
			str49 = mat.group(2);
		if(mat.group(1) != null)
		{
			src = nfl2City(mat.group(1));		// cowboys 49ers
			dst = nfl2City(str49);
		}
		else if (mat.group(3) != null)
		{
			src = nfl2City(str49);				// 49ers cowboys
			dst = nfl2City(mat.group(3));
			PC.setDeptCity(str49);
			PC.setArrvCity(mat.group(3));
		}
		else 
		{
			dst = nfl2City(str49);				// 49ers
		}
		if(src != null && dst != null)
		{
			PC.setDeptCity(src);				// cowboys 49ers  or  49ers cowboys
			PC.setArrvCity(dst);
		}
		else if (src == null)
		{
			PC.setArrvCity(dst);		// 49ers
			PC.setOneParm(true);
		}
		return PC;
		
	}
	
	
/////////// to get city from lat , lon
	
	public String getCity(double aLat, double aLon)
	{
		String city = "";
		String iata = "";
		double min = 10000000.0;
		try {
	  	
			JSONObject root = config.objLatLon;
	
			Iterator<?> keys = root.keys();
			while( keys.hasNext() )
			{
				String key = (String)keys.next();
				JSONObject obj = root.getJSONObject(key);
	
		          String lat = obj.getString("lat");
		          String lon = obj.getString("lon");
		          double lat2 = Double.valueOf(lat).doubleValue();
		          double lon2 = Double.valueOf(lon).doubleValue();
		          double curr = distance(aLat,aLon,lat2,lon2);
		          if(curr < min)
		          {
		              min = curr;
		              city = obj.getString("majorCity");
		              iata = key;
		          }
			}
		}
		catch (JSONException ex)
		{
			ex.printStackTrace();
		}
		return city;
	}

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  return (dist);
	}
	private double deg2rad(double deg) { return (deg * Math.PI / 180.0); }
	private double rad2deg(double rad) { return (rad * 180.0 / Math.PI);}
	

}
