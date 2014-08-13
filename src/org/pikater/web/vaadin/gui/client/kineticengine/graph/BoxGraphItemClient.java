package org.pikater.web.vaadin.gui.client.kineticengine.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Colour;
import net.edzard.kinetic.Group;
import net.edzard.kinetic.Image;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Rectangle;
import net.edzard.kinetic.Rectangle.RectanglePoint;
import net.edzard.kinetic.Text;
import net.edzard.kinetic.Text.FontStyle;
import net.edzard.kinetic.Vector2d;
import net.edzard.kinetic.event.EventType;
import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.shared.experiment.webformat.client.BoxInfoClient;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.BoxGraphItemShared;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.visualstyle.KineticBoxSettings;

@SuppressWarnings("deprecation")
public class BoxGraphItemClient extends AbstractGraphItemClient<KineticBoxSettings>
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
	public final Set<EdgeGraphItemClient> connectedEdges;
	
	/**
	 * Reference to an external box information received from the server.
	 */
	private final BoxInfoClient info;
	
	/**
	 * Regular constructor.
	 */
	public BoxGraphItemClient(KineticEngine kineticEngine, BoxInfoClient info)
	{
		super(kineticEngine);
		
		// first programmatic fields
		this.info = info;
		this.connectedEdges = new HashSet<EdgeGraphItemClient>();
		
		// setup master rectangle
		this.rectangle = Kinetic.createRectangle(new Box2d(Vector2d.origin, Vector2d.origin));
		this.rectangle.setCornerRadius(15);
		this.rectangle.setDraggable(false);
		
		// setup the box's icon
		this.icon = Kinetic.createImage(Vector2d.origin, new com.google.gwt.user.client.ui.Image(info.pictureURL)); 
		this.icon.setStroke(Colour.black);
		this.icon.setStrokeWidth(2);
		this.icon.setDraggable(false);
		this.icon.setListening(false);
		
		// box type text label
		this.title = Kinetic.createText(Vector2d.origin, info.boxTypeName);
		this.title.setFontStyle(FontStyle.BOLD);
		this.title.setDraggable(false);
		this.title.setListening(false);
		
		// box display name label
		this.name = Kinetic.createText(Vector2d.origin, info.displayName);
		this.name.setFontStyle(FontStyle.ITALIC);
		this.name.setDraggable(false);
		this.name.setListening(false);
		
		// create the group, bind it all together
	    this.container = Kinetic.createGroup(new Vector2d(info.initialX, info.initialY), 0);
		this.container.setID(String.valueOf(info.boxID));
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
				getKineticEngine().fromEdgesToBaseLines(connectedEdges, BoxGraphItemClient.this); // draws changes by default
				event.stopVerticalPropagation();
			}
		}, EventType.Basic.DRAGSTART);
	    this.container.addEventListener(new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				getKineticEngine().updateBaseLines(connectedEdges, BoxGraphItemClient.this); // draws changes by default
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
	public void applySettings(KineticBoxSettings settings)
	{
		final double textOffset_left = settings.getTextOffsetLeft();
		final Vector2d textSize = new Vector2d(settings.getTextWidth(textOffset_left), settings.getTextHeight());
		
		if(settings.isIconsVisible())
		{
			this.icon.show();
		}
		else
		{
			this.icon.hide();
		}
		
		this.icon.setPosition(new Vector2d(settings.getInnerComponentSpace(), settings.getInnerComponentSpace()));
		this.title.setPosition(new Vector2d(textOffset_left, settings.getInnerComponentSpace()));
		this.name.setPosition(new Vector2d(textOffset_left, settings.getInnerComponentSpace() + textSize.y));

		this.rectangle.setSize(new Vector2d(settings.getBoxWidth(), settings.getBoxHeight()));
		this.title.setSize(textSize);
		this.name.setSize(textSize);
		
		this.container.setScale(new Vector2d(settings.getScale(), settings.getScale()));
	}
	
	@Override
	protected void applyVisualStyle(VisualStyle style)
	{
		switch(style)
		{
			case SELECTED:
				container.setDraggable(false);
				// rectangle.setFill(KineticBoxColourScheme.getColor(style));
				rectangle.setStroke(KineticBoxSettings.getColor(style));
				rectangle.setStrokeWidth(3);
				break;
			case NOT_SELECTED:
				container.setDraggable(true);
				// rectangle.setFill(KineticBoxColourScheme.getColor(style));
				rectangle.setStroke(KineticBoxSettings.getColor(style));
				rectangle.setStrokeWidth(2);
				break;
				
			case HIGHLIGHTED_EDGE:
				// rectangle.setFill(KineticBoxColourScheme.getColor(style));
				rectangle.setStroke(KineticBoxSettings.getColor(style));
				rectangle.setStrokeWidth(3);
				break;
			case NOT_HIGHLIGHTED_EDGE:
				// rectangle.setFill(KineticBoxColourScheme.getColor(style));
				rectangle.setStroke(KineticBoxSettings.getColor(style));
				rectangle.setStrokeWidth(2);
				break;
			
			case HIGHLIGHTED_SLOT:
				// rectangle.setFill(KineticBoxColourScheme.getColor(style));
				rectangle.setStroke(KineticBoxSettings.getColor(style));
				rectangle.setStrokeWidth(3);
				break;
			case NOT_HIGHLIGHTED_SLOT:
				// rectangle.setFill(KineticBoxColourScheme.getColor(style));
				rectangle.setStroke(KineticBoxSettings.getColor(style));
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
	
	public static BoxGraphItemShared[] toShared(BoxGraphItemClient... boxes)
	{
		BoxGraphItemShared[] result = new BoxGraphItemShared[boxes.length];
		for(int i = 0; i < boxes.length; i++)
		{
			result[i] = new BoxGraphItemShared(boxes[i].getInfo().boxID);
		}
		return result;
	}
	
	public BoxInfoClient getInfo()
	{
		return info;
	}
	
	public Vector2d getAbsoluteNodePosition()
	{
		return container.getAbsolutePosition();
	}
	
	public Vector2d getAbsolutePointPosition(RectanglePoint point)
	{
		return rectangle.getAbsolutePointPosition(point, container.getScale());
	}
	
	public boolean isNotConnectedTo(BoxGraphItemClient otherBox)
	{
		return Collections.disjoint(connectedEdges, otherBox.connectedEdges);
	}
	
	public boolean intersects(Vector2d selectionAbsPos, Vector2d selectionSize)
	{
		return rectangle.intersects(selectionAbsPos, selectionSize);
	}
	
	public void setEdgeRegistered(EdgeGraphItemClient edge, boolean registered)
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