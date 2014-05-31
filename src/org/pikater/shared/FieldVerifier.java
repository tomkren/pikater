package org.pikater.shared;

public class FieldVerifier
{
	public static boolean isStringNullOrEmpty(String arg)
	{
		return (arg == null) || arg.isEmpty(); 
	}
	
	public static boolean isValidEmail(String email)
	{
		boolean result = false;
		if(!isStringNullOrEmpty(email))
		{
			String[] splitted = email.split("@");
			if(splitted.length == 2)
			{
				// max email length: 64@255
				if((splitted[0].length() <= 64) && (splitted[1].length() <= 255) && splitted[1].contains("."))
				{
					result = true;
				}
			}
		}
		return result;
	}
}
