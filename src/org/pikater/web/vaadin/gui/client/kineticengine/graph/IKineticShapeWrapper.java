package org.pikater.web.vaadin.gui.client.kineticengine.graph;

import net.edzard.kinetic.Node;

public interface IKineticShapeWrapper
{
	/**
	 * Gets the master node.
	 * @return The node that contains the whole underlying structure.
	 */
	public Node getMasterNode();
}