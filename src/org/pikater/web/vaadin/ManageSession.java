package org.pikater.web.vaadin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.vaadin.server.VaadinSession;

public class ManageSession
{
	public static final String key_userID = "k_uid";
	public static final String key_userUploads = "k_uu";
	public static final String key_sharedResources = "k_sr";
	
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
	
	public static void initSessionStore(VaadinSession session)
	{
		session.setAttribute(key_sharedResources, new HashSet<UUID>());
	}
	
	public static void rememberSharedResource(VaadinSession session, UUID resourceID)
	{
		Set<UUID> sharedResources = getSharedResources(session);
		if(sharedResources.add(resourceID))
		{
			session.setAttribute(key_sharedResources, sharedResources);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Set<UUID> getSharedResources(VaadinSession session)
	{
		return (Set<UUID>) session.getAttribute(key_sharedResources);
	}
}