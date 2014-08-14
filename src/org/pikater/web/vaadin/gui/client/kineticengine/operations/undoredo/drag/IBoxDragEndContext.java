package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.drag;

import net.edzard.kinetic.Vector2d;

public interface IBoxDragEndContext
{
	Vector2d getOriginalPosition();
	Vector2d getNewPosition();
	Vector2d getDelta();
}