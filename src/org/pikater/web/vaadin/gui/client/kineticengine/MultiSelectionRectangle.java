package org.pikater.web.vaadin.gui.client.kineticengine;

import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Path;
import net.edzard.kinetic.PathSVG;
import net.edzard.kinetic.Vector2d;

import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.IKineticShapeWrapper;

public class MultiSelectionRectangle implements IKineticShapeWrapper
{
	/**
	 * The kinetic shape to draw the rectangle.
	 */
	private final PathSVG path;
	
	/**
	 * The point, where the user started his selection with a mouse down event.
	 */
	private Vector2d originalMousePosition;
	
	/**
	 * The latest mouse position.
	 */
	private Vector2d currentMousePosition;
	
	public MultiSelectionRectangle()
	{
		super();

		this.path = Kinetic.createPathSVG(Vector2d.origin, "");
		this.path.setStroke(Colour.red);
		this.path.setListening(false);
		// TODO: this.path.setLineStyle(LineStyle.DASHED);
		
		reset();
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
	
	public Vector2d getOriginalMousePosition()
	{
		return this.originalMousePosition;
	}
	
	public void setOriginalMousePosition(Vector2d position)
	{
		this.originalMousePosition = position;
		this.currentMousePosition = position;
	}
	
	public void updatePath(Vector2d left, Vector2d up, Vector2d right)
	{
		this.currentMousePosition = up;
		this.path.setData(new Path().moveTo(originalMousePosition).lineTo(left).lineTo(up).lineTo(right).lineTo(originalMousePosition).toSVGPath());
	}
	
	public Box2d getPosAndSize()
	{
		Vector2d pos, size;
		if(originalMousePosition.x < currentMousePosition.x)
		{
			pos = originalMousePosition;
			size = currentMousePosition.sub(originalMousePosition);
		}
		else
		{
			pos = currentMousePosition;
			size = originalMousePosition.sub(currentMousePosition);
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
	
	public void reset()
	{
		this.originalMousePosition = Vector2d.origin;
		this.currentMousePosition = Vector2d.origin;
	}
}
