package org.pikater.shared.database.views.jirka.datasets.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.pikater.shared.AppHelper;
import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.QueryConstraints;

/**
 * A generic view for tables displaying categorical metadata information for a dataset.  
 */
public class CategoricalMetaDataTableView extends AbstractTableDBView{
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 */
	public enum Column implements IColumn
	{
		/*
		 * First the read-only properties.
		 */
		NAME,
		IS_TARGET,
		IS_REAL,
		NUMBER_OF_CATEGORIES,
		RATIO_OF_MISSING_VALUES;

		@Override
		public String getDisplayName()
		{
			return this.name();
		}

		@Override
		public ColumnType getColumnType()
		{
			switch(this)
			{
				case NAME:
					return ColumnType.STRING;
				case IS_REAL:
				case IS_TARGET:
					return ColumnType.STRING;
				case NUMBER_OF_CATEGORIES:
				case RATIO_OF_MISSING_VALUES:
					return ColumnType.STRING;
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}

	@Override
	public IColumn[] getColumns()
	{
		return Column.values();
	}
	
	
	JPADataSetLO dslo=null;
	Locale currentLocale;
	/** 
	 * Constructor.
	 * Using this constructor values will be formatted using default locale 
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 */
	public CategoricalMetaDataTableView(JPADataSetLO datasetlo)
	{
		this.dslo=datasetlo;
		this.currentLocale=AppHelper.getDefaultLocale();
	}
	
	/**
	 * Constructor if one wants to add custom locale information
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 * @param locale Locale used for value formatting
	 */
	public CategoricalMetaDataTableView(JPADataSetLO datasetlo,Locale locale){
		this.dslo=datasetlo;
		this.currentLocale=locale;
	}

	@Override
	public Collection<? extends AbstractTableRowDBView> getUninitializedRowsAscending(QueryConstraints constraints)
	{
		Collection<CategoricalMetadataTableRow> rows = new ArrayList<CategoricalMetadataTableRow>();
		if(dslo!=null){
		for(JPAAttributeMetaData md:dslo.getAttributeMetaData())
		{
			if(md instanceof JPAAttributeCategoricalMetaData){
				rows.add(new CategoricalMetadataTableRow((JPAAttributeCategoricalMetaData)md,this.currentLocale));
			}
		}
		}
		return rows;
	}
}
