package org.pikater.web.vaadin.gui.client.kineticengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.edzard.kinetic.Box2d;
import net.edzard.kinetic.Container;
import net.edzard.kinetic.Kinetic;
import net.edzard.kinetic.Layer;
import net.edzard.kinetic.Node;
import net.edzard.kinetic.Rectangle;
import net.edzard.kinetic.Stage;
import net.edzard.kinetic.Vector2d;
import net.edzard.kinetic.event.EventType;
import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.shared.experiment.webformat.ExperimentGraph;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTMisc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentWidget;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator.NodeRegisterType;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype.EndPoint;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.BiDiOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.DeleteSelectedOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.ItemRegistrationOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.MoveBoxesOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.SwapEdgeEndPointOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.CreateEdgePlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.DragEdgePlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.IEnginePlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.ItemRegistrationPlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.SelectionPlugin.SelectionOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.SelectionPlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.TrackMousePlugin;

import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

@SuppressWarnings("deprecation")
public final class KineticEngine
{
	/**
	 * Definition of components to publicly invoke draw commands on.
	 */
	public static enum EngineComponent
	{
		STAGE,
		LAYER_BOXES,
		LAYER_EDGES,
		LAYER_SELECTION
	};
	
	/**
	 * The kinetic stage to draw everything in.
	 */
	private final Stage stage;
	
	/**
	 * The layer where boxes and related shapes will be drawn. This layer will have the lowest ZIndex.
	 */
	private final Layer layer1_boxes;
	
	/**
	 * The layer where edges and related shapes will be drawn. This layer will be above layer1 and below layer3.
	 */
	private final Layer layer2_edges;
	
	/**
	 * Special layer for selected content. This layer will have the highest ZIndex.
	 */
	private final Layer layer3_selection;
	
	/**
	 * A rectangle with dimensions identical to dimensions of the whole stage. This is solely to allow
	 * mouse events capturing outside of any other groups or shapes.
	 */
	private final Rectangle fillRectangle;
	
	/**
	 * A "rectangle" to display the selection area. The PathSVG shape is used to emulate the Rectangle shape because:
	 * 1) It doesn't cover the rectangle area and thus doesn't overdraw the shapes it intersects.
	 * 2) The Line shape no longer extends Polygon shape and can not consist of more than 2 points. 
	 */
	private final MultiSelectionRectangle multiSelectionRectangle;
	
	/**
	 * Plugins extend the functionality of the engine to allow nice and easy separation of problems and code. This avoids an incredible mess
	 * and hugely improves bug-free modifiability.
	 * Maps plugin ids to instances so each plugin is only held in a single instance.
	 */
	private final Map<String, IEnginePlugin> plugins;
	
	/**
	 * Plugins will want to attach event handlers/listeners to graph items.
	 * Maps names of graph items to a list of plugins that need to attach event handlers/listeners to them.
	 */
	private final Map<String, Set<IEnginePlugin>> pluginsForGraphItem;
	
	/*
	 * Individual plugins that will be used a lot in this class:
	 */
	private final ItemRegistrationPlugin itemRegistrationPlugin;
	private final SelectionPlugin selectionPlugin;
	
	/**
	 * The wrapper class providing contextual information and server communication.
	 */
	private final KineticComponentWidget context;
	
