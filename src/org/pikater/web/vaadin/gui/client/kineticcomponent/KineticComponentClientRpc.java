package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.shared.experiment.webformat.client.BoxInfoClient;
import org.pikater.shared.experiment.webformat.client.ExperimentGraphClient;

import com.vaadin.shared.communication.ClientRpc;

public interface KineticComponentClientRpc extends ClientRpc
{
	void command_createBox(BoxInfoClient info);
	void command_receiveExperimentToLoad(ExperimentGraphClient experiment);
	void command_resetKineticEnvironment();
	
	void request_reloadVisualStyle();
}