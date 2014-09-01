package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content;

import java.util.Date;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.web.config.WebAppConfiguration;
import org.pikater.web.vaadin.ManageAuth;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class WelcomeView extends Label implements IContentComponent
{
	private static final long serialVersionUID = 9077723300509194087L;

	public WelcomeView()
	{
		super("", ContentMode.HTML);
		setSizeFull();
	}
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		if(WebAppConfiguration.avoidUsingDBForNow())
		{
			setValue("Welcome to Pikatorium.");
		}
		else
		{
			JPAUser user = ManageAuth.getUserEntity(VaadinSession.getCurrent());
			setValue(String.format("Welcome to Pikatorium.</br>Your last visit was: %s", user.getLastLogin()));
			user.setLastLogin(new Date());
			DAOs.userDAO.updateEntity(user);
		}
	}
	
	@Override
	public boolean isReadyToClose()
	{
		return true;
	}

	@Override
	public String getCloseMessage()
	{
		return null;
	}
	
	@Override
	public void beforeClose()
	{
	}
}