	/**
	 * All the event handlers of the engine.
	 */
	private final IEventListener fillRectangleMouseDownHandler = new IEventListener()
	{
		@Override
		public void handle(KineticEvent event)
		{
			// first deselect everything and draw selection layer if anything is deselected
			selectionPlugin.doSelectionRelatedOperation(SelectionOperation.DESELECTION, true, true, selectionPlugin.getSelectedBoxes());
						
			// preparation for mousemove
			multiSelectionRectangle.originalMultiSelectionRectanglePosition = getMousePosition();
			multiSelectionRectangle.getMasterNode().show();
			multiSelectionRectangle.getMasterNode().moveToTop();
			fillRectangle.moveToTop();
			
			fillRectangle.addEventListener(fillRectangleMouseMoveHandler, EventType.Basic.MOUSEMOVE);
			event.stopVerticalPropagation();
		}
	};
	private final IEventListener fillRectangleMouseMoveHandler = new IEventListener()
	{
		@Override
		public void handle(KineticEvent event)
		{
			Vector2d currentPosition = getMousePosition();
			multiSelectionRectangle.updatePath(
					new Vector2d(multiSelectionRectangle.originalMultiSelectionRectanglePosition.x, currentPosition.y),
					currentPosition,
					new Vector2d(currentPosition.x, multiSelectionRectangle.originalMultiSelectionRectanglePosition.y)
			);
			draw(EngineComponent.LAYER_SELECTION);
			event.stopVerticalPropagation();
		}
	};
	private final IEventListener fillRectangleMouseUpHandler = new IEventListener()
	{
		@Override
		public void handle(KineticEvent event)
		{
			// TODO: make this faster with smarter collision detection!
			
			// handle the event
			Box2d msrBox = multiSelectionRectangle.getPosAndSize();
			Set<BoxPrototype> boxesToSelect = new HashSet<BoxPrototype>();
			for(BoxPrototype box : itemRegistrationPlugin.getRegisteredBoxes())
			{
				if(!box.isSelected() && box.intersects(msrBox.getPosition(), msrBox.getSize()))
				{
					boxesToSelect.add(box);
				}
			}
			if(!boxesToSelect.isEmpty())
			{
				selectionPlugin.doSelectionRelatedOperation(SelectionOperation.SELECTION, false, true, (BoxPrototype[]) boxesToSelect.toArray());
			}
			
			// return the dynamic layer state to the original
			multiSelectionRectangle.getMasterNode().hide();
			fillRectangle.moveToBottom();
			fillRectangle.removeEventListener(EventType.Basic.MOUSEMOVE);
			
			// and draw
			draw(EngineComponent.STAGE);
			event.stopVerticalPropagation();
		}
	};
	
	/*
	 * TODO: 
	 * - pohybování nevybraných krabiček spojit s UNDO/REDO
	 * - až bude fungovat cancelBubble a zbavíme se fillRectanglu, bude možný i rovnou snadno rozšířit kreslení baseLine i na boxy
	 * - bug s algoritmem počítání hran (hrany jdou pres box)
	 * - vyhrát si s vlastníma kurzorama .cur soubory a ClientBundle zřejmě
	 * - update serialization/deserialization
	 */
	
