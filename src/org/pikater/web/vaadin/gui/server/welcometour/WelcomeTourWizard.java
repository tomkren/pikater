package org.pikater.web.vaadin.gui.server.welcometour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.pikater.shared.RemoteServerInfo;
import org.pikater.shared.TopologyModel;
import org.pikater.shared.TopologyModel.ServerType;
import org.vaadin.teemu.wizards.Wizard;
import org.vaadin.teemu.wizards.WizardStep;
import org.vaadin.teemu.wizards.event.WizardCancelledEvent;
import org.vaadin.teemu.wizards.event.WizardCompletedEvent;
import org.vaadin.teemu.wizards.event.WizardProgressListener;
import org.vaadin.teemu.wizards.event.WizardStepActivationEvent;
import org.vaadin.teemu.wizards.event.WizardStepSetChangedEvent;

import com.vaadin.ui.Button;

@SuppressWarnings("serial")
public class WelcomeTourWizard extends Wizard
{
	// fields for child steps
	private final Map<String, TopologyModel> parsedModels;
	private Collection<RemoteServerInfoItem> wrappedModels;
	private boolean allModelsParsed;
	
	public WelcomeTourWizard(Button.ClickListener onFinish)
	{
		super();
		
		this.parsedModels = new HashMap<String, TopologyModel>();
		this.wrappedModels = null;
		this.allModelsParsed = false;
		
		// add individual wizard steps, one by one
		addStep(new Step1(this));
		addStep(new Step2(this)); // Step2 needs to be added before Step3 - don't break the call order
		addStep(new Step3(this));
		
		addListener(new WizardProgressListener()
		{
			@Override
			public void wizardCompleted(WizardCompletedEvent event)
			{
				// no action needed
			}
			
			@Override
			public void wizardCancelled(WizardCancelledEvent event)
			{
				// the cancel button is disabled
			}
			
			@Override
			public void stepSetChanged(WizardStepSetChangedEvent event)
			{
				// no action needed
			}
			
			@Override
			public void activeStepChanged(WizardStepActivationEvent event)
			{
				WelcomeTourWizardStep step = (WelcomeTourWizardStep) event.getActivatedStep();
				step.refresh();
				// System.out.println("activeStepChanged: " + event.getActivatedStep().getCaption());
			}
		});

		// define some event handlers
		getCancelButton().setEnabled(false);
		getFinishButton().addClickListener(onFinish);
	}
	
	public void addParsedModel(String topologyName, TopologyModel model)
	{
		if(!allModelsParsed)
		{
			parsedModels.put(topologyName, model);
		}
	}
	
	public void allModelsParsedCallback()
	{
		// actually close adding new topologies
		allModelsParsed = true;
		
		// wrap the parsed topologies into properties
		wrappedModels = new ArrayList<RemoteServerInfoItem>();
		for(Entry<String, TopologyModel> entry : parsedModels.entrySet())
		{
			for(RemoteServerInfo server : entry.getValue().getMasters())
			{
				wrappedModels.add(new RemoteServerInfoItem(entry.getKey(), ServerType.MASTER, server));
			}
			for(RemoteServerInfo server : entry.getValue().getSlaves())
			{
				wrappedModels.add(new RemoteServerInfoItem(entry.getKey(), ServerType.SLAVE, server));
			}
		}
	}
	
	public Map<String, TopologyModel> getParsedModels()
	{
		return parsedModels;
	}
	
	public Collection<RemoteServerInfoItem> getWrappedModels()
	{
		return wrappedModels;
	}
	
	public boolean isLastStep(WizardStep step)
	{
		return super.isLastStep(step);
	}
}
