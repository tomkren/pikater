package org.pikater.web.vaadin.gui.server.components.wizard;

import org.vaadin.teemu.wizards.Wizard;

public abstract class RefreshableWizardStep<T extends Wizard> extends ParentAwareWizardStep<T>
{
	public RefreshableWizardStep(T parentWizard)
	{
		super(parentWizard);
	}

	public abstract void refresh();
}
