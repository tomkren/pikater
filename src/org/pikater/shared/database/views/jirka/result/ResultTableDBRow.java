package org.pikater.shared.database.views.jirka.result;

import java.util.Locale;

import org.pikater.shared.LocaleUtils;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.ActionDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;
import org.pikater.shared.util.DateUtils;

public class ResultTableDBRow extends AbstractTableRowDBView {

	private JPAResult result=null;
	private Locale currentLocale=null;

	public ResultTableDBRow(JPAResult result)
	{
		this.result=result;
		this.currentLocale=LocaleUtils.getDefaultLocale();
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final IColumn column)
	{
		ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case AGENT_NAME:
			return new StringDBViewValue(result.getAgentName(), true)
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
			//TODO: fix the back references
			String res="error";
			try{
				res=result.getExperiment().getBatch().getOwner().getLogin();
			}catch(NullPointerException npe){}
			return new StringDBViewValue(res, true)
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
		case NOTE:
			return new StringDBViewValue(result.getNote(), true)
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
		case STARTED:
			return new StringDBViewValue(DateUtils.toCzechDate(result.getStart()), true)
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
			return new StringDBViewValue(DateUtils.toCzechDate(result.getFinish()), true)
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
		case ERROR_RATE:
			return new StringDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getErrorRate()), true)
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
		case KAPPA_STATISTIC:
			return new StringDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getKappaStatistic()), true)
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
		case MEAN_ABSOLUTE_ERROR:
			return new StringDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getMeanAbsoluteError()), true)
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
		case RELATIVE_ABSOLUTE_ERROR:
			return new StringDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRelativeAbsoluteError()), true)
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
		case ROOT_MEAN_SQUARED_ERROR:
			return new StringDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRootMeanSquaredError()), true)
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
		case ROOT_RELATIVE_SQUARED_ERROR:
			return new StringDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRootRelativeSquaredError()), true)
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
		case WEKA_OPTIONS:
			return new StringDBViewValue(result.getOptions(), true)
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
		
		case CREATED_MODEL:
			//TODO: Implement best model retrieval
			return new ActionDBViewValue("Get Model") {	
				@Override
				public void execute() {
					// TODO add model retrieval implementation
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
