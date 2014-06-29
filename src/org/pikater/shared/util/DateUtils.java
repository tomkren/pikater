package org.pikater.shared.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
	private static final SimpleDateFormat czechDateFormatter = new SimpleDateFormat("dd.MM. yyyy");
	
	public static String toCzechDate(Date date)
	{
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