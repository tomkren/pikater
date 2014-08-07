package org.pikater.shared.database.views.tableview.datasets;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.BooleanDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.util.DateUtils;
import org.pikater.shared.util.FilesizeUtils;

public class DataSetTableDBRow extends AbstractTableRowDBView {

	private JPADataSetLO dataset=null;

	public DataSetTableDBRow(JPADataSetLO dataset)
	{
		this.dataset=dataset;
	}
	
	public JPADataSetLO getDataset()
	{
		return dataset;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column)
	{
		DataSetTableDBView.Column specificColumn = (DataSetTableDBView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case DESCRIPTION:
			return new StringReadOnlyDBViewValue(dataset.getDescription());
		case NUMBER_OF_INSTANCES:
			return new StringReadOnlyDBViewValue(dataset.getGlobalMetaData()!=null?""+dataset.getGlobalMetaData().getNumberofInstances():"N/A");
		case DEFAULT_TASK_TYPE:
			JPAGlobalMetaData gmd=dataset.getGlobalMetaData();
			if(gmd!=null){
				return new StringReadOnlyDBViewValue(gmd.getDefaultTaskType()!=null? gmd.getDefaultTaskType().getName() : "N/A");
			}else{
				//the global metadata for this dataset is not available
				return new StringReadOnlyDBViewValue("N/A");
			}
		case SIZE:
			return new StringReadOnlyDBViewValue(FilesizeUtils.formatFileSize(dataset.getSize()));
		case CREATED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(dataset.getCreated()));
		case OWNER:
			return new StringReadOnlyDBViewValue(dataset.getOwner().getLogin()); 
			
		/*
		 * And then custom actions.
		 */
		case APPROVED:
			return new BooleanDBViewValue(dataset.isApproved())
			{
				@Override
				protected void updateEntities(Boolean newValue)
				{
					// TODO Auto-generated method stub
				}
				
				@Override
				protected void commitEntities()
				{
					// TODO Auto-generated method stub
				}
			};
		case DOWNLOAD:
			return new NamedActionDBViewValue("Download")
			{
				@Override
				public boolean isEnabled()
				{
					return true;
				}
				
				@Override
				protected void updateEntities()
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case DELETE:
			return new NamedActionDBViewValue("Delete")
			{
				@Override
				public boolean isEnabled()
				{
					return true;
				}
				
				@Override
				protected void updateEntities()
				{
				}
				
				@Override
				protected void commitEntities()
				{
					// TODO: don't really delete but "hide" instead?
				}
			};

		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}
