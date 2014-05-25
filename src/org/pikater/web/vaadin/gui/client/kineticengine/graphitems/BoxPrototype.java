package org.pikater.web.vaadin.gui.client.kineticengine.graphitems;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.JsArray;

import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Group;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Layer;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Rectangle;
import net.edzard.kinetic.Text;
import net.edzard.kinetic.Vector2d;
import net.edzard.kinetic.Rectangle.RectanglePoint;
import net.edzard.kinetic.event.EventType;
import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTKineticSettings;
import org.pikater.web.vaadin.gui.client.kineticengine.GlobalEngineConfig;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;

@SuppressWarnings("deprecation")
public class BoxPrototype extends ExperimentGraphItem
{
	// **********************************************************************************************
	// PROGRAMMATIC VARIABLES
	
	/**
	 * Edges leading from and to this box and iterator over these 2 sets.
	 */
	public final Set<EdgePrototype> connectedEdges;
	
	// **********************************************************************************************
	// INNER KINETIC COMPONENTS
	
	/**
	 * The box's inner components.
	 */
	private final Group boxContainer;
	private final Rectangle masterShape;
	private final Text textLabel;
	
	// **********************************************************************************************
	// EXTERNAL REFERENCES
	
	public final BoxInfo info;
	
	/**
	 * Regular constructor.
	 */
	public BoxPrototype(KineticEngine kineticEngine, String ID, BoxInfo info, Vector2d position)
	{
		super(kineticEngine);
		this.info = info;
		this.connectedEdges = new HashSet<EdgePrototype>();
		
		// setup master rectangle
		this.masterShape = Kinetic.createRectangle(new Box2d(Vector2d.origin, GWTKineticSettings.getCurrentBoxSize()));
		this.masterShape.setDraggable(false);
		this.masterShape.setName(GlobalEngineConfig.name_box_masterRectangle); // the master shape that defines the bounds of the whole "box"
		
		// setup text label
	    this.textLabel = Kinetic.createText(Vector2d.origin, info.name);
	    this.textLabel.setName(GlobalEngineConfig.name_box_textLabel);
	    this.textLabel.setListening(false);
	    
	    this.boxContainer = Kinetic.createGroup();
		this.boxContainer.setPosition(position);
		this.boxContainer.setName(GlobalEngineConfig.name_box_container);
		this.boxContainer.setID(ID);
		
		this.boxContainer.add(masterShape);
		this.boxContainer.add(textLabel);
	    
	    // and finally, set event handlers
	    this.boxContainer.addEventListener(new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				getKineticEngine().fromEdgesToBaseLines(connectedEdges, BoxPrototype.this); // draws changes by default
				event.stopVerticalPropagation();
			}
		}, EventType.Basic.DRAGSTART);
	    this.boxContainer.addEventListener(new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				getKineticEngine().updateBaseLines(connectedEdges, BoxPrototype.this); // draws changes by default
				event.stopVerticalPropagation();
			}
		}, EventType.Basic.DRAGMOVE);
	    this.boxContainer.addEventListener(new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				getKineticEngine().fromBaseLinesToEdges(connectedEdges); // draws changes by default
				event.stopVerticalPropagation();
			}
		}, EventType.Basic.DRAGEND);
	}
	
	// **********************************************************************************************
	// INHERITED INTERFACE
	
	@Override
	public void registerInKinetic()
	{
		getKineticEngine().getContainer(EngineComponent.LAYER_BOXES).add(boxContainer);
	}
	
	@Override
	public void unregisterInKinetic()
	{
		boxContainer.remove();
	}
	
	@Override
	protected void destroyInnerNodes()
	{
		boxContainer.destroyChildren();
		boxContainer.destroy();
	}
	
	@Override
	protected void invertSelectionProgrammatically()
	{
		if(isSelected())
		{
			boxContainer.moveTo(getKineticEngine().getSelectionContainer()); // select
			this.boxContainer.setDraggable(false);
		}
		else
		{
			boxContainer.moveTo(getKineticEngine().getContainer(EngineComponent.LAYER_BOXES)); // deselect
			this.boxContainer.setDraggable(true);
		}
	}
	
	@Override
	protected void invertSelectionVisually()
	{
		if(isSelected())
		{
			masterShape.setFill(Colour.blue); // select
		}
		else
		{
			masterShape.setFill(Colour.white); // deselect
		}
	}
	
	@Override
	public EngineComponent getComponentToDraw()
	{
		return isSelected() ? EngineComponent.LAYER_SELECTION : EngineComponent.LAYER_BOXES;
	}
	
	@Override
	public Node getMasterNode()
	{
		return boxContainer;
	}
	
	// **********************************************************************************************
	// EDGE DRAGGING INTERFACE
	
	public void highlightAsNewEndpointCandidate()
	{
		masterShape.setFill(Colour.yellow);
	}
	
	public void cancelHighlightAsNewEndpointCandidate()
	{
		invertSelectionVisually();
	}
	
	// **********************************************************************************************
	// PUBLIC INTERFACE
	
	@Deprecated
	public static Map<String, BoxPrototype> getInstancesFrom(KineticShapeCreator shapeCreator, Layer dynamicLayer)
	{
		Map<String, BoxPrototype> result = new HashMap<String, BoxPrototype>();
		JsArray<Node> boxContainers = dynamicLayer.find("." + GlobalEngineConfig.name_box_container); 
		for(int i = 0; i < boxContainers.length(); i++)
		{
			// if we try to load existing nodes into a new box instance, it just won't work for some reason... we have to make a clone
			/*
			Group boxContainer = boxContainers.get(i).cast();
			Text textLabel = boxContainer.find("." + GlobalEngineConfig.name_box_textLabel).get(0).cast();
			Rectangle masterShape = boxContainer.find("." + GlobalEngineConfig.name_box_masterRectangle).get(0).cast();			
			
			// TODO: serialize/deserialize box info instances
			BoxPrototype newInstance = shapeCreator.createBox(NodeRegisterType.MANUAL, textLabel.getText(), boxContainer.getPosition());
			result.put(boxContainer.getID(), newInstance); // IMPORTANT: register the original ID, not the new one
			*/
		}
		return result;
	}
	
	public String getID()
	{
		return boxContainer.getID();
	}
	
	public Vector2d getAbsoluteNodePosition()
	{
		return boxContainer.getAbsolutePosition();
	}
	
	public Vector2d getAbsolutePointPosition(RectanglePoint point)
	{
		return masterShape.getAbsolutePointPosition(point);
	}
	
	public Rectangle getMasterRectangle()
	{
		return masterShape;
	}
	
	public boolean isNotConnectedTo(BoxPrototype otherBox)
	{
		return Collections.disjoint(connectedEdges, otherBox.connectedEdges);
	}
	
	public boolean intersects(Vector2d selectionAbsPos, Vector2d selectionSize)
	{
		return masterShape.intersects(selectionAbsPos, selectionSize);
	}
	
	public void reloadVisualStyle()
	{
		masterShape.setSize(GWTKineticSettings.getCurrentBoxSize());
	}
	
	public void registerEdge(EdgePrototype edge)
	{
		connectedEdges.add(edge);
	}
	
	public void unregisterEdge(EdgePrototype edge)
	{
		connectedEdges.remove(edge);
	}
}
