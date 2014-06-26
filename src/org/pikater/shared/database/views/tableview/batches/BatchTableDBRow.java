package org.pikater.shared.database.views.tableview.batches;

import java.util.Locale;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.util.DateUtils;
import org.pikater.shared.util.LocaleUtils;

public class BatchTableDBRow extends AbstractTableRowDBView {

	private JPABatch batch=null;
	private Locale currentLocale=null;

	public BatchTableDBRow(JPABatch batch)
	{
		this.batch=batch;
		this.currentLocale=LocaleUtils.getDefaultLocale();
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column)
	{
		BatchTableDBView.Column specificColumn = (BatchTableDBView.Column) column;
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
			return new StringReadOnlyDBViewValue(LocaleUtils.formatInteger(currentLocale, batch.getTotalPriority()));
		case PRIORITY:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatInteger(currentLocale, batch.getPriority()));
		case OWNER:
			return new StringReadOnlyDBViewValue(batch.getOwner().getLogin());
		case STATUS:
			return new StringReadOnlyDBViewValue(batch.getStatus().name());
		case NOTE: //TODO: make it editable (note for me)
			return new StringReadOnlyDBViewValue(batch.getNote());
			
		/*
		 * And then custom actions.
		 */
		case EXPERIMENTS:
			if((batch.getExperiments()==null) || batch.getExperiments().isEmpty()){
				return new NamedActionDBViewValue("No Experiments") {
					
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
			}else{
				return new NamedActionDBViewValue("Show Experiments") {	

					@Override
					public boolean isEnabled()
					{
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
			}

		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}
