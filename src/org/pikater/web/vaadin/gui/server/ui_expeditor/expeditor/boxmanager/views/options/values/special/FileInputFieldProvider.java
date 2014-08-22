package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values.special;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBRow;
import org.pikater.shared.database.views.tableview.datasets.DatasetPickingTableDBView;
import org.pikater.web.vaadin.gui.server.components.dbviews.DatasetDBViewRoot;
import org.pikater.web.vaadin.gui.server.components.dbviews.base.tableview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.DialogCommons.IDialogResultPreparer;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values.templated.StringValueProvider;
import org.vaadin.actionbuttontextfield.ActionButtonTextField;
import org.vaadin.actionbuttontextfield.widgetset.client.ActionButtonType;

import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class FileInputFieldProvider extends StringValueProvider
{
	@Override
	protected void doGenerateFields(Value value)
	{
		/*
		 * Generates an appropriate text field.
		 */
		super.doGenerateFields(value);
		
		/*
		 * Get the text field.
		 */
		final TextField tf_value = (TextField) getGeneratedFields().entrySet().iterator().next().getValue();
		
		/*
		 * - Add action button it.
		 * - Don't disable user editing - some datasets may be hard to find and user input
		 * may be handy. 
		 */
		ActionButtonTextField tf_value_extension = ActionButtonTextField.extend(tf_value);
		tf_value_extension.getState().type = ActionButtonType.ACTION_SEARCH;
		tf_value_extension.addClickListener(new ActionButtonTextField.ClickListener()
		{
		    @Override
		    public void buttonClick(ActionButtonTextField.ClickEvent clickEvent)
		    {
		    	GeneralDialogs.componentDialog("Search for input dataset", new DatasetPickerComponent("Select a row and click 'Ok':"), new GeneralDialogs.IDialogResultHandler()
				{
					@Override
					public boolean handleResult(Object[] args)
					{
						DataSetTableDBRow row = (DataSetTableDBRow) args[0];
						tf_value.setValue(row.getDataset().getFileName());
						return true; // close dialog
					}
				});
		    }
		});
	}
	
	private class DatasetPickerComponent extends DBTableLayout implements IDialogResultPreparer
	{
		private static final long serialVersionUID = 9055067769093710286L;

		public DatasetPickerComponent(String caption)
		{
			setSizeUndefined();
			setReadOnly(true);
			getTable().setMultiSelect(false);
			setView(new DatasetDBViewRoot<DatasetPickingTableDBView>(new DatasetPickingTableDBView()));
			addComponentAsFirst(new Label(caption));
		}
		
		@Override
		public boolean isResultReadyToBeHandled()
		{
			return getTable().isARowSelected();
		}

		@Override
		public void addArgs(List<Object> arguments)
		{
			arguments.add(getTable().getViewsOfSelectedRows()[0]);
		}
	}
}