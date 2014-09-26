package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.batches;

import org.pikater.shared.database.views.tableview.batches.BatchTableDBViewUser;
import org.pikater.web.vaadin.UserAuth;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.DynamicNeighbourWizardStep;

import com.vaadin.server.VaadinSession;

public class UserBatchesView extends BatchesView
{
	private static final long serialVersionUID = 7065648546694118987L;

	@Override
	protected DynamicNeighbourWizardStep<IWizardCommon, WizardWithDynamicSteps<IWizardCommon>> getFirstStep()
	{
		return new BatchStep(this, new BatchTableDBViewUser(UserAuth.getUserEntity(VaadinSession.getCurrent())));
	}
}