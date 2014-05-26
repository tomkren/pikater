package org.pikater.web.vaadin.gui.client.kineticengine.graphitems;

import java.util.ArrayList;
import java.util.List;

import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Group;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Layer;
import net.edzard.kinetic.Line;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Path;
import net.edzard.kinetic.PathSVG;
import net.edzard.kinetic.Rectangle;
import net.edzard.kinetic.Vector2d;
import net.edzard.kinetic.Line.LineStyle;
import net.edzard.kinetic.Rectangle.RectanglePoint;

import org.pikater.web.vaadin.gui.client.kineticengine.GlobalEngineConfig;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator.NodeRegisterType;

import com.google.gwt.core.client.JsArray;

public class EdgePrototype extends ExperimentGraphItem
{
	// **********************************************************************************************
	// "CONFIGURATION" AND TYPES
	
	/**
	 * Parameters defining the looks of the arrowhead (how "sharp" it is).
	 * In this case, it is a little bit longer than wider, which makes for a bit "sharper" arrowhead.
	 */
	private static final int arrowLength = 7;
	private static final int arrowWidth = 6;
	
	/**
	 * Parameter defining the size of the drag marks. 
	 */
	private static final int dragMarkDimensionInPixels = 12;
	private static final Vector2d dragMarkSize = new Vector2d(dragMarkDimensionInPixels, dragMarkDimensionInPixels);
	private static final Vector2d dragMarkHalfSize = new Vector2d(dragMarkDimensionInPixels >> 1, dragMarkDimensionInPixels >> 1);
	
	private static enum InternalState
	{
		EDGE,
		BASELINE
	};
	
	public static enum EndPoint
	{
		FROM,
		TO;
		
		public EndPoint getInverted()
		{
			switch (this)
			{
				case FROM:
					return TO;
				case TO:
					return FROM;
				default:
					throw new IllegalStateException();
			}
		}
	}
	
	// **********************************************************************************************
	// PROGRAMMATIC VARIABLES
	
	/**
	 * Boxes that this edge connects.
	 */
	private BoxPrototype fromBox;
	private BoxPrototype toBox;
	
	/**
	 * The state indicating whether this edge is currently being displayed as an edge or as a baseline.
	 */
	private InternalState internalState;
	
	// **********************************************************************************************
	// INNER KINETIC COMPONENTS
	
	/**
	 * The main shape representing this edge.
	 */
	private final PathSVG arrow;
	
	/**
	 * The group to contain the edge and the drag marks, not the baseline though.
	 */
	private final Group groupContainer;
	
	/**
	 * The small squares to trigger "edge dragging".
	 */
	private final Rectangle fromBoxDragMark;
	private final Rectangle toBoxDragMark;
	
	/**
	 * The line to be shown instead of this edge when an endpoint box is dragged or this edge is being connected (dragged) to a different endpoint.
	 */
	private final Line baseLine;
	
	/**
	 * Regular constructor.
	 */
	public EdgePrototype(KineticEngine kineticEngine)
	{
		super(kineticEngine);
		
		this.fromBox = null;
		this.toBox = null;
		
		this.internalState = InternalState.EDGE;
		
		this.baseLine = Kinetic.createLine(Vector2d.origin, Vector2d.origin);
		this.baseLine.setStroke(Colour.aqua);
		this.baseLine.setListening(false);
		this.baseLine.setName(GlobalEngineConfig.name_edge_baseLine);
		
		this.groupContainer = Kinetic.createGroup();
		this.groupContainer.setDraggable(false);
		this.groupContainer.setName(GlobalEngineConfig.name_edge_container);
		
		this.arrow = Kinetic.createPathSVG(Vector2d.origin, "");
		this.arrow.setStrokeWidth(3.0);
		this.arrow.setListening(false);
		this.arrow.setName(GlobalEngineConfig.name_edge_arrrow);
		
		this.fromBoxDragMark = Kinetic.createRectangle(new Box2d(Vector2d.origin, dragMarkSize));
		this.fromBoxDragMark.setFill(Colour.paleturquoise);
		this.fromBoxDragMark.setDraggable(false);
		this.fromBoxDragMark.setName(GlobalEngineConfig.name_edge_fromDrag);
		// this.fromBoxDragMark.hide();
		
		this.toBoxDragMark = Kinetic.createRectangle(new Box2d(Vector2d.origin, dragMarkSize));
		this.toBoxDragMark.setFill(Colour.paleturquoise);
		this.toBoxDragMark.setDraggable(false);
		this.toBoxDragMark.setName(GlobalEngineConfig.name_edge_toDrag);
		// this.toBoxDragMark.hide();
		
		this.groupContainer.add(this.arrow);
		this.groupContainer.add(this.fromBoxDragMark);
		this.groupContainer.add(this.toBoxDragMark);
	}
	
