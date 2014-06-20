package org.pikater.shared.database.views.jirka.datasets.metadata;

import java.util.Locale;

import org.pikater.shared.AppHelper;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;

public class CategoricalMetadataTableRow extends AbstractTableRowDBView {

	private JPAAttributeCategoricalMetaData attrCat=null;
	private Locale currentLocale;

	public CategoricalMetadataTableRow(JPAAttributeCategoricalMetaData attrCat,Locale locale)
	{
		this.attrCat=attrCat;
		this.currentLocale=locale;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final IColumn column)
	{
		CategoricalMetaDataTableView.Column specificColumn = (CategoricalMetaDataTableView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case NAME:
			return new StringDBViewValue(attrCat.getName(), true)
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
			return new StringDBViewValue(AppHelper.formatBool(currentLocale,attrCat.isTarget()), true)
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
			return new StringDBViewValue(AppHelper.formatDouble(currentLocale,attrCat.getRatioOfMissingValues()), true)
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
		
		case NUMBER_OF_CATEGORIES:
			return new StringDBViewValue(AppHelper.formatInteger(currentLocale,attrCat.getNumberOfCategories()), true)
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
