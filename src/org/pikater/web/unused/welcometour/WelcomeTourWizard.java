package org.pikater.web.unused.welcometour;

import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithOutput;

import com.vaadin.ui.Button;

public class WelcomeTourWizard extends WizardWithOutput<WelcomeTourCommons>
{
	private static final long serialVersionUID = 8369742100965859153L;
	
	public WelcomeTourWizard(Button.ClickListener onFinish)
	{
		super(new WelcomeTourCommons());
		setRefreshActivatedSteps(true);
		
		// add individual wizard steps, one by one
		addStep(new Step1(this));
		addStep(new Step2(this));
		addStep(new Step3(this));
		
		// define some event handlers
		getCancelButton().setEnabled(false);
		getFinishButton().addClickListener(onFinish);
	}
}