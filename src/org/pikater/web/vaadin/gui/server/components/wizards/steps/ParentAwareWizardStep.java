package org.pikater.web.vaadin.gui.server.components.wizards.steps;

import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithOutput;
import org.vaadin.teemu.wizards.WizardStep;

public abstract class ParentAwareWizardStep<OUTPUT extends IWizardCommon, WIZARD extends WizardWithOutput<OUTPUT>> implements WizardStep
{
	private final WIZARD parentWizard;

	public ParentAwareWizardStep(WIZARD parentWizard)
	{
		this.parentWizard = parentWizard;
	}
	
	public WIZARD getParentWizard()
	{
		return parentWizard;
	}
	
	public OUTPUT getOutput()
	{
		return parentWizard.getOutput();
	}
}