package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.shared.experiment.webformat.ExperimentGraph;
import org.pikater.web.vaadin.gui.shared.KineticComponentClickMode;

import com.vaadin.shared.communication.ServerRpc;

public interface KineticComponentServerRpc extends ServerRpc
{
	void command_setExperimentModified(boolean modified);
	void command_onLoadCallback(int absoluteX, int absoluteY);
	void command_alterClickMode(KineticComponentClickMode newClickMode);
	void command_openOptionsManager(String[] selectedBoxesAgentIDs);
	
	void response_sendExperimentToSave(ExperimentGraph experiment);
}
