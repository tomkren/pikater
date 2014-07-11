package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.shared.experiment.webformat.ExperimentGraph;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.ClickMode;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.AbstractGraphItemShared.RegistrationOperation;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.BoxGraphItemShared;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.EdgeGraphItemShared;

import com.vaadin.shared.communication.ServerRpc;

public interface KineticComponentServerRpc extends ServerRpc
{
	// general commands
	void command_onLoadCallback(int absoluteX, int absoluteY);
	void command_setExperimentModified(boolean modified);
	void command_alterClickMode(ClickMode newClickMode);
	
	// experiment graph related commands
	void command_boxSetChange(RegistrationOperation opKind, BoxGraphItemShared[] boxes);
	void command_edgeSetChange(RegistrationOperation opKind, EdgeGraphItemShared[] edges);
	void command_selectionChange(String[] selectedBoxesAgentIDs);
	
	// responses
	void response_sendExperimentToSave(ExperimentGraph experiment);
}