	public EdgePrototype(KineticEngine kineticEngine, Group groupContainer, Line baseLine)
	{
		super(kineticEngine);
		
		this.fromBox = null;
		this.toBox = null;
		
		this.internalState = InternalState.EDGE;
		
		this.baseLine = baseLine;
		
		this.groupContainer = groupContainer;
		this.arrow = groupContainer.find("." + GlobalEngineConfig.name_edge_arrrow).get(0).cast();
		this.fromBoxDragMark = groupContainer.find("." + GlobalEngineConfig.name_edge_fromDrag).get(0).cast();
		this.toBoxDragMark = groupContainer.find("." + GlobalEngineConfig.name_edge_toDrag).get(0).cast();
	}
	
	// **********************************************************************************************
	// INHERITED INTERFACE
	
	@Override
	public void registerInKinetic()
	{
		Layer layer = getKineticEngine().getContainer(EngineComponent.LAYER_EDGES).cast();
		layer.add(groupContainer);
		layer.add(baseLine);
		
		assert(internalState == InternalState.EDGE);
		baseLine.hide();
	}
	
	@Override
	public void unregisterInKinetic()
	{
		groupContainer.remove();
		baseLine.remove();
	}
	
	@Override
	protected void destroyInnerNodes()
	{
		baseLine.destroy();
		groupContainer.destroyChildren();
		groupContainer.destroy();
	}
	
	@Override
	protected void invertSelectionProgrammatically()
	{
		if(isSelected())
		{
			groupContainer.moveTo(getKineticEngine().getSelectionContainer()); // select
		}
		else
		{
			groupContainer.moveTo(getKineticEngine().getContainer(EngineComponent.LAYER_EDGES)); // deselect
		}
	}
	
	@Override
	protected void invertSelectionVisually()
	{
		if(isSelected())
		{
			arrow.setStroke(Colour.coral); // select
		}
		else
		{
			arrow.setStroke(Colour.black); // deselect
		}
	}
	
	@Override
	public EngineComponent getComponentToDraw()
	{
		return internalState == InternalState.BASELINE ? EngineComponent.LAYER_EDGES :
			(isSelected() ? EngineComponent.LAYER_SELECTION : EngineComponent.LAYER_EDGES);
	}
	
	@Override
	public Node getMasterNode()
	{
		switch (internalState)
		{
			case BASELINE:
				return baseLine;
			case EDGE:
				return groupContainer;
			default:
				throw new IllegalStateException();	
		}
	}
	
	// **********************************************************************************************
	// ENDPOINT OR EDGE DRAGGING RELATED METHODS
	
	public void endPointDrag_toBaseLine(BoxPrototype draggedEndPoint)
	{
		// IMPORTANT: don't violate the call order
		baseLine.setEnd(getOtherEndpoint(draggedEndPoint).getAbsolutePointPosition(RectanglePoint.CENTER));
		endPointDrag_updateBaseLine(draggedEndPoint);
		toBaseLine_onFinish();
	}
	
	public void endPointDrag_updateBaseLine(BoxPrototype draggedEndPoint)
	{
		updateBaseLineStart(draggedEndPoint.getAbsolutePointPosition(RectanglePoint.CENTER));
	}
	
	public void endPointDrag_toEdge()
	{
		toEdge_onFinish(true);
	}
	