	/*
	 * GENERAL NOTES ABOUT KINETIC BEHAVIOUR:
	 * - Each node can only have 1 parent (container) - a group or a layer! Beware of what happens when you cross add a node into a different layer...
	 * - When moving a node from a group to a layer, both layers need to be drawn, if distinct.
	 * - Hidden objects don't listen for events... makes sense :).
	 * - When a group is dragged, leaf draggable nodes get updated (X and Y attributes)... not the group itself. Make sure to set children not draggable :).
	 * - When something really odd happens:
	 * 		a) check whether event handlers are not "stealing" events with the "event.stopPropagation()" command, 
	 * 		b) debug thoroughly to check whether a silent javascript exception is caused (happens sometimes).
	 * - Events are fired in the order they were added, whether named or not.
	 * - Events can not be simulated from named events... bug?
	 */
	public KineticEngine(KineticComponentWidget context, final Stage stage) 
	{
		/*
		 * First setup the basic Kinetic variables.
		 */
		
		this.stage = stage;
		this.layer1_boxes = Kinetic.createLayer();
		this.layer2_edges = Kinetic.createLayer();
		this.layer3_selection = Kinetic.createLayer();
		this.stage.add(this.layer1_boxes);
		this.stage.add(this.layer2_edges);
		this.stage.add(this.layer3_selection);
		
		/*
		 * ZINDEX NOTES:
		 * ZIndex = value that determines priority for event handlers when 2 objects collide. The higher ZIndex, the closer to the user.
		 */
		
		setFirstArgHigherZIndex(layer2_edges, layer1_boxes);
		setFirstArgHigherZIndex(layer3_selection, layer2_edges);
		
		/*
		 * Setup internal layer variables and objects.
		 */
		
		this.fillRectangle = Kinetic.createRectangle(new Box2d(Vector2d.origin, stage.getSize()));
		this.fillRectangle.setOpacity(0); // hidden objects don't listen for events, so it has to be fully transparent instead
		this.fillRectangle.setDraggable(false);
		this.layer1_boxes.add(this.fillRectangle); // has to be the selection layer since it is the topmost layer by default
		setFillRectangleHandlers();
		
		/* 
		// TODO: simpler multi-selection using the stage... but event propagation bug!
		final IEventListener stageContentMouseOver = new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				System.out.println("MouseMove!");
			}
		};
		this.stage.addEventListener(EventTypeStageBasic.contentMousedown, new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				System.out.println("MouseDown!");
				stage.addEventListener(EventTypeStageBasic.contentMousemove, stageContentMouseOver);
			}
		});
		this.stage.addEventListener(EventTypeStageBasic.contentMouseup, new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				stage.removeEventListener(EventTypeStageBasic.contentMousemove);
				System.out.println("MouseUp!");
			}
		});
		*/
		
		this.multiSelectionRectangle = new MultiSelectionRectangle();
		this.layer3_selection.add(multiSelectionRectangle.getMasterNode());
		multiSelectionRectangle.getMasterNode().hide();
		
		/*
		 * Setup plugins.
		 * IMPORTANT: don't violate the call order - it is very important for correct functionality since plugins may depend upon each other. 
		 */
		this.plugins = new HashMap<String, IEnginePlugin>();
		this.pluginsForGraphItem = new HashMap<String, Set<IEnginePlugin>>();
		addPlugin(new SelectionPlugin(this));
		addPlugin(new ItemRegistrationPlugin(this));
		addPlugin(new TrackMousePlugin(this));
		addPlugin(new DragEdgePlugin(this));
		addPlugin(new CreateEdgePlugin(this));
		this.itemRegistrationPlugin = (ItemRegistrationPlugin) getPlugin(ItemRegistrationPlugin.pluginID);
		this.selectionPlugin = (SelectionPlugin) getPlugin(SelectionPlugin.pluginID);
		
		this.context = context;
		
		// and finally, draw the stage
		draw(EngineComponent.STAGE);
	}
	
	// *****************************************************************************************************
	// SERIALIZATION/DESERIALIZATION INTERFACE
	
	public ExperimentGraph toIntermediateFormat()
	{
		ExperimentGraph result = new ExperimentGraph();
		
		// first convert all boxes
		Map<BoxPrototype, String> boxToWebFormatID = new HashMap<BoxPrototype, String>();
		for(BoxPrototype box : itemRegistrationPlugin.getRegisteredBoxes())
		{
			Vector2d currentPosition = box.getAbsoluteNodePosition();
			box.getInfo().initialX = (int) currentPosition.x;
			box.getInfo().initialY = (int) currentPosition.y;
			boxToWebFormatID.put(box, result.addLeafBoxAndReturnID(box.getInfo()));
		}
		
		// then convert all edges
		Set<EdgePrototype> serializedEdges = new HashSet<EdgePrototype>(); 
		for(BoxPrototype box : itemRegistrationPlugin.getRegisteredBoxes())
		{
			for(EdgePrototype edge : box.connectedEdges)
			{
				if(!serializedEdges.contains(edge))
				{
					String fromBoxID = boxToWebFormatID.get(edge.getEndPoint(EndPoint.FROM));
					String toBoxID = boxToWebFormatID.get(edge.getEndPoint(EndPoint.TO));
					result.connect(fromBoxID, toBoxID);
					serializedEdges.add(edge);
				}
			}
		}
		
		return result;
	}
	
