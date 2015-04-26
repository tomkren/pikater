package org.pikater.shared.database.views.tableview.datasets.metadata;

import java.util.Locale;

import org.pikater.shared.database.jpa.JPAAttributeNumericalMetaData;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.BooleanReadOnlyDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.util.LocaleUtils;

public class NumericalMetadataTableDBRow extends AbstractTableRowDBView {

	private JPAAttributeNumericalMetaData attrNum = null;
	private Locale currentLocale;

	public NumericalMetadataTableDBRow(JPAAttributeNumericalMetaData attrNum, Locale locale) {
		this.attrNum = attrNum;
		this.currentLocale = locale;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column) {
		NumericalMetaDataTableDBView.Column specificColumn = (NumericalMetaDataTableDBView.Column) column;
		switch (specificColumn) {
		/*
		 * First the read-only properties.
		 */
		case NAME:
			return new StringReadOnlyDBViewValue(attrNum.getName());
		case AVERAGE:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getAvarage()));
		case IS_REAL:
			return new BooleanReadOnlyDBViewValue(attrNum.getIsReal());
		case IS_TARGET:
			return new BooleanReadOnlyDBViewValue(attrNum.isTarget());
		case MAXIMUM:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getMax()));
		case MEDIAN:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getMedian()));
		case MINIMUM:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getMin()));
			/*
			case MODE:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale,attrNum.getMode()));
			*/
		case RATIO_OF_MISSING_VALUES:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getRatioOfMissingValues()));
		case VARIANCE:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getVariance()));
		case STD_DEVIATION:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getStandardDeviation()));
		case CHI_SQUARE_NORM:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getChiSquareNormalD()));
		case CHI_SQUARE_NORM_T:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getChiSquareTestNormalD()));
		case G_TEST:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getgTestNormalD()));
		case QUARTILE_1:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getQ1()));
		case QUARTILE_2:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getQ2()));
		case QUARTILE_3:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getQ3()));
		case SKEWNESS:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getSkewness()));
		case KURTOSIS:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getKurtosis()));	
		case ENTROPY:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getEntropy()));
		case CLASS_ENTROPY:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, attrNum.getClassEntropy()));

		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow() {
	}
}
