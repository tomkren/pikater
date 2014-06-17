package org.pikater.shared.database.views.jirka.datasets;

import org.pikater.shared.AppHelper;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAGlobalMetaData;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.values.AbstractDBViewValue;
import org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue;

public class DataSetTableRow extends AbstractTableRowDBView {

	private JPADataSetLO dataset=null;

	public DataSetTableRow(JPADataSetLO dataset)
	{
		this.dataset=dataset;
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(final IColumn column)
	{
		DataSetTableView.Column specificColumn = (DataSetTableView.Column) column;
		switch(specificColumn)
		{
		/*
		 * First the read-only properties.
		 */
		case DESCRIPTION:
			return new StringDBViewValue(dataset.getDescription(), true)
			{
				@Override
				protected void commitValue()
				{
				}
			};
		case NUMBER_OF_INSANCES:
			return new StringDBViewValue(dataset.getGlobalMetaData()!=null?""+dataset.getGlobalMetaData().getNumberofInstances():"N/A", true)
			{
				@Override
				protected void commitValue()
				{
				}
			};
		case DEFAULT_TASK_TYPE:
			JPAGlobalMetaData gmd=dataset.getGlobalMetaData();
			if(gmd!=null){
				return new StringDBViewValue(gmd.getDefaultTaskType()!=null? gmd.getDefaultTaskType().getName() : "N/A", true)
				{
					@Override
					protected void commitValue()
					{
					}
				};
			}else{
				//the global metadata for this dataset is not available
				return new StringDBViewValue("N/A", true)
				{
					@Override
					protected void commitValue()
					{
					}
				};
			}
		case SIZE:
			return new StringDBViewValue(AppHelper.formatFileSize(dataset.getSize()), true) {

				@Override
				protected void commitValue() {
					// TODO Auto-generated method stub

				}
			};
		case CREATED:
			return new StringDBViewValue(""+dataset.getCreated(),true) {

				@Override
				protected void commitValue() {
					// TODO Auto-generated method stub

				}
			};
		case OWNER:
			return new StringDBViewValue(dataset.getOwner().getLogin(),true) {

				@Override
				protected void commitValue() {
					// TODO Auto-generated method stub

				}
			};


		default:
			throw new IllegalStateException("Unknown column: " + specificColumn.name());
		}
	}

	@Override
	public void commitRow() {
		// TODO Auto-generated method stub
	}
}
