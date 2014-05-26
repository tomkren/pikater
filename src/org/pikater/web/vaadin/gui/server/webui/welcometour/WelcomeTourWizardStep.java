package org.pikater.web.vaadin.gui.server.webui.welcometour;

import org.vaadin.teemu.wizards.WizardStep;

public abstract class WelcomeTourWizardStep implements WizardStep
{
	protected final WelcomeTourWizard parentWizard;

	public WelcomeTourWizardStep(WelcomeTourWizard parentWizard)
	{
		this.parentWizard = parentWizard;
	}
	
	public abstract void refresh();
}
