package org.pikater.web.vaadin.gui.server.components.forms;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.base.FormFieldFactory;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs.IDialogComponent;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public abstract class SaveExperimentForm extends CustomFormLayout implements IDialogComponent
{
	private static final long serialVersionUID = -692840139111911571L;
	
	public enum ExperimentSaveMode
	{
		SAVE_FOR_LATER,
		SAVE_FOR_EXECUTION
	}
	
	private final ExperimentSaveMode saveMode;
	private final TextField experimentNameField;
	private final ComboBox priorityAssignedByUserField;
	private final TextField computationEstimateInHoursField;
	private final ComboBox sendEmailWhenFinishedField;
	private final TextArea experimentNoteField;
	
	public SaveExperimentForm(ExperimentSaveMode saveMode)
	{
		super(null);
		
		JPAUser user = ManageAuth.getUserEntity(VaadinSession.getCurrent());
		
		// first create the fields
		this.saveMode = saveMode;
		this.experimentNameField = FormFieldFactory.getGeneralTextField("Experiment name:", "Enter the name", null, false, false);
		this.experimentNoteField = FormFieldFactory.getGeneralTextArea("Note:", "A short description for this experiment?", null, false, false);
		switch(saveMode)
		{
			case SAVE_FOR_LATER:
				this.priorityAssignedByUserField = null;
				this.computationEstimateInHoursField = null;
				this.sendEmailWhenFinishedField = null;
				break;
				
			case SAVE_FOR_EXECUTION:
				List<Integer> userPriorityOptions = new ArrayList<Integer>();
				for(int i = 0; i <= user.getPriorityMax(); i++)
				{
					userPriorityOptions.add(i);
				}
				this.priorityAssignedByUserField = FormFieldFactory.getGeneralComboBox("Priority:", userPriorityOptions, user.getPriorityMax(), true, false);
				this.computationEstimateInHoursField = FormFieldFactory.getNumericField("Est. computation time (hours):", new Integer(1), 1, null, true, false);
				this.sendEmailWhenFinishedField = FormFieldFactory.getGeneralCheckField("Send email when finished:", true, false, false);
				break;
				
			default:
				throw new IllegalStateException("Unknown state: " + saveMode.name());
		}
		
		// and then add the fields with the right order
		addField("name", experimentNameField);
		if(saveMode == ExperimentSaveMode.SAVE_FOR_EXECUTION)
		{
			addField("priority", priorityAssignedByUserField);
			addField("computation estimate", computationEstimateInHoursField);
			addField("email when finished", sendEmailWhenFinishedField);
		}
		addField("note", experimentNoteField);
	}

	@Override
	public IOnSubmit getSubmitAction()
	{
		return null;
	}
	
	public ExperimentSaveMode getSaveMode()
	{
		return saveMode;
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
	
	//--------------------------------------------------------------------
	// METHODS DEFINING THIS FORM'S BEHAVIOUR AS A PART OF A DIALOG

	@Override
	public void addArgs(List<Object> arguments)
	{
		switch(saveMode)
		{
			case SAVE_FOR_LATER: // only some information needs to be provided
				arguments.add(getExperimentName());
				arguments.add(getExperimentNote());
				break;
			case SAVE_FOR_EXECUTION: // full information needs to be provided
				arguments.add(getExperimentName());
				arguments.add(getPriorityAssignedByUser());
				arguments.add(getComputationEstimateInHours());
				arguments.add(getSendEmailWhenFinished());
				arguments.add(getExperimentNote());
				break;
			default:
				throw new IllegalStateException("Unknown state: " + saveMode.name());
		}
	}
}