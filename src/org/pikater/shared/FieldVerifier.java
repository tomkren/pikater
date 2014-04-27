package org.pikater.shared;

public class FieldVerifier
{
	public static boolean isStringNullOrEmpty(String arg)
	{
		return (arg == null) || arg.isEmpty(); 
	}
}
