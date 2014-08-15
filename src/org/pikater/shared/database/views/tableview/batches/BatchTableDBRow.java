package org.pikater.shared.database.views.tableview.batches;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.RepresentativeDBViewValue;
import org.pikater.shared.database.views.base.values.RepresentativeReadonlyDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.util.DateUtils;
import org.pikater.shared.util.LocaleUtils;

public class BatchTableDBRow extends AbstractTableRowDBView
{
	private static final Set<String> allowedTotalPriorities = new HashSet<String>()
	{
		private static final long serialVersionUID = -7303124181497778648L;

		{
			for(int i = 1; i < 100; i++)
			{
				add(String.valueOf(i));
			}
		}
	};
	
	private final JPABatch batch;
	private final Locale currentLocale;
	private final boolean adminMode;

	public BatchTableDBRow(JPABatch batch, boolean adminMode)
	{
		this.batch = batch;
		this.currentLocale = LocaleUtils.getDefaultLocale();
		this.adminMode = adminMode;
	}
	
	public JPABatch getBatch()
	{
		return batch;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column)
	{
		AbstractBatchTableDBView.Column specificColumn = (AbstractBatchTableDBView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case NAME:
			return new StringReadOnlyDBViewValue(batch.getName());
		case CREATED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(batch.getCreated()));
		case FINISHED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(batch.getFinished()));
		case MAX_PRIORITY:
			if(adminMode)
			{
				return new RepresentativeDBViewValue(allowedTotalPriorities, String.valueOf(batch.getTotalPriority()))
				{
					@Override
					protected void updateEntities(String newValue)
					{
						// TODO Auto-generated method stub
					}
					
					@Override
					protected void commitEntities()
					{
						// TODO Auto-generated method stub
					}
				};
			}
			else
			{
				return new RepresentativeReadonlyDBViewValue(allowedTotalPriorities, String.valueOf(batch.getTotalPriority()));
			}
			
		case OWNER:
			return new StringReadOnlyDBViewValue(batch.getOwner().getLogin());
		case STATUS:
			return new StringReadOnlyDBViewValue(batch.getStatus().name());
		case NOTE:
			return new StringReadOnlyDBViewValue(batch.getNote());
		case ABORT:
			return new NamedActionDBViewValue("Abort")
			{
				@Override
				protected void commitEntities()
				{
					// TODO Auto-generated method stub
				}
				
				@Override
				protected void updateEntities()
				{
					// TODO Auto-generated method stub
				}
				
				@Override
				public boolean isEnabled()
				{
					return true; // TODO: experiment is scheduled or being executed
				}
			};
		case RESULTS:
			return new NamedActionDBViewValue("Download")
			{
				@Override
				public boolean isEnabled()
				{
					return true; // TODO: experiment is finished
				}
				
				@Override
				protected void commitEntities()
				{
					// TODO Auto-generated method stub
				}
				
				@Override
				protected void updateEntities()
				{
					// TODO Auto-generated method stub
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
