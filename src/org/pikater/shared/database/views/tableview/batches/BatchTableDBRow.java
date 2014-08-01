package org.pikater.shared.database.views.tableview.batches;

import java.util.Locale;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
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
			return new StringReadOnlyDBViewValue(LocaleUtils.formatInteger(currentLocale, batch.getTotalPriority()));
		case PRIORITY:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatInteger(currentLocale, batch.getPriority()));
		case OWNER:
			return new StringReadOnlyDBViewValue(batch.getOwner().getLogin());
		case STATUS:
			return new StringReadOnlyDBViewValue(batch.getStatus().name());
		case NOTE:
			return new StringReadOnlyDBViewValue(batch.getNote());
			
		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}
