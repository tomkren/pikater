package org.pikater.shared.database.views.tableview.datasets.metadata;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAAttributeCategoricalMetaData;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.AbstractTableDBView;
import org.pikater.shared.util.LocaleUtils;

/**
 * A generic view for tables displaying categorical metadata information for a dataset.  
 */
public class CategoricalMetaDataTableDBView extends AbstractTableDBView {
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 */
	public enum Column implements ITableColumn {
		/*
		 * First the read-only properties.
		 */
		NAME, IS_TARGET, CATEGORY_COUNT, RATIO_OF_MISSING_VALUES, ENTROPY, CLASS_ENTROPY;

		@Override
		public String getDisplayName() {
			switch (this) {
			case RATIO_OF_MISSING_VALUES:
				return "MISSING_VALUES_%";
			case CATEGORY_COUNT:
				return "CATEGORIES";
			default:
				return name();
			}
		}

		@Override
		public DBViewValueType getColumnType() {
			switch (this) {
			case IS_TARGET:
				return DBViewValueType.BOOLEAN;

			case NAME:
			case CATEGORY_COUNT:
			case RATIO_OF_MISSING_VALUES:
			case ENTROPY:
			case CLASS_ENTROPY:
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
		return getAllColumns();
	}

	@Override
	public ITableColumn getDefaultSortOrder() {
		return Column.NAME;
	}

	JPADataSetLO dslo = null;
	Locale currentLocale;

	/** 
	 * Constructor.
	 * Using this constructor values will be formatted using default locale 
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 */
	public CategoricalMetaDataTableDBView(JPADataSetLO datasetlo) {
		this.dslo = datasetlo;
		this.currentLocale = LocaleUtils.getDefaultLocale();
	}

	/**
	 * Constructor if one wants to add custom locale information
	 * @param datasetlo The dataset for which we want to list numerical metadata
	 * @param locale Locale used for value formatting
	 */
	public CategoricalMetaDataTableDBView(JPADataSetLO datasetlo, Locale locale) {
		this.dslo = datasetlo;
		this.currentLocale = locale;
	}

	@Override
	protected QueryResult queryUninitializedRows(QueryConstraints constraints) {
		List<CategoricalMetadataTableDBRow> resultRows = new ArrayList<CategoricalMetadataTableDBRow>();
		if (dslo != null) {
			for (JPAAttributeMetaData md : dslo.getAttributeMetaData()) {
				if (md instanceof JPAAttributeCategoricalMetaData) {
					resultRows.add(new CategoricalMetadataTableDBRow((JPAAttributeCategoricalMetaData) md, this.currentLocale));
				}
			}
		}
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), resultRows.size());
		return new QueryResult(resultRows.subList(constraints.getOffset(), endIndex), resultRows.size());
	}
}