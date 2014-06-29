package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin;

import java.io.InputStream;

import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.database.views.tableview.externalagents.ExternalAgentTableDBRow;
import org.pikater.shared.database.views.tableview.externalagents.ExternalAgentTableDBView;
import org.pikater.shared.database.views.tableview.externalagents.ExternalAgentTableDBView.Column;
import org.pikater.web.HttpContentType;
import org.pikater.web.servlets.download.DownloadRegistrar;
import org.pikater.web.servlets.download.IDownloadResource;
import org.pikater.web.vaadin.gui.server.components.dbviews.IDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;

public class AgentsView extends DBTableLayout implements IContentComponent, IDBViewRoot<ExternalAgentTableDBView>
{
	private static final long serialVersionUID = -4608720645535772140L;
	
	protected final ExternalAgentTableDBView underlyingDBView; 

	public AgentsView()
	{
		super();
		setSizeUndefined();
		
		this.underlyingDBView = new ExternalAgentTableDBView();
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		setView(this); // required to be executed after initializing db view
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

	@Override
	public ExternalAgentTableDBView getUnderlyingDBView()
	{
		return underlyingDBView;
	}

	@Override
	public void onCellCreate(ITableColumn column, Object component)
	{
	}

	@Override
	public int getColumnSize(ITableColumn column)
	{
		ExternalAgentTableDBView.Column specificColumn = (ExternalAgentTableDBView.Column) column;
		switch(specificColumn)
		{
			case OWNER:
				return 100;
			case CREATED:
				return 75;
			case NAME:
				return 150;
			case AGENT_CLASS:
				return 150; // TODO: set tooltip
			case DESCRIPTION:
				return 250;
			case APPROVE:
			case DOWNLOAD:
			case DELETE:
				return 100;
			default:
				throw new IllegalStateException("Unknown state: " + specificColumn.name());
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
			Page.getCurrent().setLocation(DownloadRegistrar.issueAOneTimeDownloadURL(new IDownloadResource()
			{
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
			}));
		}
		else if(specificColumn == Column.DELETE)
		{
			MyDialogs.confirm(null, String.format("Really delete agent '%s'?", agentName), new MyDialogs.IDialogResultHandler()
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