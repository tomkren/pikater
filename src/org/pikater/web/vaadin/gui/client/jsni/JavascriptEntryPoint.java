package org.pikater.web.vaadin.gui.client.jsni;

public class JavascriptEntryPoint
{
	public static enum UnderlyingOS
	{
		WINDOWS,
		MAC_OS,
		UNIX,
		LINUX,
		UNKNOWN;
	}
	
	public final static native void initializeComparisonTab(String title)
	/*-{
		window.myChild = window.open(null, "_blank");
		window.myChild.document.title = title;
	}-*/;
	
	public final static native void updateComparisonTab(String content)
	/*-{
		window.myChild.document.body.innerHTML = content;
		window.myChild.focus();
	}-*/;
	
	private final static native int getOS()
	/*-{
		return OSName;
	}-*/;
	
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
}