	public void fromIntermediateFormat(ExperimentGraph experiment)
	{
		// first convert all boxes
		Map<String, BoxPrototype> guiBoxes = new HashMap<String, BoxPrototype>();
		for(String leafBoxID : experiment.leafBoxes.keySet())
		{
			guiBoxes.put(leafBoxID, getContext().getShapeCreator().createBox(NodeRegisterType.MANUAL, experiment.leafBoxes.get(leafBoxID)));
		}
		
		// then convert all edges
		List<EdgePrototype> edges = new ArrayList<EdgePrototype>();
		for(Entry<String, Set<String>> entry : experiment.edges.entrySet())
		{
			BoxPrototype fromBox = guiBoxes.get(entry.getKey());
			for(String toLeafBoxID : entry.getValue())
			{
				BoxPrototype toBox = guiBoxes.get(toLeafBoxID);
				edges.add(getContext().getShapeCreator().createEdge(NodeRegisterType.MANUAL, fromBox, toBox));
			}
		}
		
		// and finally, put everything into the environment
		registerCreated(false, (BoxPrototype[]) guiBoxes.values().toArray(), (EdgePrototype[]) edges.toArray());
	}
	
	private String toJSON(EngineComponent component)
	{
		return getContainer(component).toJSON();
	}
	
	/**
	 * Only use this for debug purposes.
	 * @param component
	 * @param attrsToPrint
	 * @return
	 */
	@Deprecated()
	private String toMyJSON(EngineComponent component, JsArrayString attrsToPrint)
	{
		return getContainer(component).toMyJSON(attrsToPrint);
	}
	
	/**
	 * Method is currently buggy.
	 * @param component
	 * @param attrsToPrint
	 * @return
	 */
	@Deprecated
	private void fromJSON(String dLayerJSON, String edgeListJSON)
	{
		/*
		 * First reset the current state.
		 */
		
		/*
		 * TODO:
		 */
		
		//  selPlugin.doSelectionRelatedOperation(OperationKind.DESELECTION, false, selPlugin.getSelectedBoxes());
		// this.allBoxes.clear();
		// this.parentCanvas.getUndoRedoManager().clear();
		// this.parentCanvas.getShapeCreator().reset();
		
		this.layer1_boxes.destroyChildren();
		this.layer2_edges.destroyChildren(); // TODO: this destroys fillRectangle - wait for the next KineticJS version?
		
		/*
		 * Parse and build the graph.
		 */
		
		// create a virtual layer from which we will get all boxes and edges and register them in our own original layer
		Layer deserializedLayer = Kinetic.createNode(dLayerJSON).cast();
		
		// make clones of original objects
		Map<String, BoxPrototype> originalIdToBoxWithNewID = BoxPrototype.getInstancesFrom(getContext().getShapeCreator(), deserializedLayer);
		List<EdgePrototype> unbindedEdges = EdgePrototype.getInstancesFrom(getContext().getShapeCreator(), deserializedLayer);
		
		// register the clones in our engine
		List<ExperimentGraphItem> allItems = new ArrayList<ExperimentGraphItem>(originalIdToBoxWithNewID.values());
		allItems.addAll(unbindedEdges);
		// registerCreated(false, (ExperimentGraphItem[]) allItems.toArray()); // beware if edges are required to be connected (so far they are not)
		
		// bind and build
        Map<String, JSONArray> edgeBindings = jsonToEdgeList(edgeListJSON);
        for(Entry<String, JSONArray> edgeBinding : edgeBindings.entrySet())
        {
        	for(int i = 0; i < edgeBinding.getValue().size(); i++)
        	{
        		// bind variables
        		EdgePrototype edge = unbindedEdges.remove(0);
        		edge.setEndpoint(EndPoint.FROM, originalIdToBoxWithNewID.get(edgeBinding.getKey()));
        		edge.setEndpoint(EndPoint.TO, originalIdToBoxWithNewID.get(edgeBinding.getValue().get(i).isString().stringValue()));
        		
        		// bind graph components
        		edge.updateEdge();
        	}
        }
        
        // and finally, draw changes
        draw(EngineComponent.STAGE);
	}
	
	private String getEdgeListJSON()
	{
		Map<String, JSONArray> edgeList = new HashMap<String, JSONArray>();
		for(BoxPrototype box : itemRegistrationPlugin.getRegisteredBoxes())
		{
			JSONArray array = new JSONArray();
			for(EdgePrototype edge : box.connectedEdges)
			{
				if(edge.getEndPoint(EndPoint.FROM) == box)
				{
					array.set(array.size(), new JSONString(edge.getOtherEndpoint(box).getInfo().boxID));
				}
			}
			if(array.size() > 0)
			{
				edgeList.put(box.getInfo().boxID, array);
			}
		}
		return edgeListToJSON(edgeList);
	}
	
