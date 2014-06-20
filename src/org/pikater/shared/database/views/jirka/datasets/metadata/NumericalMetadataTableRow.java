package org.pikater.shared.database.views.jirka.datasets.metadata;

import java.util.Locale;

import org.pikater.shared.AppHelper;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;

public class NumericalMetadataTableRow extends AbstractTableRowDBView {

	private JPAAttributeNumericalMetaData attrNum=null;
	private Locale currentLocale;

	public NumericalMetadataTableRow(JPAAttributeNumericalMetaData attrNum,Locale locale)
	{
		this.attrNum=attrNum;
		this.currentLocale=locale;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final IColumn column)
	{
		NumericalMetaDataTableView.Column specificColumn = (NumericalMetaDataTableView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case NAME:
			return new StringDBViewValue(attrNum.getName(), true)
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
		case AVERAGE:
			return new StringDBViewValue(AppHelper.formatDouble(currentLocale, attrNum.getAvarage()), true)
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
		case IS_REAL:
			return new StringDBViewValue(AppHelper.formatBool(currentLocale,attrNum.getIsReal()), true)
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
		case IS_TARGET:
			return new StringDBViewValue(AppHelper.formatBool(currentLocale,attrNum.isTarget()), true)
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
		case MAXIMUM:
			return new StringDBViewValue(AppHelper.formatDouble(currentLocale,attrNum.getMax()), true)
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
		case MEDIAN:
			return new StringDBViewValue(AppHelper.formatDouble(currentLocale,attrNum.getMedian()), true)
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
		case MINIMUM:
			return new StringDBViewValue(AppHelper.formatDouble(currentLocale,attrNum.getMin()), true)
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
		case MODE:
			return new StringDBViewValue(AppHelper.formatDouble(currentLocale,attrNum.getMode()), true)
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
		case RATIO_OF_MISSING_VALUES:
			return new StringDBViewValue(AppHelper.formatDouble(currentLocale,attrNum.getRatioOfMissingValues()), true)
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
		case VARIANCE:
			return new StringDBViewValue(AppHelper.formatDouble(currentLocale,attrNum.getVariance()), true)
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
