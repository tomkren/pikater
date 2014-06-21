package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.jirka.datasets.DataSetTableDBView;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.tabledbview.expandable.ExpandableDBTableWizard;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;

public class DatasetsView extends ExpandableDBTableWizard implements IContentComponent
{
	private static final long serialVersionUID = -1564809345462937610L;
	
	// TODO: adding datasets: ARFF, CSV, XLS

	public DatasetsView()
	{
		super();
		setSizeFull();
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		addStep(new DataSetTableDBView(ServerConfigurationInterface.avoidUsingDBForNow() ? JPAUser.getDummy() : ManageAuth.getUserEntity(VaadinSession.getCurrent())));
	}

	@Override
	public boolean hasUnsavedProgress()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCloseDialogMessage()
	{
		// TODO Auto-generated method stub
		return null;
	}
}