	private static String edgeListToJSON(Map<String, JSONArray> edgeList)
	{
	    String json = "";
	    if (edgeList != null && !edgeList.isEmpty())
	    {
	        JSONObject jsonObj = new JSONObject();
	        for (Entry<String, JSONArray> entry: edgeList.entrySet())
	        {
	            jsonObj.put(entry.getKey(), entry.getValue());
	        }
	        json = jsonObj.toString();
	        // System.out.println("Serialized JSON: " + json);
	    }
	    return json;
	}

	private static Map<String, JSONArray> jsonToEdgeList(String json)
	{
	    Map<String, JSONArray> result = new HashMap<String, JSONArray>();

	    JSONValue parsed = JSONParser.parseStrict(json);
	    JSONObject jsonObj = parsed.isObject();
	    if (jsonObj != null)
	    {
	        for (String key : jsonObj.keySet())
	        {
	        	result.put(key, jsonObj.get(key).isArray());
	        }
	    }
	    return result;
	}
	
	// *****************************************************************************************************
	// INTERFACE TO USE FROM CONTEXT
	
	/**
	 * This method only resets the engine, e.g. boxes, edges and selection. Further cleanup is expected to be done
	 * in the calling code.
	 */
	public void destroyGraphAndClearStage()
	{
		itemRegistrationPlugin.destroyGraphAndClearStage();
	}
	
	public void resize(int newWidth, int newHeight)
	{
		Vector2d newSize = new Vector2d(newWidth, newHeight); 
		stage.setSize(newSize);
		fillRectangle.setSize(newSize);
	}
	
	// *****************************************************************************************************
	// IMPLEMENTATION INDEPENDENT PUBLIC INTERFACE
	
	/*
	 * Undo/redo related wrapper routines.
	 */
	
	public void registerCreated(boolean applyChangeToHistory, BoxPrototype[] boxes, EdgePrototype[] edges) 
	{
		ItemRegistrationOperation operation = new ItemRegistrationOperation(this, boxes, edges);
		if(applyChangeToHistory)
		{
			pushNewOperation(operation);
		}
		operation.firstExecution();
	}
	
	public void deleteSelected()
	{
		DeleteSelectedOperation operation = new DeleteSelectedOperation(this);
		pushNewOperation(operation);
		operation.firstExecution();
	}
	
	public void moveSelected(Set<EdgePrototype> edgesInBetween)
	{
		MoveBoxesOperation operation = new MoveBoxesOperation(this, selectionPlugin.getSelectedKineticNodes(),
				(EdgePrototype[]) edgesInBetween.toArray());
		pushNewOperation(operation);
		operation.firstExecution();
	}
	
	public void swapEdgeEndpoint()
	{
		SwapEdgeEndPointOperation operation = new SwapEdgeEndPointOperation(this);
		pushNewOperation(operation);
		operation.firstExecution();
	}
	
	/*
	 * Box drag routines. 
	 */
	
	public void fromEdgesToBaseLines(Set<EdgePrototype> edges, BoxPrototype movingBox)
	{
		for(EdgePrototype edge : edges)
		{
			edge.endPointDrag_toBaseLine(movingBox != null ? movingBox : edge.getSelectedEndpoint());
		}
		draw(EngineComponent.LAYER_EDGES);
		draw(EngineComponent.LAYER_SELECTION);
	}
	
	public void updateBaseLines(Set<EdgePrototype> edges, BoxPrototype movingBox)
	{
		for(EdgePrototype edge : edges)
		{
			edge.endPointDrag_updateBaseLine(movingBox != null ? movingBox : edge.getSelectedEndpoint());
		}
		draw(EngineComponent.LAYER_EDGES);
	}
	
	public void fromBaseLinesToEdges(Set<EdgePrototype> edges)
	{
		for(EdgePrototype edge : edges)
		{
			edge.endPointDrag_toEdge();
		}
		draw(EngineComponent.LAYER_EDGES);
		draw(EngineComponent.LAYER_SELECTION);
	}
	
