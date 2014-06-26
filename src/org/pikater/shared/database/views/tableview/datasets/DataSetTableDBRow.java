package org.pikater.shared.database.views.tableview.datasets;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.StringDBViewValue;
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
			return new StringDBViewValue(dataset.getDescription(), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case NUMBER_OF_INSTANCES:
			return new StringDBViewValue(dataset.getGlobalMetaData()!=null?""+dataset.getGlobalMetaData().getNumberofInstances():"N/A", true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case DEFAULT_TASK_TYPE:
			JPAGlobalMetaData gmd=dataset.getGlobalMetaData();
			if(gmd!=null){
				return new StringDBViewValue(gmd.getDefaultTaskType()!=null? gmd.getDefaultTaskType().getName() : "N/A", true)
				{
					@Override
					protected void updateEntities(String newValue)
					{
					}
					
					@Override
					protected void commitEntities()
					{
					}
				};
			}else{
				//the global metadata for this dataset is not available
				return new StringDBViewValue("N/A", true)
				{
					@Override
					protected void updateEntities(String newValue)
					{
					}
					
					@Override
					protected void commitEntities()
					{
					}
				};
			}
		case SIZE:
			return new StringDBViewValue(FilesizeUtils.formatFileSize(dataset.getSize()), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case CREATED:
			return new StringDBViewValue(DateUtils.toCzechDate(dataset.getCreated()), true)
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
		case OWNER:
			return new StringDBViewValue(dataset.getOwner().getLogin(),true) 
			{
				@Override
				protected void updateEntities(String newValue)
				{
				}
				
				@Override
				protected void commitEntities()
				{
				}
			};
			
		/*
		 * And then actions.
		 */
		case APPROVE:
			return new NamedActionDBViewValue("Approve")
			{
				@Override
				public boolean isEnabled()
				{
					// TODO: whether this dataset is already approved 
					return true;
				}
				
				@Override
				protected void updateEntities()
				{
					// TODO Auto-generated method stub
				}
				
				@Override
				protected void commitEntities()
				{
					// TODO Auto-generated method stub
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
