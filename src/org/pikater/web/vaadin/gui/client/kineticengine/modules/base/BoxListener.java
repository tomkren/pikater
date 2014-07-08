package org.pikater.web.vaadin.gui.client.kineticengine.modules.base;

import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.BoxGraphItemClient;

/**
 * General template for listeners attached to a box.
 */
public abstract class BoxListener extends ModuleEventListener
{
	protected final BoxGraphItemClient parentBox;
	
	public BoxListener(BoxGraphItemClient parentBox)
	{
		this.parentBox = parentBox;
	}
}