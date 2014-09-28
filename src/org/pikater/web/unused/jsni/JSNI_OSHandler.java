package org.pikater.web.unused.jsni;

public class JSNI_OSHandler
{
	public static enum UnderlyingOS
	{
		WINDOWS,
		MAC_OS,
		UNIX,
		LINUX,
		UNKNOWN;
	}
	
	public static UnderlyingOS getUnderlyingOS()
	{
		switch (getOS())
		{
			case 0:
				return UnderlyingOS.WINDOWS;
			case 1:
				return UnderlyingOS.MAC_OS;
			case 2:
				return UnderlyingOS.UNIX;
			case 3:
				return UnderlyingOS.LINUX;
			default:
				return UnderlyingOS.UNKNOWN;
		}
	}
	
	// ---------------------------------------------------------
	// PRIVATE METHODS
	
	private static native int getOS()
	/*-{
		return OSName;
	}-*/;
}
