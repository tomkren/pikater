package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.Experiment;
import org.pikater.shared.experiment.webformat.ExperimentMetadata;

import com.vaadin.shared.communication.ClientRpc;

public interface KineticComponentClientRpc extends ClientRpc
{
	void command_createBox(BoxInfo info, int posX, int posY);
	void command_receiveExperimentToLoad(Experiment experiment);
	void command_resetKineticEnvironment();
	
	void request_reloadVisualStyle();
	void request_sendExperimentToSave(ExperimentMetadata metadata);
}