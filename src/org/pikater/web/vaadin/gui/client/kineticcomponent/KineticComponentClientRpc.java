package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.web.experiment.client.BoxInfoClient;
import org.pikater.web.experiment.client.ExperimentGraphClient;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.IKineticComponent;

import com.vaadin.shared.communication.ClientRpc;

public interface KineticComponentClientRpc extends ClientRpc, IKineticComponent
{
	void receiveExperimentToLoad(ExperimentGraphClient experiment);
	void createBox(BoxInfoClient info);
}