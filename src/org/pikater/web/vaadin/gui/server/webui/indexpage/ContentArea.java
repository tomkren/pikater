package org.pikater.web.vaadin.gui.server.webui.indexpage;

import java.util.Date;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.AuthHandler;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class ContentArea extends Panel
{
	private static final long serialVersionUID = 7642456908975377869L;
	
	private final Label defaultContent;
	
	public ContentArea()
	{
		super();
		setStyleName("contentArea");
		setSizeFull();
		
		this.defaultContent = new Label("", ContentMode.HTML);
		setContent(this.defaultContent);
	}
	
	@Override
	public void setContent(Component content)
	{
		super.setContent(content != null ? content : defaultContent);
	}
	
	@Override
	public void attach()
	{
		super.attach();
		
		if(ServerConfigurationInterface.avoidUsingDBForNow())
		{
			this.defaultContent.setValue("Welcome to Pikatorium.");
		}
		else
		{
			JPAUser user = AuthHandler.getUserEntity(getSession());
			this.defaultContent.setValue(String.format("Welcome to Pikatorium.</br>Your last visit was: %s", user.getLastLogin()));
			user.setLastLogin(new Date());
			DAOs.userDAO.updateEntity(user);
		}
	}
}
