package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.SchemaDataSource;

import com.vaadin.shared.communication.ClientRpc;

public interface KineticComponentClientRpc extends ClientRpc
{
	void createBox(BoxInfo info, int posX, int posY);
	void loadExperiment(SchemaDataSource experiment);
	void resetKineticEnvironment();
}