package org.pikater.web.vaadin.gui.client.kineticeditorcore;

import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Path;
import net.edzard.kinetic.PathSVG;
import net.edzard.kinetic.Vector2d;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.IShapeWrapper;

public class MultiSelectionRectangle implements IShapeWrapper
{
	/**
	 * The point, where the user started his selection with a mouse down event.
	 */
	public Vector2d originalMultiSelectionRectanglePosition;
	
	/**
	 * The latest mouse position.
	 */
	private Vector2d oppositeCorner;
	
	/**
	 * The kinetic shape to draw the rectangle.
	 */
	private final PathSVG path;
	
	public MultiSelectionRectangle()
	{
		super();
		this.originalMultiSelectionRectanglePosition = Vector2d.origin;

		this.path = Kinetic.createPathSVG(Vector2d.origin, "");
		this.path.setStroke(Colour.red);
		this.path.setListening(false);
		// TODO: this.path.setLineStyle(LineStyle.DASHED);
	}
	
	// **********************************************************************************************
	// INHERITED INTERFACE
	
	@Override
	public Node getMasterNode()
	{
		return path;
	}

	// **********************************************************************************************
	// PUBLIC INTERFACE
	
	public void updatePath(Vector2d left, Vector2d up, Vector2d right)
	{
		oppositeCorner = up;
		this.path.setData(new Path().moveTo(originalMultiSelectionRectanglePosition).lineTo(left).lineTo(up).lineTo(right).lineTo(originalMultiSelectionRectanglePosition).toSVGPath());
	}
	
	public Box2d getPosAndSize()
	{
		Vector2d pos, size;
		if(originalMultiSelectionRectanglePosition.x < oppositeCorner.x)
		{
			pos = originalMultiSelectionRectanglePosition;
			size = oppositeCorner.sub(originalMultiSelectionRectanglePosition);
		}
		else
		{
			pos = oppositeCorner;
			size = originalMultiSelectionRectanglePosition.sub(oppositeCorner);
		}
		if(size.x < 0)
		{
			pos.x += size.x;
			size.x = Math.abs(size.x);
		}
		if(size.y < 0)
		{
			pos.y += size.y;
			size.y = Math.abs(size.y);
		}
		return new Box2d(pos, size);
	}
}
