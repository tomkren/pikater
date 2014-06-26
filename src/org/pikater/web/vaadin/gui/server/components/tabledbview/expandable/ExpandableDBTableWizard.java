package org.pikater.web.vaadin.gui.server.components.tabledbview.expandable;

import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.ui.Button;

public class ExpandableDBTableWizard extends Wizard
{
	private static final long serialVersionUID = 2459471148804635291L;

	public ExpandableDBTableWizard()
	{
		super();
		
		// make all control buttons invisible and disabled, only enable the back button later when a secondary step is added
		setButtonEnabled(getBackButton(), false);
		setButtonEnabled(getNextButton(), false);
		setButtonEnabled(getFinishButton(), false);
		setButtonEnabled(getCancelButton(), false);

		addListener(new WizardProgressListener()
		{
			@Override
			public void wizardCompleted(WizardCompletedEvent event)
			{
				// finish button is not visible and enabled
			}

			@Override
			public void wizardCancelled(WizardCancelledEvent event)
			{
				// cancel button is not visible and enabled
			}

			@Override
			public void stepSetChanged(WizardStepSetChangedEvent event)
			{
				// everything important is done in the following method:
			}

			@Override
			public void activeStepChanged(WizardStepActivationEvent event)
			{
				if(!isLastStep(event.getActivatedStep()))
				{
					// remove the last step
					steps.remove(steps.size() - 1);
				}
				setButtonEnabled(getBackButton(), !isFirstStep(event.getActivatedStep()));
			}
		});
	}
	
	public ExpandableDBTableLayout addStep(AbstractTableDBView dbView)
	{
		ExpandableDBTableLayout result = new ExpandableDBTableLayout(dbView, new IDBTableLayoutOwnerExpandable()
		{
			@Override
			public void switchToView(AbstractTableDBView dbView)
			{
				activateStep(addStep(dbView));
			}
		}, false); // TODO: immediate or not?
		super.addStep(result);
		return result;
	}
	
	private static void setButtonEnabled(Button btn, boolean enabled)
	{
		btn.setEnabled(enabled);
		btn.setVisible(enabled);
	}
}