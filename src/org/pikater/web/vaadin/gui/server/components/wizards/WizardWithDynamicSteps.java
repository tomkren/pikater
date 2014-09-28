package org.pikater.web.vaadin.gui.server.components.wizards;

import org.pikater.web.vaadin.gui.server.components.wizards.steps.DynamicNeighbourWizardStep;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * Unlike {@link WizardWithOutput} which requires statically defined
 * steps, this one allows for each step to construct the next step
 * dynamically.
 * 
 * @author SkyCrawl
 *
 * @param <T> The wizard's "state" class.
 */
public abstract class WizardWithDynamicSteps<T extends IWizardCommon> extends WizardWithOutput<T>
{
	private static final long serialVersionUID = 6111242222755309996L;
	
	public WizardWithDynamicSteps(T output, boolean cancelButtonUsable)
	{
		super(output);
		addStyleName("dynamicWizard");
		setButtonUsed(getFinishButton(), false);
		if(!cancelButtonUsable)
		{
			setButtonUsed(getCancelButton(), false);
		}
		
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
				if(!isLastStep(currentStep))
				{
					activateStep(steps.get(steps.size() - 1));
				}
			}
			
			@Override
			public void activeStepChanged(WizardStepActivationEvent event)
			{
				@SuppressWarnings("unchecked")
				DynamicNeighbourWizardStep<T, WizardWithDynamicSteps<T>> activatedStep = 
						(DynamicNeighbourWizardStep<T, WizardWithDynamicSteps<T>>) event.getActivatedStep(); 
				
				if(!isLastStep(activatedStep))
				{
					steps.remove(steps.size() - 1); // remove the last step
				}
				getBackButton().setEnabled(!isFirstStep(activatedStep));
				getNextButton().setVisible(!activatedStep.isLeaf()); // except leaf wizard steps
			}
		});
		
		getNextButton().addClickListener(new Button.ClickListener()
		{
			private static final long serialVersionUID = -6508154321218032779L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				advance(getCurrentStep().constructNextStep());
			}
		});
	}
	
	protected boolean canAdvance()
	{
		return !getCurrentStep().isLeaf();
	}
	
	public void advance(DynamicNeighbourWizardStep<T, WizardWithDynamicSteps<T>> newStep)
	{
		if(canAdvance())
		{
			if(newStep != null)
			{
				getNextButton().setEnabled(false); // always disable on advance - new steps are created
				addStep(newStep);
			}
			// activateStep(newStep);
		}
		else
		{
			throw new IllegalStateException("The current step has been declared as leaf. Can not expand.");
		}
	}
	
	/**
	 * A must-call from child classes. Alternatively, call the following:
	 * <pre>
	 * addStep(getFirstStep());
	 * </pre>
	 */
	protected void finishInit()
	{
		addStep(createFirstStep());
	}
	
	@Override
	public void addStep(WizardStep step)
	{
		// a lot of this class's code depends on the following "filter":
		if(step instanceof DynamicNeighbourWizardStep)
		{
			super.addStep(step);
		}
		else
		{
			throw new IllegalArgumentException("The argument step does not extend {@link DynamicNeighbourWizardStep}.");
		}
	}
	
	//----------------------------------------------------
	// A LITTLE SPECIAL SOMETHING
	
	@SuppressWarnings("unchecked")
	protected DynamicNeighbourWizardStep<T, WizardWithDynamicSteps<T>> getCurrentStep()
	{
		// the {@link addStep} method ensures steps are of the correct type
		return (DynamicNeighbourWizardStep<T, WizardWithDynamicSteps<T>>) currentStep;
	}
	
	/*
	protected void forceNextStepTransitionNow()
	{
		if(getCurrentStep().isLeaf())
		{
			throw new UnsupportedOperationException("Can not advance because the current step is declared as leaf.");
		}
		else
		{
			addStep(getCurrentStep().constructNextStep());
		}
	}
	*/
	
	/**
	 * Return the first dynamically constructed step.
	 * @return
	 */
	protected abstract DynamicNeighbourWizardStep<T, WizardWithDynamicSteps<T>> createFirstStep();
}