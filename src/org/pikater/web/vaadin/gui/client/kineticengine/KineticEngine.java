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
import org.pikater.web.vaadin.gui.client.kineticengine.modules.CreateEdgeModule;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.DragEdgeModule;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.ItemRegistrationModule;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.SelectionModule;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.TrackMouseModule;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.SelectionModule.SelectionOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.base.IEngineModule;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.BiDiOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.DeleteSelectedOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.ItemRegistrationOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.MoveBoxesOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.SwapEdgeEndPointOperation;

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
	private Stage stage;
	
	/**
	 * The layer where boxes and related shapes will be drawn. This layer will have the lowest ZIndex.
	 */
	private final Layer layer1_boxes;
	
	/**
	 * Special layer for selected content. This layer will have the highest ZIndex.
	 */
	private final Layer layer2_selection;
	
	/**
	 * The layer where edges and related shapes will be drawn. This layer will be above layer1 and below layer3.
	 */
	private final Layer layer3_edges;
	
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
	private final Map<String, IEngineModule> modules;
	
	/**
	 * Plugins will want to attach event handlers/listeners to graph items.
	 * Maps names of graph items to a list of plugins that need to attach event handlers/listeners to them.
	 */
	private final Map<String, Set<IEngineModule>> modulesForGraphItem;
	
	/*
	 * Individual plugins that will be used a lot in this class:
	 */
	private final ItemRegistrationModule itemRegistrationModule;
	private final SelectionModule selectionModule;
	
	/**
	 * The wrapper class providing contextual information and server communication.
	 */
	private KineticComponentWidget context;
	
	/**
	 * All the event handlers of the engine.
	 */
	private final IEventListener fillRectangleMouseDownHandler = new IEventListener()
	{
		@Override
		public void handle(KineticEvent event)
		{
			// first deselect everything and draw selection layer if anything is deselected
			selectionModule.doSelectionRelatedOperation(SelectionOperation.DESELECTION, true, true, selectionModule.getSelectedBoxes());
						
			// preparation for mousemove
			multiSelectionRectangle.setOriginalMousePosition(getMousePosition());
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
					new Vector2d(multiSelectionRectangle.getOriginalMousePosition().x, currentPosition.y),
					currentPosition,
					new Vector2d(currentPosition.x, multiSelectionRectangle.getOriginalMousePosition().y)
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
			for(BoxPrototype box : itemRegistrationModule.getRegisteredBoxes())
			{
				if(!box.isSelected() && box.intersects(msrBox.getPosition(), msrBox.getSize()))
				{
					boxesToSelect.add(box);
				}
			}
			if(!boxesToSelect.isEmpty())
			{
				selectionModule.doSelectionRelatedOperation(SelectionOperation.SELECTION, false, true, boxesToSelect.toArray(new BoxPrototype[0]));
			}
			
			// return the dynamic layer state to the original
			multiSelectionRectangle.reset();
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
	 * - Javascript can not convert objects to arbitrary type, so beware of using the "toArray()" method. Use "toArray(T[] array)" instead.
	 */
	
	/**
	 * Constructor.
	 * Remember that this constructor only creates and partly sets up inner fields. The
	 * {@link #setContext()} method needs to be used to fully initialize the engine. 
	 */
	public KineticEngine() 
	{
		/*
		 * First setup the basic Kinetic variables.
		 */
		
		this.stage = null;
		this.layer1_boxes = Kinetic.createLayer();
		this.layer2_selection = Kinetic.createLayer();
		this.layer3_edges = Kinetic.createLayer();
		
		/*
		 * Setup internal layer variables and objects.
		 */
		
		this.fillRectangle = Kinetic.createRectangle(new Box2d(Vector2d.origin, Vector2d.origin));
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
		this.layer2_selection.add(multiSelectionRectangle.getMasterNode());
		multiSelectionRectangle.getMasterNode().hide();
		
		/*
		 * Setup plugins.
		 * IMPORTANT: don't violate the call order - it is very important for correct functionality since plugins may depend upon each other. 
		 */
		this.modules = new HashMap<String, IEngineModule>();
		this.modulesForGraphItem = new HashMap<String, Set<IEngineModule>>();
		addModule(new TrackMouseModule(this));
		addModule(new SelectionModule(this));
		addModule(new ItemRegistrationModule(this));
		addModule(new DragEdgeModule(this));
		addModule(new CreateEdgeModule(this));
		this.itemRegistrationModule = (ItemRegistrationModule) getModule(ItemRegistrationModule.moduleID);
		this.selectionModule = (SelectionModule) getModule(SelectionModule.moduleID);
		
		this.context = null;
	}
	
	// *****************************************************************************************************
	// SERIALIZATION/DESERIALIZATION INTERFACE
	
	public ExperimentGraph toIntermediateFormat()
	{
		ExperimentGraph result = new ExperimentGraph();
		
		// first convert all boxes
		Map<BoxPrototype, String> boxToWebFormatID = new HashMap<BoxPrototype, String>();
		for(BoxPrototype box : itemRegistrationModule.getRegisteredBoxes())
		{
			Vector2d currentPosition = box.getAbsoluteNodePosition();
			box.getInfo().initialX = (int) currentPosition.x;
			box.getInfo().initialY = (int) currentPosition.y;
			boxToWebFormatID.put(box, result.addLeafBoxAndReturnID(box.getInfo()));
		}
		
		// then convert all edges
		Set<EdgePrototype> serializedEdges = new HashSet<EdgePrototype>(); 
		for(BoxPrototype box : itemRegistrationModule.getRegisteredBoxes())
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
		registerCreated(false, guiBoxes.values().toArray(new BoxPrototype[0]), edges.toArray(new EdgePrototype[0]));
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
		this.layer3_edges.destroyChildren(); // TODO: this destroys fillRectangle - wait for the next KineticJS version?
		
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
		for(BoxPrototype box : itemRegistrationModule.getRegisteredBoxes())
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
		itemRegistrationModule.destroyGraphAndClearStage();
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
		MoveBoxesOperation operation = new MoveBoxesOperation(this, selectionModule.getSelectedKineticNodes(),
				edgesInBetween.toArray(new EdgePrototype[0]));
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
	 * Module related public routines.
	 */
	
	public IEngineModule getModule(String id)
	{
		return modules.get(id);
	}
	
	public void addModule(IEngineModule module)
	{
		modules.put(module.getModuleID(), module);
		String[] itemsToAttachTo = module.getItemsToAttachTo();
		if(itemsToAttachTo != null)
		{
			for(String graphItemName : itemsToAttachTo) 
			{
				if(!modulesForGraphItem.containsKey(graphItemName))
				{
					// LinkedHashSet retains the insertion order which is very important here
					modulesForGraphItem.put(graphItemName, new LinkedHashSet<IEngineModule>());
				}
				modulesForGraphItem.get(graphItemName).add(module);
			}
		}
	}
	
	public void attachModuleHandlersTo(ExperimentGraphItem graphItem)
	{
		for(IEngineModule module : modulesForGraphItem.get(GWTMisc.getSimpleName(graphItem.getClass())))
		{
			module.attachEventListeners(graphItem);
		}
	}
	
	public BoxPrototype getHoveredBox()
	{
		return ((TrackMouseModule) getModule(TrackMouseModule.moduleID)).getCurrentlyHoveredBox();
	}
	
	/*
	 * Miscellaneous public routines.
	 */
	
	public KineticComponentWidget getContext()
	{
		return context;
	}
	
	public void setContext(KineticComponentWidget context)
	{
		/*
		 * First set up the stage.
		 */
		if(this.stage != null)
		{
			this.layer1_boxes.remove();
			this.layer2_selection.remove();
			this.layer3_edges.remove();
			this.stage.destroy();
		}
		this.stage = Kinetic.createStage(context.getStageDOMElement());
		this.stage.add(layer1_boxes);
		this.stage.add(layer2_selection);
		this.stage.add(layer3_edges);
		
		/*
		 * The set up z-indexes for layers.
		 * NOTE: ZIndex = value that determines priority for event handlers when 2 objects collide. The higher ZIndex, the closer to the user. 
		 */
		
		setFirstArgHigherZIndex(layer2_selection, layer1_boxes);
		setFirstArgHigherZIndex(layer3_edges, layer2_selection);
		
		// and do the rest
		this.fillRectangle.setSize(this.stage.getSize());
		this.context = context;
	}
	
	public Container getContainer(EngineComponent component)
	{
		switch (component)
		{
			case LAYER_BOXES:
				return layer1_boxes;
			case LAYER_EDGES:
				return layer3_edges;
			case LAYER_SELECTION:
				return layer2_selection;
			case STAGE:
				return stage;
			default:
				throw new IllegalStateException();
		}
	}
	
	public Container getSelectionContainer()
	{
		return selectionModule.getSelectionContainer();
	}
	
	public Node getFillRectangle()
	{
		return fillRectangle;
	}
	
	public Vector2d getMousePosition()
	{
		return stage.getPointerPosition();
	}
	
	public void reloadVisualStyle()
	{
		for(BoxPrototype box : itemRegistrationModule.getRegisteredBoxes())
		{
			box.applyUserSettings();
			for(EdgePrototype edge : box.connectedEdges)
			{
				edge.applyUserSettings();
			}
		}
		draw(EngineComponent.STAGE);
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
		// this.fillRectangle.addEventListener(fillRectangleMouseMoveHandler, EventType.Basic.MOUSEMOVE); // mouse down handler does this
		this.fillRectangle.addEventListener(fillRectangleMouseUpHandler, EventType.Basic.MOUSEUP);
	}
	
	// *****************************************************************************************************
	// MISCELLANEOUS PRIVATE INTERFACE
	
	private void pushNewOperation(BiDiOperation operation)
	{
		getContext().getHistoryManager().push(operation);
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