	public void edgeDrag_toBaseLine(Vector2d initialDragPosition, BoxPrototype staticBox)
	{
		// IMPORTANT: don't violate the call order
		baseLine.setEnd(staticBox.getAbsolutePointPosition(RectanglePoint.CENTER));
		edgeDrag_updateBaseLine(initialDragPosition);
		toBaseLine_onFinish();
	}
	
	public void edgeDrag_updateBaseLine(Vector2d newPosition)
	{
		updateBaseLineStart(newPosition);
	}
	
	public void edgeDrag_toEdge(boolean updateEdge)
	{
		toEdge_onFinish(updateEdge);
	}
	
	// **********************************************************************************************
	// JUST SOME GETTERS AND UNIMPORTANT PUBLIC ROUTINES
	
	public static List<EdgePrototype> getInstancesFrom(KineticShapeCreator shapeCreator, Layer dynamicLayer)
	{
		// first find all arrows and baselines
		JsArray<Node> containers = dynamicLayer.find("." + GlobalEngineConfig.name_edge_container);
		JsArray<Node> baseLines = dynamicLayer.find("." + GlobalEngineConfig.name_edge_baseLine);
		
		// just in case check
		assert(containers.length() == baseLines.length());
		
		// now pair up arrows with baselines (arrows alone determine the edge in the original graph, so it doesn't matter which baseline we connect to which arrow)
		List<EdgePrototype> result = new ArrayList<EdgePrototype>();
		for(int i = 0; i < containers.length(); i++)
		{
			Group container = containers.get(i).cast();
			Line baseLine = baseLines.get(i).cast();
			result.add(shapeCreator.createEdge(NodeRegisterType.MANUAL, container, baseLine));
		}
		return result;
	}
	
	public BoxPrototype getEndPoint(EndPoint endPoint)
	{
		switch (endPoint)
		{
			case FROM:
				return fromBox;
			case TO:
				return toBox;
			default:
				throw new IllegalStateException();
		}
	}
	
	public Rectangle getDragMark(EndPoint endPoint)
	{
		switch (endPoint)
		{
			case FROM:
				return fromBoxDragMark;
			case TO:
				return toBoxDragMark;
			default:
				throw new IllegalStateException();
		}
	}
	
	public boolean isFromEndpoint(BoxPrototype box)
	{
		return fromBox == box;
	}
	
	public boolean areBothEndsSelected()
	{
		return fromBox.isSelected() && toBox.isSelected();
	}
	
	public boolean isExactlyOneEndSelected()
	{
		return fromBox.isSelected() ^ toBox.isSelected(); // exclusive or (xor)
	}
	
	public int getSelectedEndpointsCount()
	{
		return (fromBox.isSelected() ? 1 : 0) + (toBox.isSelected() ? 1 : 0);
	}
	
	public BoxPrototype getOtherEndpoint(BoxPrototype oneEndpoint)
	{
		assert((oneEndpoint == fromBox) || (oneEndpoint == toBox));
		return oneEndpoint == fromBox ? toBox : fromBox;
	}
	
	public BoxPrototype getSelectedEndpoint()
	{
		assert(isExactlyOneEndSelected());
		return fromBox.isSelected() ? fromBox : toBox;
	}
	
	public BoxPrototype getNotSelectedEndpoint()
	{
		assert(isExactlyOneEndSelected());
		return fromBox.isSelected() ? toBox : fromBox;
	}
	
	// **********************************************************************************************
	// OTHER IMPORTANT PUBLIC METHODS
	
	/**
	 * When using this method, keep in mind that eventually {@link #registerEdgeInEndpoints} need to be
	 * called to keep consistency.
	 */
	public void connectFromBox(BoxPrototype box)
	{
		if(this.fromBox != null)
		{
			this.fromBox.unregisterEdge(this);
		}
		this.fromBox = box;
	}
	
	/**
	 * When using this method, keep in mind that eventually {@link #registerEdgeInEndpoints} need to be
	 * called to keep consistency.
	 */
	public void connectToBox(BoxPrototype box)
	{
		if(this.toBox != null)
		{
			this.toBox.unregisterEdge(this);
		}
		this.toBox = box;
	}

