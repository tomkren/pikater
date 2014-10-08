package org.pikater.web.vaadin.gui.server.components.wizards.steps;

import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithOutput;

/**
 * Wizard step for general use (see {@link ParentAwareWizardStep}), that
 * may be refreshed when activated (if the parent wizard is set to do so).
 * 
 * @author SkyCrawl
 *
 */
public abstract class RefreshableWizardStep<OUTPUT extends IWizardCommon, WIZARD extends WizardWithOutput<OUTPUT>> extends ParentAwareWizardStep<OUTPUT, WIZARD>
{
	public RefreshableWizardStep(WIZARD parentWizard)
	{
		super(parentWizard);
	}

	public abstract void refresh();
}
