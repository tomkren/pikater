package org.pikater.web.vaadin;

import com.vaadin.server.VaadinSession;

public class ManageSession
{
	public static final String key_userID = "k_uid";
	
	public static void setAttribute(VaadinSession session, String key, Object value)
	{
		session.setAttribute(key, value);
	}
	
	public static Object getAttribute(VaadinSession session, String key)
	{
		return session.getAttribute(key);
	}
	
	public static void clearAttribute(VaadinSession session, String key)
	{
		session.setAttribute(key, null);
	}
}
