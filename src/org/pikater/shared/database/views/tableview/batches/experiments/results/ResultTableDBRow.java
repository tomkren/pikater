package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.Locale;

import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.util.DateUtils;
import org.pikater.shared.util.LocaleUtils;

public class ResultTableDBRow extends AbstractTableRowDBView {

	private JPAResult result=null;
	private Locale currentLocale=null;

	public ResultTableDBRow(JPAResult result)
	{
		this.result=result;
		this.currentLocale=LocaleUtils.getDefaultLocale();
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
			return new StringReadOnlyDBViewValue(result.getAgentName());
		case OWNER:
			//TODO: fix the back references
			String res="error";
			try{
				res=result.getExperiment().getBatch().getOwner().getLogin();
			}catch(NullPointerException npe){}
			return new StringReadOnlyDBViewValue(res);
		case NOTE:
			return new StringReadOnlyDBViewValue(result.getNote());
		case STARTED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(result.getStart()));
		case FINISHED:
			return new StringReadOnlyDBViewValue(DateUtils.toCzechDate(result.getFinish()));
		case ERROR_RATE:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getErrorRate()));
		case KAPPA_STATISTIC:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getKappaStatistic()));
		case MEAN_ABSOLUTE_ERROR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getMeanAbsoluteError()));
		case RELATIVE_ABSOLUTE_ERROR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRelativeAbsoluteError()));
		case ROOT_MEAN_SQUARED_ERROR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRootMeanSquaredError()));
		case ROOT_RELATIVE_SQUARED_ERROR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRootRelativeSquaredError()));
		case WEKA_OPTIONS:
			return new StringReadOnlyDBViewValue(result.getOptions());
		
		/*
		 * And then custom actions.
		 */
		case CREATED_MODEL:
			//TODO: Implement best model retrieval
			return new NamedActionDBViewValue("Get Model") {	

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
		
		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}