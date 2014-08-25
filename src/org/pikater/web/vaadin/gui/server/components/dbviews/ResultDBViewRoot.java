package org.pikater.web.vaadin.gui.server.components.dbviews;

import java.io.InputStream;
import java.util.UUID;

import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.batches.experiments.results.ResultTableDBRow;
import org.pikater.shared.database.views.tableview.batches.experiments.results.ResultTableDBView;
import org.pikater.web.HttpContentType;
import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;
import org.pikater.web.sharedresources.download.IDownloadResource;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.AbstractDBViewRoot;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.TextField;

public class ResultDBViewRoot<V extends ResultTableDBView> extends AbstractDBViewRoot<V>
{
	public ResultDBViewRoot(V view)
	{
		super(view);
	}

	@Override
	public int getColumnSize(ITableColumn column)
	{
		ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
		switch(specificColumn)
		{
			case AGENT_NAME:
			case ERROR_RATE:
			case KAPPA:
			case REL_ABS_ERR:
			case MEAN_ABS_ERR:
				return 100;
				
			case ROOT_REL_SQR_ERR:
			case TRAINED_MODEL:
				return 115;
				
			case WEKA_OPTIONS:
			case NOTE:
			case ROOT_MEAN_SQR_ERR:
				return 125;
			
			default:
				throw new IllegalStateException("Unknown state: " + specificColumn.name());
		}
	}
	
	@Override
	public ITableColumn getExpandColumn()
	{
		return null;
	}
	
	@Override
	public void onCellCreate(ITableColumn column, AbstractDBViewValue<?> value, AbstractComponent component)
	{
		ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
		if(specificColumn == ResultTableDBView.Column.NOTE)
		{
			TextField tf_value = (TextField) component;
			tf_value.setDescription(tf_value.getValue());				
		}
	}
	
	@Override
	public void approveAction(ITableColumn column, AbstractTableRowDBView row, Runnable action)
	{
		ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
		if(specificColumn == ResultTableDBView.Column.TRAINED_MODEL)
		{
			// download, don't run action
			final ResultTableDBRow rowView = (ResultTableDBRow) row;
			UUID resultsDownloadResourceUI = ResourceRegistrar.registerResource(VaadinSession.getCurrent(), new IDownloadResource()
			{
				@Override
				public ResourceExpiration getLifeSpan()
				{
					return ResourceExpiration.ON_FIRST_PICKUP;
				}

				@Override
				public InputStream getStream() throws Throwable
				{
					return rowView.getResult().getCreatedModel().getInputStream();
				}

				@Override
				public long getSize()
				{
					return rowView.getResult().getCreatedModel().getSerializedAgent().length;
				}

				@Override
				public String getMimeType()
				{
					return HttpContentType.APPLICATION_OCTET_STREAM.toString();
				}

				@Override
				public String getFilename()
				{
					return rowView.getResult().getCreatedModel().getFileName();
				}
			});
			Page.getCurrent().setLocation(ResourceRegistrar.getDownloadURL(resultsDownloadResourceUI));
		}
		else
		{
			throw new IllegalStateException(String.format("Action '%s' has to be approved before being executed", specificColumn.name())); 
		}
	}
}