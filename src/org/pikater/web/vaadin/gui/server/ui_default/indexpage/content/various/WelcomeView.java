package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.various;

import java.util.Date;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

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
	}
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		if(ServerConfigurationInterface.avoidUsingDBForNow())
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
	public boolean hasUnsavedProgress()
	{
		return false;
	}

	@Override
	public String getCloseDialogMessage()
	{
		return null;
	}
}
