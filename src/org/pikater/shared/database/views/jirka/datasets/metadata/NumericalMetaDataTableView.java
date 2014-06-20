package org.pikater.shared.database.views.jirka.datasets.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.pikater.shared.AppHelper;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.QueryConstraints;
import org.pikater.shared.database.views.jirka.abstractview.QueryResult;

/**
 * A generic view for tables displaying numerical metadata information for a dataset.  
 */
public class NumericalMetaDataTableView extends AbstractTableDBView{
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
		AVERAGE,
		MINIMUM,
		MAXIMUM,
		MODE,
		MEDIAN,
		VARIANCE,
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
				case MINIMUM:
				case MAXIMUM:
				case AVERAGE:
				case MODE:
				case MEDIAN:
				case VARIANCE:
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
	 * Using this constructor values will be formatted using en-US locale 
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 */
	public NumericalMetaDataTableView(JPADataSetLO datasetlo)
	{
		this.dslo=datasetlo;
		this.currentLocale=AppHelper.getDefaultLocale();
	}
	
	/**
	 * Constructor if one wants to add custom locale information
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 * @param locale Locale used for value formatting
	 */
	public NumericalMetaDataTableView(JPADataSetLO datasetlo,Locale locale){
		this.dslo=datasetlo;
		this.currentLocale=locale;
	}
	
	@Override
	protected QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		
		List<NumericalMetadataTableRow> rows = new ArrayList<NumericalMetadataTableRow>();
		if(dslo != null)
		{
			for(JPAAttributeMetaData md : dslo.getAttributeMetaData())
			{
				if(md instanceof JPAAttributeNumericalMetaData)
				{
					rows.add(new NumericalMetadataTableRow((JPAAttributeNumericalMetaData)md,this.currentLocale));
				}
			}
		}
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), rows.size());
		return new QueryResult(rows.subList(constraints.getOffset(), endIndex), rows.size());
	}
}
