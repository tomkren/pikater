package org.pikater.shared.database.views.tableview.datasets.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;
import org.pikater.shared.util.LocaleUtils;

/**
 * A generic view for tables displaying numerical metadata information for a dataset.  
 */
public class NumericalMetaDataTableDBView extends AbstractTableDBView{
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 */
	public enum Column implements ITableColumn
	{
		/*
		 * First the read-only properties.
		 */
		NAME,
		IS_TARGET,
		IS_REAL,
		MINIMUM,
		MAXIMUM,
		AVERAGE,
		// MODE,
		MEDIAN,
		VARIANCE,
		RATIO_OF_MISSING_VALUES;

		@Override
		public String getDisplayName()
		{
			return this.name();
		}

		@Override
		public DBViewValueType getColumnType()
		{
			switch(this)
			{
				case NAME:
					return DBViewValueType.STRING;
				case IS_REAL:
				case IS_TARGET:
					return DBViewValueType.BOOLEAN;
				case MINIMUM:
				case MAXIMUM:
				case AVERAGE:
				// case MODE:
				case MEDIAN:
				case VARIANCE:
				case RATIO_OF_MISSING_VALUES:
					return DBViewValueType.STRING;
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}

	@Override
	public ITableColumn[] getColumns()
	{
		return Column.values();
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return Column.NAME;
	}
	
	JPADataSetLO dslo=null;
	Locale currentLocale;
	/** 
	 * Constructor.
	 * Using this constructor values will be formatted using en-US locale 
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 */
	public NumericalMetaDataTableDBView(JPADataSetLO datasetlo)
	{
		this.dslo=datasetlo;
		this.currentLocale=LocaleUtils.getDefaultLocale();
	}
	
	/**
	 * Constructor if one wants to add custom locale information
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 * @param locale Locale used for value formatting
	 */
	public NumericalMetaDataTableDBView(JPADataSetLO datasetlo,Locale locale){
		this.dslo=datasetlo;
		this.currentLocale=locale;
	}
	
	@Override
	protected QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		
		List<NumericalMetadataTableDBRow> rows = new ArrayList<NumericalMetadataTableDBRow>();
		if(dslo != null)
		{
			for(JPAAttributeMetaData md : dslo.getAttributeMetaData())
			{
				if(md instanceof JPAAttributeNumericalMetaData)
				{
					rows.add(new NumericalMetadataTableDBRow((JPAAttributeNumericalMetaData)md,this.currentLocale));
				}
			}
		}
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), rows.size());
		return new QueryResult(rows.subList(constraints.getOffset(), endIndex), rows.size());
	}
}