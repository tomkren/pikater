package org.pikater.shared;

import java.text.NumberFormat;
import java.util.Locale;

public class LocaleUtils
{
	public static String formatInteger(Locale locale,int value)
	{
		NumberFormat numberFormat=NumberFormat.getInstance(locale);
		return numberFormat.format(value);
	}
	
	public static String formatDouble(Locale locale,double value)
	{
		NumberFormat numberFormat=NumberFormat.getInstance(locale);
		return numberFormat.format(value);
	}
	
	public static String formatBool(Locale locale,boolean value)
	{
		return ""+value;
	}
	
	public static Locale getDefaultLocale()
	{
		return new Locale.Builder()
				.setLanguage("en")
				.setRegion("US")
				.build();
	}
}