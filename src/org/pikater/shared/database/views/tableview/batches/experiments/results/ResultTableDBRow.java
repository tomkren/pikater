package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.Locale;

import org.pikater.core.ontology.subtrees.newoption.NewOptions;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.util.LocaleUtils;

public class ResultTableDBRow extends AbstractTableRowDBView {
	private JPAResult result;
	private JPADataSetLO firstValidInput;
	private JPADataSetLO firstValidOutput;
	private Locale currentLocale;

	public ResultTableDBRow(JPAResult result) {
		this.result = result;
		this.firstValidInput = null;
		this.firstValidOutput = null;
		this.currentLocale = LocaleUtils.getDefaultLocale();
	}

	public JPAResult getResult() {
		return result;
	}

	public JPADataSetLO getFirstValidInput() {
		return firstValidInput;
	}

	public JPADataSetLO getFirstValidOutput() {
		return firstValidOutput;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final ITableColumn column) {
		ResultTableDBView.Column specificColumn = (ResultTableDBView.Column) column;
		switch (specificColumn) {
		/*
		 * First the read-only properties.
		 */
		case AGENT_NAME:
			return new StringReadOnlyDBViewValue(result.getAgentName().substring(result.getAgentName().lastIndexOf(".") + 1));
		case WEKA_OPTIONS:
			return new StringReadOnlyDBViewValue(NewOptions.importXML(result.getOptions()).exportToWeka());
		case NOTE:
			return new StringReadOnlyDBViewValue(result.getNote());
		case ERROR_RATE:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getErrorRate()));
		case KAPPA:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getKappaStatistic()));
		case MEAN_ABS_ERR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getMeanAbsoluteError()));
		case REL_ABS_ERR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRelativeAbsoluteError()));
		case ROOT_MEAN_SQR_ERR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRootMeanSquaredError()));
		case ROOT_REL_SQR_ERR:
			return new StringReadOnlyDBViewValue(LocaleUtils.formatDouble(currentLocale, result.getRootRelativeSquaredError()));

			/*
			 * And then custom actions.
			 */
		case TRAINED_MODEL:
			return new NamedActionDBViewValue("Download") {
			// no DB changes needed - this is completely GUI managed
				@Override
				public boolean isEnabled() {
					return result.getCreatedModel() != null;
				}

				@Override
				public void updateEntities() {
				}

				@Override
				protected void commitEntities() {
				}
			};
		case VISUALIZE:
			return new NamedActionDBViewValue("Visualize") {
			// no DB changes needed - this is completely GUI managed
				@Override
				public boolean isEnabled() {
					if (result.hasAnOutput()) {
						setValidOutput();
						return firstValidOutput != null;
					} else {
						return false;
					}
				}

				@Override
				public void updateEntities() {
				}

				@Override
				protected void commitEntities() {
				}
			};
		case COMPARE:
			return new NamedActionDBViewValue("Compare") {
			// no DB changes needed - this is completely GUI managed
				@Override
				public boolean isEnabled() {
					if (result.hasAnInput() && result.hasAnOutput()) {
						setValidInput();
						setValidOutput();
						return (firstValidInput != null) && (firstValidOutput != null);
					} else {
						return false;
					}
				}

				@Override
				public void updateEntities() {
				}

				@Override
				protected void commitEntities() {
				}
			};

		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow() {
	}

	//------------------------------------------------------------
	// SPECIAL PRIVATE INTERFACE

	private void setValidInput() {
		for (JPADataSetLO dataset : result.getInputs()) {
			if (dataset.hasComputedMetadata()) {
				firstValidInput = dataset;
				return;
			}
		}
		firstValidInput = null;
	}

	private void setValidOutput() {
		for (JPADataSetLO dataset : result.getOutputs()) {
			if (dataset.hasComputedMetadata()) {
				firstValidOutput = dataset;
				return;
			}
		}
		firstValidOutput = null;
	}
}