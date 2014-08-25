package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.datasets;

import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBView;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.dbviews.DatasetDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.datasets.upload.DatasetUploadWizard;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.steinwedel.messagebox.MessageBox;

public class UserDatasetsView extends DatasetsView
{
	private static final long serialVersionUID = 5568928739598297585L;
	
	public UserDatasetsView()
	{
		super();
		
		getMainLayout().setCommitImmediately(false);
		getMainLayout().addCustomActionComponent(new Button("Upload new dataset", new Button.ClickListener()
		{
			private static final long serialVersionUID = -1045335713385385849L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				MessageBox mb = GeneralDialogs.wizardDialog("Dataset upload guide", new DatasetUploadWizard(getMainLayout().getTable()));
				mb.setWidth("600px");
				mb.setHeight("500px");
			}
		}));
	}
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		if(ManageAuth.isUserAuthenticated(VaadinSession.getCurrent()))
		{
			// always call these last, when you're absolutely ready to display the content
			getMainLayout().setView(new DatasetDBViewRoot<DataSetTableDBView>(new DataSetTableDBView(
					ManageAuth.getUserEntity(VaadinSession.getCurrent()
			))));
			super.finishInit(); // don't forget to!
		}
		else
		{
			throw new IllegalStateException("User is not logged in.");
		}
	}
}