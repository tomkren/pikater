package org.pikater.web.vaadin.gui.server.components.forms;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.base.FormFieldFactory;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class SaveExperimentForm extends CustomFormLayout
{
	private static final long serialVersionUID = -692840139111911571L;
	
	private final TextField experimentNameField;
	private final ComboBox priorityAssignedByUserField;
	private final TextField computationEstimateInHoursField;
	private final ComboBox sendEmailWhenFinishedField;
	private final TextArea experimentNoteField;
	
	public SaveExperimentForm()
	{
		super(null);
		
		JPAUser user = ManageAuth.getUserEntity(VaadinSession.getCurrent());
		
		this.experimentNameField = FormFieldFactory.getGeneralTextField("Experiment name:", "Enter the name", null, false, false);
		
		List<Integer> userPriorityOptions = new ArrayList<Integer>();
		for(int i = 0; i <= user.getPriorityMax(); i++)
		{
			userPriorityOptions.add(i);
		}
		this.priorityAssignedByUserField = FormFieldFactory.getGeneralComboBox("Priority:", userPriorityOptions, user.getPriorityMax(), true, false);
		this.computationEstimateInHoursField = FormFieldFactory.getNumericField("Est. computation time (hours):", new Integer(1), 1, null, true, false);
		this.sendEmailWhenFinishedField = FormFieldFactory.getGeneralCheckField("Send email when finished:", true, false, false);
		this.experimentNoteField = FormFieldFactory.getGeneralTextArea("Notes:", "A short description for this experiment?", null, false, false);
		
		// TODO: add fields to the layout
	}

	@Override
	public IOnSubmit getSubmitAction()
	{
		return null;
	}
	
	public String getExperimentName()
	{
		return experimentNameField.getValue();
	}
	
	public Integer getPriorityAssignedByUser()
	{
		return Integer.parseInt((String) priorityAssignedByUserField.getValue());
	}
	
	public Integer getComputationEstimateInHours()
	{
		return Integer.parseInt(computationEstimateInHoursField.getValue());
	}
	
	public boolean getSendEmailWhenFinished()
	{
		return (Boolean) sendEmailWhenFinishedField.getConvertedValue();
	}
	
	public String getExperimentNote()
	{
		return experimentNoteField.getValue();
	}
}