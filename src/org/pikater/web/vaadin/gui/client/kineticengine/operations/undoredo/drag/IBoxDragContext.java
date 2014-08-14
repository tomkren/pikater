package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.drag;

import java.util.Set;

import net.edzard.kinetic.Node;
import net.edzard.kinetic.Vector2d;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.BoxGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.EdgeGraphItemClient;

public interface IBoxDragContext
{
	KineticEngine getEngine();
	Vector2d getCurrentPosition();
	BoxGraphItemClient getBoxBeingDragged();
	Set<EdgeGraphItemClient> getEdgesInBetween();
	Node[] getAllMovedNodes();
	void setNewPositions(Node[] allMovedNodes, IBoxDragEndContext dragEndContext);
	void setOriginalPositions(Node[] allMovedNodes, IBoxDragEndContext dragEndContext);
}