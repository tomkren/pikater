package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.Locale;

import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.util.LocaleUtils;

public class ResultTableDBRow extends AbstractTableRowDBView {

	private JPAResult result=null;
	private Locale currentLocale=null;

	public ResultTableDBRow(JPAResult result)
	{
		this.result=result;
		this.currentLocale=LocaleUtils.getDefaultLocale();
	}
	
	public JPAResult getResult()
	{
		return result;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column)
	{
		ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case AGENT_NAME:
			return new StringReadOnlyDBViewValue(result.getAgentName().substring(result.getAgentName().lastIndexOf(".") + 1));
		case WEKA_OPTIONS:
			return new StringReadOnlyDBViewValue(result.getOptions());
		case NOTE:
			return new StringReadOnlyDBViewValue(result.getNote());
		case ERROR_RATE:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getErrorRate()));
		case KAPPA:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getKappaStatistic()));
		case MEAN_ABS_ERR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getMeanAbsoluteError()));
		case REL_ABS_ERR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRelativeAbsoluteError()));
		case ROOT_MEAN_SQR_ERR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRootMeanSquaredError()));
		case ROOT_REL_SQR_ERR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRootRelativeSquaredError()));
		
		/*
		 * And then custom actions.
		 */
		case TRAINED_MODEL:
			return new NamedActionDBViewValue("Download") // no DB changes needed - this is completely GUI managed
			{
				@Override
				public boolean isEnabled()
				{
					return result.getCreatedModel() != null;
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
		
		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}