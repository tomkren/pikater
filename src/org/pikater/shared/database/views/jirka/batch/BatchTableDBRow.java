package org.pikater.shared.database.views.jirka.batch;

import java.util.Locale;

import org.pikater.shared.LocaleUtils;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.ActionDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;
import org.pikater.shared.util.DateUtils;

public class BatchTableDBRow extends AbstractTableRowDBView {

	private JPABatch batch=null;
	private Locale currentLocale=null;

	public BatchTableDBRow(JPABatch batch)
	{
		this.batch=batch;
		this.currentLocale=LocaleUtils.getDefaultLocale();
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final IColumn column)
	{
		BatchTableDBView.Column specificColumn = (BatchTableDBView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case NAME:
			return new StringDBViewValue(batch.getName(), true)
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
			return new StringDBViewValue(DateUtils.toCzechDate(batch.getCreated()), true)
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
		case FINISHED:
			return new StringDBViewValue(DateUtils.toCzechDate(batch.getFinished()), true)
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
		case MAX_PRIORITY:
			return new StringDBViewValue(LocaleUtils.formatInteger(currentLocale, batch.getTotalPriority()), true)
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
		case PRIORITY:
			return new StringDBViewValue(LocaleUtils.formatInteger(currentLocale, batch.getPriority()), true)
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
			return new StringDBViewValue(batch.getOwner().getLogin(), true)
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
		case STATUS:
			return new StringDBViewValue(batch.getStatus().name(), true)
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
		case EXPERIMENTS:
			if((batch.getExperiments()==null) || batch.getExperiments().isEmpty()){
				return new ActionDBViewValue("No Experiments") {
					@Override
					public void execute() {
						//we dont need to do anything
					}
				};
			}else{
				return new ActionDBViewValue("Show Experiments") {	
					@Override
					public void execute() {
						// TODO Auto-generated method stub
						
					}
				};
			}
		
			
		case NOTE: //TODO: make it editable (note for me)
			return new StringDBViewValue(batch.getNote(), true)
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
		

		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}
