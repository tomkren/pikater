package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.shared.experiment.webformat.client.BoxInfoClient;
import org.pikater.shared.experiment.webformat.client.ExperimentGraphClient;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.IKineticComponent;

import com.vaadin.shared.communication.ClientRpc;

public interface KineticComponentClientRpc extends ClientRpc, IKineticComponent
{
	void receiveExperimentToLoad(ExperimentGraphClient experiment);
	void createBox(BoxInfoClient info);
}