package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values.special;

import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.dbviews.pickers.ModelWizardPicker;
import org.pikater.web.vaadin.gui.server.components.dbviews.pickers.ModelWizardPickerOutput;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options.values.templated.IntegerValueProvider;
import org.vaadin.actionbuttontextfield.ActionButtonTextField;
import org.vaadin.actionbuttontextfield.widgetset.client.ActionButtonType;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.TextField;

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
		 * Add action button to it.
		 */
		ActionButtonTextField tf_value_extension = ActionButtonTextField.extend(tf_value);
		tf_value_extension.getState().type = ActionButtonType.ACTION_SEARCH;
		tf_value_extension.addClickListener(new ActionButtonTextField.ClickListener()
		{
		    @Override
		    public void buttonClick(ActionButtonTextField.ClickEvent clickEvent)
		    {
		    	GeneralDialogs.componentDialog("Search for model", new ModelWizardPicker(ManageAuth.getUserEntity(VaadinSession.getCurrent())), 
		    			new GeneralDialogs.IDialogResultHandler()
				{
					@Override
					public boolean handleResult(Object[] args)
					{
						ModelWizardPickerOutput output = (ModelWizardPickerOutput) args[0];
						tf_value.setValue(String.valueOf(output.getResult().getCreatedModel().getId()));
						return true; // close dialog
					}
				});
		    }
		});
	}
}