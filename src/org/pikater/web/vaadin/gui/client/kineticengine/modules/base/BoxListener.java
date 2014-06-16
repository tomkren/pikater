package org.pikater.web.vaadin.gui.client.kineticengine.modules.base;

import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;

/**
 * General template for listeners attached to a box.
 */
public abstract class BoxListener extends ModuleEventListener
{
	protected final BoxPrototype parentBox;
	
	public BoxListener(BoxPrototype parentBox)
	{
		this.parentBox = parentBox;
	}
}