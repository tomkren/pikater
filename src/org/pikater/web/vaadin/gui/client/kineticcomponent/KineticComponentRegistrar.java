package org.pikater.web.vaadin.gui.client.kineticcomponent;

import java.util.HashMap;
import java.util.Map;

import org.pikater.shared.experiment.webformat.ExperimentGraph;

public class KineticComponentRegistrar
{
	private static final Map<String, ExperimentGraph> connectorIDtoWidget = new HashMap<String, ExperimentGraph>();
	
	public static void saveExperimentFor(String connectorID, ExperimentGraph experiment)
	{
		connectorIDtoWidget.put(connectorID, experiment);
	}
	
	public static ExperimentGraph getSavedExperimentFor(String connectorID)
	{
		return connectorIDtoWidget.get(connectorID); 
	}
}