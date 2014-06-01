package org.pikater.web.vaadin;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.server.VaadinSession;

public class NoSessionStoreCounter
{
	private final NoSessionStoreData data;
	private final Set<VaadinSession> sessionCounter;
	
	public NoSessionStoreCounter()
	{
		this.data = new NoSessionStoreData();
		this.sessionCounter = new HashSet<VaadinSession>();
	}

	public NoSessionStoreData getData()
	{
		return data;
	}

	public void bindToSession(VaadinSession session)
	{
		sessionCounter.add(session);
	}
	
	public int unbindFromSession(VaadinSession session)
	{
		sessionCounter.remove(session);
		return sessionCounter.size(); 
	}
}
