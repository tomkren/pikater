package org.pikater.web.vaadin;

import java.util.HashMap;
import java.util.Map;

import org.pikater.web.vaadin.gui.server.components.upload.UserUploadsManager;

import com.vaadin.server.VaadinSession;

/**
 * Stores arbitrary data that will be bound to a user id instead of a session. This
 * is useful for having some data shared across various sessions and allows for the
 * user to log in from various machines at once, each having no effect on another.
 */
public class NoSessionStore
{
	private static final Map<Integer, NoSessionStoreCounter> userIDToDataCounter = new HashMap<Integer, NoSessionStoreCounter>();
	
	public static NoSessionStoreData getData(int userID)
	{
		return getCounter(userID).getData();
	}
	
	/**
	 * Binds the given userID to a unique instance of {@link UserUploadsManager}. If an instance already exists
	 * for the given userID, does nothing.
	 * @param userID 
	 */
	public static void bind(int userID, VaadinSession session)
	{
		if(!userIDToDataCounter.containsKey(userID))
		{
			userIDToDataCounter.put(userID, new NoSessionStoreCounter());
		}
		getCounter(userID).bindToSession(session);
	}
	
	public static void unbind(int userID, VaadinSession session)
	{
		if(getCounter(userID).unbindFromSession(session) == 0) // this was the last bound session
		{
			// discard counter and data
			userIDToDataCounter.remove(userID);
		}
	}
	
	private static NoSessionStoreCounter getCounter(int userID)
	{
		if(!userIDToDataCounter.containsKey(userID))
		{
			throw new IllegalStateException("No sessions were bound to the provided user id. You have to the 'bind' method first.");
		}
		else
		{
			return userIDToDataCounter.get(userID);
		}
	}
}