	/*
	 * Plugin related public routines.
	 */
	
	public IEnginePlugin getPlugin(String id)
	{
		return plugins.get(id);
	}
	
	public void addPlugin(IEnginePlugin plugin)
	{
		plugins.put(plugin.getPluginID(), plugin);
		String[] itemsToAttachTo = plugin.getItemsToAttachTo();
		if(itemsToAttachTo != null)
		{
			for(String graphItemName : itemsToAttachTo) 
			{
				if(!pluginsForGraphItem.containsKey(graphItemName))
				{
					// LinkedHashSet retains the insertion order which is very important here
					pluginsForGraphItem.put(graphItemName, new LinkedHashSet<IEnginePlugin>());
				}
				pluginsForGraphItem.get(graphItemName).add(plugin);
			}
		}
	}
	
	public void removePlugin(String pluginID)
	{
		plugins.remove(pluginID);
	}
	
	public void attachPluginHandlersTo(ExperimentGraphItem graphItem)
	{
		for(IEnginePlugin plugin : pluginsForGraphItem.get(GWTMisc.getSimpleName(graphItem.getClass())))
		{
			plugin.attachEventListeners(graphItem);
		}
	}
	
	public BoxPrototype getHoveredBox()
	{
		return ((TrackMousePlugin) getPlugin(TrackMousePlugin.pluginID)).getCurrentlyHoveredBox();
	}
	
	/*
	 * Miscellaneous public routines.
	 */
	
	public void reloadVisualStyle()
	{
		for(BoxPrototype box : itemRegistrationPlugin.getRegisteredBoxes())
		{
			box.reloadVisualStyle();
		}
		draw(EngineComponent.STAGE);
	}
	
	public KineticComponentWidget getContext()
	{
		return context;
	}
	
	public Container getContainer(EngineComponent component)
	{
		switch (component)
		{
			case LAYER_BOXES:
				return layer1_boxes;
			case LAYER_EDGES:
				return layer2_edges;
			case LAYER_SELECTION:
				return layer3_selection;
			case STAGE:
				return stage;
			default:
				throw new IllegalStateException();
		}
	}
	
	public Container getSelectionContainer()
	{
		return selectionPlugin.getSelectionContainer();
	}
	
	public Node getFillRectangle()
	{
		return fillRectangle;
	}
	
	public Vector2d getMousePosition()
	{
		return stage.getPointerPosition();
	}
	
	public void draw(EngineComponent component)
	{
		getContainer(component).draw();
	}
	
	public void removeFillRectangleHandlers()
	{
		this.fillRectangle.removeEventListener(EventType.Basic.MOUSEDOWN, EventType.Basic.MOUSEMOVE, EventType.Basic.MOUSEUP);
	}
	
	public void setFillRectangleHandlers()
	{
		removeFillRectangleHandlers();
		this.fillRectangle.addEventListener(fillRectangleMouseDownHandler, EventType.Basic.MOUSEDOWN);
		this.fillRectangle.addEventListener(fillRectangleMouseMoveHandler, EventType.Basic.MOUSEMOVE);
		this.fillRectangle.addEventListener(fillRectangleMouseUpHandler, EventType.Basic.MOUSEUP);
	}
	
	// *****************************************************************************************************
	// MISCELLANEOUS PRIVATE INTERFACE
	
	private void pushNewOperation(BiDiOperation operation)
	{
		getContext().getUndoRedoManager().push(operation);
	}
	
	private void setFirstArgHigherZIndex(Node node1, Node node2)
	{
		if(node1.getZIndex() == node2.getZIndex())
		{
			node1.setZIndex(node2.getZIndex() + 1);
		}
		else if(node1.getZIndex() < node2.getZIndex()) 
		{
			swapZIndexes(node1, node2);
		}
	}
	
	private void swapZIndexes(Node node1, Node node2)
	{
		int tempZIndex = node1.getZIndex();
		node1.setZIndex(node2.getZIndex());
		node2.setZIndex(tempZIndex);
	}
}