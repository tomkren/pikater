package org.pikater.core.utilities;

import java.util.Date;

public class CoreUtilities
{

	public static String getPikaterDateString(Date date) {
		return "" + date.getTime();
	}

	public static String getCurrentPikaterDateString() {
		return getPikaterDateString(new Date());
	}

	public static Date getDateFromPikaterDateString(String dateString) {
		try {
			long millis = Long.parseLong(dateString);
			return new Date(millis);
		} catch (NumberFormatException e) {
			return new Date();
		}
	}

}
