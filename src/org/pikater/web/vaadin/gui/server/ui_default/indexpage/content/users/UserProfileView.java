package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.users;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.forms.UserProfileForm;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.CustomComponent;

public class UserProfileView extends CustomComponent implements IContentComponent
{
	private static final long serialVersionUID = -5751678204210363235L;
	
	private final UserProfileForm innerForm;
	
	public UserProfileView()
	{
		super();
		
		this.innerForm = new UserProfileForm();
		this.innerForm.setSizeUndefined();
		
		setCompositionRoot(this.innerForm);
	}
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		innerForm.enter(ServerConfigurationInterface.avoidUsingDBForNow() ? JPAUser.getDummy() : ManageAuth.getUserEntity(VaadinSession.getCurrent()));
	}
	
	@Override
	public boolean hasUnsavedProgress()
	{
		return innerForm.isFormUpdated();
	}

	@Override
	public String getCloseDialogMessage()
	{
		return "Changes were not stored yet. Discard them and continue?";
	}
}