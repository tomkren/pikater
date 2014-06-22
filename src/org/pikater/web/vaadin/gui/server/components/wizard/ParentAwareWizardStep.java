package org.pikater.web.vaadin.gui.server.components.wizard;

import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;

public abstract class ParentAwareWizardStep<T extends Wizard> implements WizardStep
{
	private final T parentWizard;

	public ParentAwareWizardStep(T parentWizard)
	{
		this.parentWizard = parentWizard;
	}
	
	public T getParentWizard()
	{
		return parentWizard;
	}
}