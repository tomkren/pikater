package org.pikater.shared.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{
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