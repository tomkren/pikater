package org.pikater.shared.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class providing locale-related date formatting.
 * 
 * @author SkyCrawl
 */
public class DateUtils
{
	/**
	 * Returns a string representation of the given date, in the following
	 * format: "dd.MM. yyyy".
	 * 
	 * @param date
	 * @return
	 */
	public static String toCzechDate(Date date)
	{
		SimpleDateFormat czechDateFormatter = new SimpleDateFormat("dd.MM. yyyy");
	
		if(date == null)
		{
			return "";
		}
		else
		{
			return czechDateFormatter.format(date);
		}
	}
}