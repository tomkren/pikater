package org.pikater.web.vaadin.gui.server.components.wizards;

import org.pikater.web.vaadin.gui.server.components.wizards.steps.RefreshableWizardStep;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.Button;

@StyleSheet("wizards.css")
public class WizardWithOutput<T extends IWizardCommon> extends Wizard
{
	private static final long serialVersionUID = -673702186069757491L;
	
	private final T output;
	private boolean refreshActivatedSteps;

	public WizardWithOutput(T output)
	{
		super();
		setSizeUndefined();
		setStyleName("myWizard");
		
		this.output = output;
		this.refreshActivatedSteps = false;
		
		addListener(new WizardProgressListener()
		{
			@Override
			public void wizardCompleted(WizardCompletedEvent event)
			{
			}
			
			@Override
			public void wizardCancelled(WizardCancelledEvent event)
			{
			}
			
			@Override
			public void stepSetChanged(WizardStepSetChangedEvent event)
			{
			}
			
			@Override
			public void activeStepChanged(WizardStepActivationEvent event)
			{
				if(refreshActivatedSteps)
				{
					if(event.getActivatedStep() instanceof RefreshableWizardStep<?, ?>)
					{
						((RefreshableWizardStep<?, ?>) event.getActivatedStep()).refresh();
					}
				}
			}
		});
	}
	
	public T getOutput()
	{
		return output;
	}
	
	public void setRefreshActivatedSteps(boolean refresh)
	{
		this.refreshActivatedSteps = refresh;
	}
	
	public void setContentPadding(boolean enabled)
	{
		if(enabled)
		{
			addStyleName("contentPadding");
		}
		else
		{
			removeStyleName("contentPadding");
		}
	}
	
	protected static void setButtonUsed(Button btn, boolean used)
	{
		btn.setEnabled(used);
		btn.setVisible(used);
	}
}