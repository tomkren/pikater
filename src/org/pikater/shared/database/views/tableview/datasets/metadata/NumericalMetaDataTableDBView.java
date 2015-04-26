package org.pikater.shared.database.views.tableview.datasets.metadata;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.AbstractTableDBView;
import org.pikater.shared.util.LocaleUtils;

/**
 * A generic view for tables displaying numerical metadata information for a dataset.  
 */
public class NumericalMetaDataTableDBView extends AbstractTableDBView {
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 */
	public enum Column implements ITableColumn {
		/*
		 * First the read-only properties.
		 */
		NAME, IS_TARGET, IS_REAL, MINIMUM, MAXIMUM, AVERAGE,
		// MODE,
		MEDIAN, STD_DEVIATION, VARIANCE, QUARTILE_1, QUARTILE_2, QUARTILE_3, SKEWNESS, KURTOSIS,
		RATIO_OF_MISSING_VALUES, ENTROPY, CLASS_ENTROPY, CHI_SQUARE_NORM, CHI_SQUARE_NORM_T, G_TEST;

		@Override
		public String getDisplayName() {
			switch (this) {
			case RATIO_OF_MISSING_VALUES:
				return "MISSING_VALUES_%";
			case MINIMUM:
				return "MIN";
			case MAXIMUM:
				return "MAX";
			case AVERAGE:
				return "AVG";
			case QUARTILE_1:
				return "1st Quartile";
			case QUARTILE_2:
				return "2nd Quartile";
			case QUARTILE_3:
				return "3rd Quartile";
			case STD_DEVIATION:
				return "STD. Deviation";
			case CHI_SQUARE_NORM:
				return "χ² Normal";
			case CHI_SQUARE_NORM_T:
				return "χ² N.Test";
			case G_TEST:
				return "G Test";
			case SKEWNESS:
			case KURTOSIS:
			default:
				return name();
			}
		}

		@Override
		public DBViewValueType getColumnType() {
			switch (this) {
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
			case QUARTILE_1:
			case QUARTILE_2:
			case QUARTILE_3:
			case STD_DEVIATION:
			case RATIO_OF_MISSING_VALUES:
			case ENTROPY:
			case CLASS_ENTROPY:
			case CHI_SQUARE_NORM:
			case CHI_SQUARE_NORM_T:
			case G_TEST:
			case SKEWNESS:
			case KURTOSIS:
				return DBViewValueType.STRING;
			default:
				throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}

	@Override
	public Set<ITableColumn> getAllColumns() {
		return new LinkedHashSet<ITableColumn>(EnumSet.allOf(Column.class));
	}

	@Override
	public Set<ITableColumn> getDefaultColumns() {
		Set<ITableColumn> result = getAllColumns();
		result.remove(Column.IS_REAL);
		return result;
	}

	@Override
	public ITableColumn getDefaultSortOrder() {
		return Column.NAME;
	}

	JPADataSetLO dslo = null;
	Locale currentLocale;

	/** 
	 * Constructor.
	 * Using this constructor values will be formatted using en-US locale 
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 */
	public NumericalMetaDataTableDBView(JPADataSetLO datasetlo) {
		this.dslo = datasetlo;
		this.currentLocale = LocaleUtils.getDefaultLocale();
	}

	/**
	 * Constructor if one wants to add custom locale information
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 * @param locale Locale used for value formatting
	 */
	public NumericalMetaDataTableDBView(JPADataSetLO datasetlo, Locale locale) {
		this.dslo = datasetlo;
		this.currentLocale = locale;
	}

	@Override
	protected QueryResult queryUninitializedRows(QueryConstraints constraints) {
		List<NumericalMetadataTableDBRow> resultRows = new ArrayList<NumericalMetadataTableDBRow>();
		if (dslo != null) {
			for (JPAAttributeMetaData md : dslo.getAttributeMetaData()) {
				if (md instanceof JPAAttributeNumericalMetaData) {
					resultRows.add(new NumericalMetadataTableDBRow((JPAAttributeNumericalMetaData) md, this.currentLocale));
				}
			}
		}
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), resultRows.size());
		return new QueryResult(resultRows.subList(constraints.getOffset(), endIndex), resultRows.size());
	}
}