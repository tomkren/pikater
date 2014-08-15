package org.pikater.web.vaadin.gui.server.components.wizards.steps;

import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithDynamicSteps;

public abstract class DynamicNeighbourWizardStep<OUTPUT extends IWizardCommon, WIZARD extends WizardWithDynamicSteps<OUTPUT>> extends ParentAwareWizardStep<OUTPUT, WIZARD>
{
	private final boolean isLeaf;
	
	public DynamicNeighbourWizardStep(WIZARD parentWizard, boolean isLeaf)
	{
		super(parentWizard);
		
		this.isLeaf = isLeaf;
	}
	
	//----------------------------------------------------
	// INHERITED INTERFACE
	
	@Override
	public boolean onBack()
	{
		return true;
	}
	
	@Override
	public final boolean onAdvance()
	{
		/*
		 * This is a "system" method, called in ActiveStepChanged event etc.
		 * Unless you want to suffer from heap out of memory error, don't add or activate steps in here.
		 */
		return true;
	}
	
	//----------------------------------------------------
	// OTHER PUBLIC INTERFACE
	
	public boolean isLeaf()
	{
		return isLeaf;
	}
	
	//----------------------------------------------------
	// ABSTRACT INTERFACE
	
	public abstract DynamicNeighbourWizardStep<OUTPUT, WIZARD> constructNextStep();
}