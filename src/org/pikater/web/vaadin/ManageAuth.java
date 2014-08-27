package org.pikater.web.vaadin;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;

import com.vaadin.server.VaadinSession;

public class ManageAuth
{
	public static Integer getUserID(VaadinSession session)
	{
		if(session == null)
		{
			throw new NullPointerException("Session is null. Did you use the 'getSession()' method? If so, make sure you"
					+ "use it from the 'AbstractComponent.attach' method or just use 'VaadinSession.getCurrent()' instead.");
		}
		else
		{
			return (Integer) ManageSession.getAttribute(session, ManageSession.key_userID);
		}
	}
	
	public static JPAUser getUserEntity(VaadinSession session)
	{
		return DAOs.userDAO.getByID(getUserID(session), EmptyResultAction.LOG_NULL);
	}
	
	public static boolean isUserAuthenticated(VaadinSession session)
	{
		return getUserID(session) != null;
	}
	
	public static void login(VaadinSession session, int userID)
	{
		if(isUserAuthenticated(session))
		{
			throw new IllegalStateException("User is already authenticated in this session.");
		}
		else
		{
			ManageSession.setAttribute(session, ManageSession.key_userID, userID);
			ManageSession.setAttribute(session, ManageSession.key_userUploads, new ManageUserUploads());
		}
	}
	
	public static void logout(VaadinSession session)
	{
		if(isUserAuthenticated(session))
		{
			ManageSession.clearAttribute(session, ManageSession.key_userUploads);
			ManageSession.clearAttribute(session, ManageSession.key_userID);
		}
		else
		{
			throw new IllegalStateException("User is not authenticated.");
		}
	}
}
