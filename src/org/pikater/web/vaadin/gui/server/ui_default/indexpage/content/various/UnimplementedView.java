package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.various;

import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;

public class UnimplementedView extends Label implements IContentComponent
{
	private static final long serialVersionUID = -7610583075707286907L;

	public UnimplementedView()
	{
		// super("<font color=\"red\">Unimplemented yet.</font>", ContentMode.HTML);
		super("Unimplemented yet");
		setStyleName("errorLabel");
	}
	
	@Override
	public void enter(ViewChangeEvent event)
	{
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
