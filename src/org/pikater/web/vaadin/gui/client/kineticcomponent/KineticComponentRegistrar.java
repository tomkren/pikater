package org.pikater.web.vaadin.gui.client.kineticcomponent;

import java.util.HashMap;
import java.util.Map;

import org.pikater.shared.experiment.webformat.Experiment;

public class KineticComponentRegistrar
{
	private static final Map<String, Experiment> connectorIDtoWidget = new HashMap<String, Experiment>();
	
	public static void saveExperimentFor(String connectorID, Experiment experiment)
	{
		connectorIDtoWidget.put(connectorID, experiment);
	}
	
	public static Experiment getSavedExperimentFor(String connectorID)
	{
		return connectorIDtoWidget.get(connectorID); 
	}
}