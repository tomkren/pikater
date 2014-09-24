package org.pikater.web.unused.welcometour;

import org.pikater.web.vaadin.gui.server.components.wizards.steps.ParentAwareWizardStep;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Step1 extends ParentAwareWizardStep<WelcomeTourCommons, WelcomeTourWizard>
{
	private final Component content;
	
	public Step1(WelcomeTourWizard parentWizard)
	{
		super(parentWizard);
		
		VerticalLayout layout = new VerticalLayout();
		layout.addComponent(new Label("Welcome into the NewPikater setup. This wizard will guide you through the process."));
		layout.addComponent(new Label("Click 'next' to continue"));
		
		this.content = layout;
	}
	
	@Override
	public String getCaption()
	{
		return "Welcome";
	}

	@Override
	public Component getContent()
	{
		return content;
	}

	@Override
	public boolean onAdvance()
	{
		return true;
	}

	@Override
	public boolean onBack()
	{
		return false;
	}
}