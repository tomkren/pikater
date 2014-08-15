package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import java.io.InputStream;
import java.util.UUID;

import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.externalagents.ExternalAgentTableDBRow;
import org.pikater.shared.database.views.tableview.externalagents.ExternalAgentTableDBView;
import org.pikater.shared.database.views.tableview.externalagents.ExternalAgentTableDBView.Column;
import org.pikater.web.HttpContentType;
import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.pikater.web.sharedresources.download.IDownloadResource;
import org.pikater.web.vaadin.gui.server.components.dbviews.IDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TextField;

public class AgentsView extends DBTableLayout implements IContentComponent, IDBViewRoot<ExternalAgentTableDBView>
{
	private static final long serialVersionUID = -4608720645535772140L;
	
	protected final ExternalAgentTableDBView underlyingDBView;

	public AgentsView()
	{
		super();
		setSizeUndefined();
		setWidth("100%");
		
		this.underlyingDBView = new ExternalAgentTableDBView();
	}
	
	//----------------------------------------------------
	// VIEW INTERFACE

	@Override
	public void enter(ViewChangeEvent event)
	{
		setView(this); // required to be executed after initializing DB view
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
	
	//----------------------------------------------------
	// DB VIEW INTERFACE
	
	@Override
	public ExternalAgentTableDBView getUnderlyingDBView()
	{
		return underlyingDBView;
	}

	@Override
	public int getColumnSize(ITableColumn column)
	{
		ExternalAgentTableDBView.Column specificColumn = (ExternalAgentTableDBView.Column) column;
		switch(specificColumn)
		{
			case OWNER:
			case CREATED:
				return 100;
			case NAME:
				return 150;
			case AGENT_CLASS:
				return 150;
			case DESCRIPTION:
				return 250;
			case APPROVED:
				return 75;
			case DOWNLOAD:
				return 100;
			case DELETE:
				return 75;
			default:
				throw new IllegalStateException("Unknown state: " + specificColumn.name());
		}
	}
	
	@Override
	public ITableColumn getExpandColumn()
	{
		return ExternalAgentTableDBView.Column.DESCRIPTION;
	}
	
	@Override
	public void onCellCreate(ITableColumn column, AbstractComponent component)
	{
		ExternalAgentTableDBView.Column specificColumn = (ExternalAgentTableDBView.Column) column;
		if((specificColumn == Column.AGENT_CLASS) || (specificColumn == Column.DESCRIPTION)) 
		{
			TextField tf_value = (TextField) component;
			tf_value.setDescription(tf_value.getValue());
		}
	}

	@Override
	public void approveAction(ITableColumn column, AbstractTableRowDBView row, final Runnable action)
	{
		String agentName = (String) row.getValueWrapper(ExternalAgentTableDBView.Column.NAME).getValue();
		
		ExternalAgentTableDBView.Column specificColumn = (ExternalAgentTableDBView.Column) column;
		if(specificColumn == Column.DOWNLOAD)
		{
			// download, don't run action
			final ExternalAgentTableDBRow rowView = (ExternalAgentTableDBRow) row;
			final UUID agentDownloadResourceUUID = ResourceRegistrar.registerResource(VaadinSession.getCurrent(), new IDownloadResource()
			{
				@Override
				public ResourceExpiration getLifeSpan()
				{
					return ResourceExpiration.ON_FIRST_PICKUP;
				}
				
				@Override
				public InputStream getStream()
				{
					return rowView.getAgent().getInputStream();
				}
				
				@Override
				public long getSize()
				{
					return rowView.getAgent().getJar().length;
				}
				
				@Override
				public String getMimeType()
				{
					return HttpContentType.APPLICATION_JAR.toString();
				}
				
				@Override
				public String getFilename()
				{
					return rowView.getAgent().getName();
				}
			});
			Page.getCurrent().setLocation(ResourceRegistrar.getDownloadURL(agentDownloadResourceUUID));
		}
		else if(specificColumn == Column.DELETE)
		{
			GeneralDialogs.confirm(null, String.format("Really delete agent '%s'?", agentName), new GeneralDialogs.IDialogResultHandler()
			{
				@Override
				public boolean handleResult(Object[] args)
				{
					action.run();
					return true; // close the dialog
				}
			});
		}
		else
		{
			action.run();
		}
	}
}