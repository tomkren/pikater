package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user;

import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.DynamicNeighbourWizardStep;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.BatchesView;

import com.vaadin.server.VaadinSession;

public class UserBatchesView extends BatchesView
{
	private static final long serialVersionUID = 7065648546694118987L;

	@Override
	protected DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> getFirstStep()
	{
		return new BatchStep(this, ManageAuth.getUserEntity(VaadinSession.getCurrent()));
	}
}