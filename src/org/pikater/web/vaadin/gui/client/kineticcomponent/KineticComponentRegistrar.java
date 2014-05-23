package org.pikater.web.vaadin.gui.client.kineticcomponent;

import java.util.HashMap;
import java.util.Map;

import org.pikater.shared.experiment.webformat.SchemaDataSource;

public class KineticComponentRegistrar
{
	private static final Map<String, SchemaDataSource> connectorIDtoWidget = new HashMap<String, SchemaDataSource>();
	
	public static void saveExperimentFor(String connectorID, SchemaDataSource experiment)
	{
		connectorIDtoWidget.put(connectorID, experiment);
	}
	
	public static SchemaDataSource getSavedExperimentFor(String connectorID)
	{
		return connectorIDtoWidget.get(connectorID); 
	}
}