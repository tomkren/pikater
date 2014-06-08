package org.pikater.web.vaadin;

import java.util.List;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.daos.AbstractDAO.EmptyResultAction;
import org.pikater.web.config.ServerConfigurationInterface;

import com.vaadin.server.VaadinSession;

public class ManageAuth
{
	/**
	 * Tries to authenticate the user. If the provided auth info is correct, automatically
	 * authenticates the user in this UI.
	 * @param session
	 * @param login
	 * @param password
	 * @return true if the provided auth info was correct
	 */
	public static boolean authenticateUser(VaadinSession session, String login, String password)
	{
		if(ServerConfigurationInterface.avoidUsingDBForNow())
		{
			return true;
		}
		else
		{
			List<JPAUser> usersWithProvidedLogin = DAOs.userDAO.getByLogin(login);
			for(JPAUser user : usersWithProvidedLogin)
			{
				if(user.getPassword().equals(password)) // passwords match
				{
					login(session, user.getId()); // actually authenticate in this UI
					return true;
				}
			}
			return false;
		}
	}
	
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
	
	private static void login(VaadinSession session, int userID)
	{
		if(isUserAuthenticated(session))
		{
			throw new IllegalStateException("User is already authenticated in this session.");
		}
		else
		{
			ManageSession.setAttribute(session, ManageSession.key_userID, userID);
			NoSessionStore.bind(userID, session);
		}
	}
	
	public static void logout(VaadinSession session)
	{
		if(isUserAuthenticated(session))
		{
			NoSessionStore.unbind(getUserID(session), session);
			ManageSession.clearAttribute(session, ManageSession.key_userID);
			
			// Redirect from the page
			// getUI().getPage().setLocation("/myapp/logoutpage.html");
			
			/*
			 * Don't close the whole session to avoid redirecting to a logout page. Instead,
			 * close all non-main UIs manually and reset the content 
			 */
			// getSession().getUIs()
		}
		else
		{
			throw new IllegalStateException("User is not authenticated.");
		}
	}
}
