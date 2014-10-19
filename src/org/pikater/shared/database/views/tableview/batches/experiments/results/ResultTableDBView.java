package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.AbstractTableDBView;

/**
 * A generic view for tables displaying result errors and statistic.  
 */
public abstract class ResultTableDBView extends AbstractTableDBView {
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 */
	public enum Column implements ITableColumn {
		/*
		 * First the read-only properties.
		 */
		AGENT_NAME, WEKA_OPTIONS, ERROR_RATE, KAPPA, REL_ABS_ERR, MEAN_ABS_ERR, ROOT_REL_SQR_ERR, ROOT_MEAN_SQR_ERR, NOTE, TRAINED_MODEL, VISUALIZE, COMPARE;

		@Override
		public String getDisplayName() {
			switch (this) {
			case AGENT_NAME:
				return "AGENT";
			default:
				return name();
			}
		}

		@Override
		public DBViewValueType getColumnType() {
			switch (this) {
			case AGENT_NAME:
			case WEKA_OPTIONS:
			case NOTE:
			case ERROR_RATE:
			case KAPPA:
			case MEAN_ABS_ERR:
			case REL_ABS_ERR:
			case ROOT_MEAN_SQR_ERR:
			case ROOT_REL_SQR_ERR:
				return DBViewValueType.STRING;

			case TRAINED_MODEL:
			case VISUALIZE:
			case COMPARE:
				return DBViewValueType.NAMED_ACTION;

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
		result.remove(Column.AGENT_NAME);
		result.remove(Column.WEKA_OPTIONS);
		result.remove(Column.NOTE);
		result.remove(Column.TRAINED_MODEL);
		result.remove(Column.VISUALIZE);
		result.remove(Column.COMPARE);
		return result;
	}

	@Override
	public ITableColumn getDefaultSortOrder() {
		return Column.ERROR_RATE;
	}
}