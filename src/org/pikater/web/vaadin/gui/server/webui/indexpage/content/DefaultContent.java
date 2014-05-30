package org.pikater.web.vaadin.gui.server.webui.indexpage.content;

import java.util.Date;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.AuthHandler;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class DefaultContent extends Label implements IContentComponent
{
	private static final long serialVersionUID = 9077723300509194087L;

	public DefaultContent()
	{
		super("", ContentMode.HTML);
	}
	
	@Override
	public void attach()
	{
		super.attach();
		
		if(ServerConfigurationInterface.avoidUsingDBForNow())
		{
			setValue("Welcome to Pikatorium.");
		}
		else
		{
			JPAUser user = AuthHandler.getUserEntity(getSession());
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
