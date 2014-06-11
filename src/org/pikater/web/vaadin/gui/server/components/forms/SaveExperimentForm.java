package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.forms.abstractform.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormCheckField;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormFieldGenerator;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormTextArea;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormTextField;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button.ClickListener;

public class SaveExperimentForm extends CustomFormLayout
{
	private static final long serialVersionUID = -692840139111911571L;
	
	private final FormTextField experimentNameField;
	private final FormTextField priorityAssignedByUserField;
	private final FormTextField computationEstimateInHoursField;
	private final FormCheckField sendEmailWhenFinishedField;
	private final FormTextArea experimentNoteField;
	
	public SaveExperimentForm()
	{
		super(null);
		
		this.experimentNoteField = new FormTextArea("Notes:", "A short description for this experiment?", null, false, false);
		this.experimentNameField = FormFieldGenerator.getGeneralStringField("Experiment name:", "Enter the name", null, false, false);
		this.priorityAssignedByUserField = FormFieldGenerator.getUserPriorityForExperimentField(ManageAuth.getUserEntity(VaadinSession.getCurrent()));
		this.computationEstimateInHoursField = FormFieldGenerator.getComputationEstimateInHoursField();
		this.sendEmailWhenFinishedField = FormFieldGenerator.getSendEmailWhenFinished();
	}

	@Override
	public ClickListener getActionButtonListener()
	{
		return null;
	}
	
	public String getExperimentName()
	{
		return experimentNameField.getValue();
	}
	
	public Integer getPriorityAssignedByUser()
	{
		return Integer.parseInt(priorityAssignedByUserField.getValue());
	}
	
	public Integer getComputationEstimateInHours()
	{
		return Integer.parseInt(computationEstimateInHoursField.getValue());
	}
	
	public boolean getSendEmailWhenFinished()
	{
		return sendEmailWhenFinishedField.getValue();
	}
	
	public String getExperimentNote()
	{
		return experimentNoteField.getValue();
	}
}
