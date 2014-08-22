package org.pikater.shared.database.views.tableview.datasets.metadata;

import java.util.Locale;

import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.BooleanReadOnlyDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
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
			return new StringReadOnlyDBViewValue(attrCat.getName());
		case IS_TARGET:
			return new BooleanReadOnlyDBViewValue(attrCat.isTarget());
		case RATIO_OF_MISSING_VALUES:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale,attrCat.getRatioOfMissingValues()));
		case CATEGORY_COUNT:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatInteger(currentLocale,attrCat.getNumberOfCategories()));
		case ENTROPY:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale,attrCat.getEntropy()));
		case CLASS_ENTROPY:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale,attrCat.getClassEntropy()));
		
		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow()
	{
	}
}
