package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values.special;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.shared.database.views.tableview.datasets.DataSetTableDBRow;
import org.pikater.web.vaadin.gui.server.components.dbviews.special.DatasetPickerComponent;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values.templated.IntegerValueProvider;
import org.vaadin.actionbuttontextfield.ActionButtonTextField;
import org.vaadin.actionbuttontextfield.widgetset.client.ActionButtonType;

import com.vaadin.ui.TextField;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;

public class CAModelFieldProvider extends IntegerValueProvider
{
	@Override
	protected void doGenerateFields(Value value)
	{
		/*
		 * Generates an appropriate integer field.
		 */
		super.doGenerateFields(value);
		
		/*
		 * Get the text field.
		 */
		final TextField tf_value = (TextField) getGeneratedFields().entrySet().iterator().next().getValue();
		
		/*
		 * Disable user editing.
		 */
		
		
		/*
		 * Add a validator that doesn't allow 0 as argument.
		 */
		
		
		/*
		 * Add action button to it.
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
}