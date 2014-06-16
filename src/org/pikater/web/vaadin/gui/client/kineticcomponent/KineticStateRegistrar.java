package org.pikater.web.vaadin.gui.client.kineticcomponent;

import java.util.HashMap;
import java.util.Map;

public class KineticStateRegistrar
{
	private static final Map<String, KineticState> connectorIDToSavedState = new HashMap<String, KineticState>();
	
	public static void saveState(String connectorID, KineticState state)
	{
		connectorIDToSavedState.put(connectorID, state);
	}
	
	public static KineticState getSavedState(String connectorID)
	{
		return connectorIDToSavedState.get(connectorID);
	}
}