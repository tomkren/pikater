package org.pikater.shared.database.views.tableview.batches;

import java.util.LinkedHashSet;
import java.util.Set;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.RepresentativeDBViewValue;
import org.pikater.shared.database.views.base.values.RepresentativeReadonlyDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.util.DateUtils;

public class BatchTableDBRow extends AbstractTableRowDBView
{
	private static final Set<String> allowedTotalPriorities = new LinkedHashSet<String>()
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
	// private final Locale currentLocale;
	private final boolean adminMode;

	public BatchTableDBRow(JPABatch batch, boolean adminMode)
	{
		this.batch = batch;
		// this.currentLocale = LocaleUtils.getDefaultLocale();
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
					public boolean isReadOnly()
					{
						return batch.isBeingExecuted();
					}
					
					@Override
					protected void updateEntities(String newValue)
					{
						batch.setTotalPriority(Integer.parseInt(newValue));
					}
					
					@Override
					protected void commitEntities()
					{
						commitRow();
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
			
		/*
		 * And then actions. 
		 */
		case ABORT:
			return new NamedActionDBViewValue("Abort") // no DB changes needed - this is completely GUI managed
			{
				@Override
				public boolean isEnabled()
				{
					return batch.isDesignatedForExecution();
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
		case RESULTS:
			return new NamedActionDBViewValue("Download") // no DB changes needed - this is completely GUI managed
			{
				@Override
				public boolean isEnabled()
				{
					return batch.isFinishedOrFailed();
				}
				
				@Override
				protected void commitEntities()
				{
				}
				
				@Override
				protected void updateEntities()
				{
				}
			}; 
			
		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
		DAOs.batchDAO.updateEntity(batch);
	}
}
