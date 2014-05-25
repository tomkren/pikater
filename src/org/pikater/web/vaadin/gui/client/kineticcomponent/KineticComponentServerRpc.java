package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.shared.experiment.webformat.Experiment;
import org.pikater.shared.experiment.webformat.ExperimentMetadata;

import com.vaadin.shared.communication.ServerRpc;

public interface KineticComponentServerRpc extends ServerRpc
{
	void command_setExperimentModified(boolean modified);
	void command_onLoadCallback(int absoluteX, int absoluteY);
	
	void response_reloadVisualStyle();
	void response_sendExperimentToSave(ExperimentMetadata metadata, Experiment experiment);
}