	/**
	 * Only call this method after both endpoints have been correctly set with the
	 * {@link #connectFromBox} and {@link #connectToBox} methods.
	 */
	public void registerEdgeInEndpoints()
	{
		assert((fromBox != null) && (toBox != null));
		this.fromBox.registerEdge(this);
		this.toBox.registerEdge(this);
	}
	
	public void unregisterEdgeInEndpoints()
	{
		assert((fromBox != null) && (toBox != null));
		this.fromBox.unregisterEdge(this);
		this.toBox.unregisterEdge(this);
	}
	
	public void updateEdge()
	{
		// compute new endpoints
		Vector2d delta = toBox.getAbsolutePointPosition(RectanglePoint.CENTER).sub(fromBox.getAbsolutePointPosition(RectanglePoint.CENTER)); // the vector from A to B <=> B-A <=> delta
		double angle = Math.toDegrees(Math.atan2(delta.y, delta.x));
		double absAngle = Math.abs(angle);
		RectanglePoint b1Endpoint;
		if(absAngle < 60)
		{
			b1Endpoint = RectanglePoint.WESTCENTER;
		}
		else if(absAngle < 120)
		{
			b1Endpoint = angle < 0 ? RectanglePoint.NORTHCENTER : RectanglePoint.SOUTHCENTER;
		}
		else
		{
			b1Endpoint = RectanglePoint.EASTCENTER;
		}
		
		// update arrow
		Vector2d fromPos = fromBox.getAbsolutePointPosition(b1Endpoint);
		Vector2d toPos = toBox.getAbsolutePointPosition(b1Endpoint.getInverted());
		groupContainer.setPosition(Vector2d.origin); // have to reset in case we performed a drag operation earlier
		computeArrowPath(fromPos, toPos);
		
		// update the drag marks
		this.fromBoxDragMark.setPosition(fromPos.sub(dragMarkHalfSize));
		this.toBoxDragMark.setPosition(toPos.sub(dragMarkHalfSize));
	}
	
	// **********************************************************************************************
	// PRIVATE INTERFACE
	
	private void computeArrowPath(Vector2d fromPos, Vector2d toPos)
	{
		// arrowhead precomputation
		Vector2d u = new Vector2d(toPos.x - fromPos.x, toPos.y - fromPos.y);
		u.normalize();
		Vector2d v = new Vector2d(-u.y, u.x);
		u.scale(arrowLength * Math.sqrt(3));
		v.scale(arrowWidth);
		
		// compute the arrowhead's end points
		Vector2d leftArrowEnd = new Vector2d(toPos.x, toPos.y);
		leftArrowEnd.sub(u);
		leftArrowEnd.add(v);
		Vector2d rightArrowEnd = new Vector2d(toPos.x, toPos.y);
		rightArrowEnd.sub(u);
		rightArrowEnd.sub(v);

		// update the arrow
		this.arrow.setData(new Path().moveTo(fromPos).lineTo(toPos).moveTo(leftArrowEnd).lineTo(toPos).lineTo(rightArrowEnd).closePath().toSVGPath());
		this.arrow.setPosition(0, 0); // reset any selection drags to avoid the X and Y properties to act as an offset
	}
	
	private void updateBaseLineStart(Vector2d position)
	{
		baseLine.setStart(position);
		Vector2d delta = position.sub(baseLine.getEnd()); // alias for the same Vector2d instance
		if(delta.x == 0 || delta.y == 0)
		{
			baseLine.setStrokeWidth(2);
			baseLine.setLineStyle(LineStyle.NORMAL);
		}
		else
		{
			baseLine.setStrokeWidth(0.5);
			baseLine.setLineStyle(LineStyle.DASHED);
		}
	}
	
	private void toBaseLine_onFinish()
	{
		// change the state
		internalState = InternalState.BASELINE;
		
		// and switch visible components
		groupContainer.hide();
		baseLine.show();
	}
	
	private void toEdge_onFinish(boolean updateEdge)
	{
		// IMPORTANT: don't violate the call order
		
		// change the state
		internalState = InternalState.EDGE;
		
		// update arrow positions
		if(updateEdge)
		{
			updateEdge();
		}
		registerEdgeInEndpoints();
		
		// and switch visible components
		baseLine.hide();
		groupContainer.show();
	}
}
