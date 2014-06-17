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
import net.edzard.kinetic.Image;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Layer;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Rectangle;
import net.edzard.kinetic.Text;
import net.edzard.kinetic.Vector2d;
import net.edzard.kinetic.Rectangle.RectanglePoint;
import net.edzard.kinetic.Text.FontStyle;
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
	// INNER KINETIC COMPONENTS
	
	/**
	 * The group to hold all other shapes together.
	 */
	private final Group container;
	
	/**
	 * The outward shape that depicts the bounds of the box. 
	 */
	private final Rectangle rectangle;
	
	/**
	 * This box's icon corresponding to its type. 
	 */
	private final Image icon;
	
	/**
	 * This box's title corresponding to its type.  
	 */
	private final Text title;
	
	/**
	 * The box's name ("instance" of box type). 
	 */
	private final Text name;
	
	// **********************************************************************************************
	// PROGRAMMATIC VARIABLES

	/**
	 * Edges leading from and to this box and iterator over these 2 sets.
	 * The Set interface is counted upon in {@link #registerEdge()} and {@link #unregisterEdge()}
	 * methods so don't change it lightly.
	 */
	public final Set<EdgePrototype> connectedEdges;
	
	/**
	 * Reference to an external box information received from the server.
	 */
	private final BoxInfo info;
	
	/**
	 * Regular constructor.
	 */
	public BoxPrototype(KineticEngine kineticEngine, BoxInfo info)
	{
		super(kineticEngine);
		
		// first programmatic fields
		this.info = info;
		this.connectedEdges = new HashSet<EdgePrototype>();
		
		// setup master rectangle
		rectangle = Kinetic.createRectangle(new Box2d(Vector2d.origin, Vector2d.origin));
		rectangle.setDraggable(false);
		rectangle.setCornerRadius(15);
		
		final double componentSpace = 13; // in pixels
		
		// setup the box's icon
		icon = Kinetic.createImage(new Vector2d(componentSpace, componentSpace), new com.google.gwt.user.client.ui.Image(info.pictureURL));
		icon.setDraggable(false);
		icon.setStroke(Colour.black);
		icon.setStrokeWidth(2);
		
		double textOffset_left = componentSpace + icon.getWidth() + componentSpace / 2;
		
		// box type text label
		title = Kinetic.createText(new Vector2d(textOffset_left, componentSpace), info.boxTypeName);
		title.setDraggable(false);
		title.setFontStyle(FontStyle.BOLD);
		
		// box display name label
		name = Kinetic.createText(new Vector2d(textOffset_left, componentSpace + icon.getHeight() / 2), info.displayName);
		name.setDraggable(false);
		name.setFontStyle(FontStyle.ITALIC);
		
		// create the group, bind it all together
	    this.container = Kinetic.createGroup(new Vector2d(info.initialX, info.initialY), 0);
		this.container.setID(info.boxID);
		this.container.add(rectangle);
		this.container.add(icon);
		this.container.add(title);
		this.container.add(name);
		
	    // set event handlers
	    this.container.addEventListener(new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				getKineticEngine().fromEdgesToBaseLines(connectedEdges, BoxPrototype.this); // draws changes by default
				event.stopVerticalPropagation();
			}
		}, EventType.Basic.DRAGSTART);
	    this.container.addEventListener(new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				getKineticEngine().updateBaseLines(connectedEdges, BoxPrototype.this); // draws changes by default
				event.stopVerticalPropagation();
			}
		}, EventType.Basic.DRAGMOVE);
	    this.container.addEventListener(new IEventListener()
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
	public void applyUserSettings()
	{
		/*
		 * Set sizes.
		 */
		
		final Vector2d boxSize = GWTKineticSettings.getCurrentBoxSize();
		final Vector2d textSize = new Vector2d(boxSize.x - title.getPosition().x - 10, icon.getHeight());
		
		rectangle.setSize(boxSize);
		// icon already has size set from the constructor
		title.setSize(textSize);
		name.setSize(textSize);
	}
	
	@Override
	protected void applyVisualStyle(VisualStyle style)
	{
		switch(style)
		{
			case SELECTED:
				container.setDraggable(false);
				rectangle.setStroke(Colour.gold);
				rectangle.setStrokeWidth(3);
				break;
			case NOT_SELECTED:
				container.setDraggable(true);
				rectangle.setStroke(Colour.black);
				rectangle.setStrokeWidth(2);
				break;
				
			case HIGHLIGHTED:
				rectangle.setStroke(Colour.red);
				rectangle.setStrokeWidth(3);
				break;
			case NOT_HIGHLIGHTED:
				rectangle.setStroke(Colour.black);
				rectangle.setStrokeWidth(2);
				break;

			default:
				throw new IllegalStateException("Unknown state: " + style.name());
		}
	}
	
	@Override
	protected void setRegisteredInKinetic(boolean registered)
	{
		if(registered)
		{
			getKineticEngine().getContainer(EngineComponent.LAYER_BOXES).add(container);
		}
		else
		{
			container.remove();
		}
	}

	@Override
	protected void destroyInnerNodes()
	{
		container.destroyChildren();
		container.destroy();
	}
	
	@Override
	protected void invertSelectionProgrammatically()
	{
		if(isSelected())
		{
			container.moveTo(getKineticEngine().getSelectionContainer()); // select
		}
		else
		{
			container.moveTo(getKineticEngine().getContainer(EngineComponent.LAYER_BOXES)); // deselect
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
		return container;
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
	
	public BoxInfo getInfo()
	{
		return info;
	}
	
	public Vector2d getAbsoluteNodePosition()
	{
		return container.getAbsolutePosition();
	}
	
	public Vector2d getAbsolutePointPosition(RectanglePoint point)
	{
		return rectangle.getAbsolutePointPosition(point);
	}
	
	public boolean isNotConnectedTo(BoxPrototype otherBox)
	{
		return Collections.disjoint(connectedEdges, otherBox.connectedEdges);
	}
	
	public boolean intersects(Vector2d selectionAbsPos, Vector2d selectionSize)
	{
		return rectangle.intersects(selectionAbsPos, selectionSize);
	}
	
	public void setEdgeRegistered(EdgePrototype edge, boolean registered)
	{
		if(registered)
		{
			connectedEdges.add(edge);
		}
		else
		{
			connectedEdges.remove(edge);
		}
	}
}