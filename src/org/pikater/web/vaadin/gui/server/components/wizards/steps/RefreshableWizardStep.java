package org.pikater.web.vaadin.gui.server.components.wizards.steps;

import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithOutput;

public abstract class RefreshableWizardStep<OUTPUT extends IWizardCommon, WIZARD extends WizardWithOutput<OUTPUT>> extends ParentAwareWizardStep<OUTPUT, WIZARD>
{
	public RefreshableWizardStep(WIZARD parentWizard)
	{
		super(parentWizard);
	}

	public abstract void refresh();
}
