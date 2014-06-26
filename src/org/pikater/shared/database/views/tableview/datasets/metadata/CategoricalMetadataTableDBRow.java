package org.pikater.shared.database.views.tableview.datasets.metadata;

import java.util.Locale;

import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.StringDBViewValue;
import org.pikater.shared.database.views.tableview.base.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.util.LocaleUtils;

public class CategoricalMetadataTableDBRow extends AbstractTableRowDBView {

	private JPAAttributeCategoricalMetaData attrCat=null;
	private Locale currentLocale;

	public CategoricalMetadataTableDBRow(JPAAttributeCategoricalMetaData attrCat,Locale locale)
	{
		this.attrCat=attrCat;
		this.currentLocale=locale;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column)
	{
		CategoricalMetaDataTableDBView.Column specificColumn = (CategoricalMetaDataTableDBView.Column) column;
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
			return new StringDBViewValue(LocaleUtils.formatBool(currentLocale,attrCat.isTarget()), true)
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
			return new StringDBViewValue(LocaleUtils.formatDouble(currentLocale,attrCat.getRatioOfMissingValues()), true)
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
			return new StringDBViewValue(LocaleUtils.formatInteger(currentLocale,attrCat.getNumberOfCategories()), true)
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
