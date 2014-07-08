package org.pikater.web.vaadin.gui.client.kineticengine.modules;

import java.util.HashSet;
import java.util.Set;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTMisc;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.BoxGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.EdgeGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.AbstractGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.SelectionModule.SelectionOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.base.IEngineModule;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.BoxGraphItemShared;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.EdgeGraphItemShared;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.GraphItemSetChange;

public class ItemRegistrationModule implements IEngineModule
{
	public static String moduleID;
	
	/**
	 * The engine instance to work with.
	 */
	private final KineticEngine kineticEngine;
	
	/**
	 * Selection plugin is needed for manual deselection.
	 */
	private final SelectionModule selectionModule;
	
	/**
	 * A self-explanatory variable.
	 */
	private final Set<BoxGraphItemClient> allRegisteredBoxes;
	
	/**
	 * Constructor.
	 * @param kineticEngine
	 */
	public ItemRegistrationModule(KineticEngine engine)
	{
		moduleID = GWTMisc.getSimpleName(this.getClass());
		this.kineticEngine = engine;
		this.selectionModule = (SelectionModule) engine.getModule(SelectionModule.moduleID);
		this.allRegisteredBoxes = new HashSet<BoxGraphItemClient>();
	}
	
	// **********************************************************************************************
	// INHERITED INTERFACE
	
	@Override
	public String getModuleID()
	{
		return moduleID;
	}

	@Override
	public String[] getItemsToAttachTo()
	{
		return null;
	}

	@Override
	public void attachEventListeners(AbstractGraphItemClient graphItem)
	{
	}
	
	// *****************************************************************************************************
	// PUBLIC TYPES AND INTERFACE TO PERFORM ALL ITEM REGISTRATION/UNREGISTRATION RELATED OPERATIONS
	
	public enum RegistrationOperation
	{
		/**
		 * Register and display items immediately.
		 */
		REGISTER,
		
		/**
		 * Remove items from the graph immediately but don't destroy or disconnect them.
		 */
		UNREGISTER;
		
		public boolean registrationCheck(EdgeGraphItemClient edge)
		{
			boolean registered = edge.getMasterNode().isRegistered();
			return this == REGISTER ? !registered : registered; // only register unregistered edges and vice versa
		}
	}
	
	/**
	 * Does the operation corresponding to the arguments. The following things are
	 * required to be done in the calling code:</br>
	 * <ul>
	 * <li> Proper initialization of the given items.
	 * </ul> 
	 * @param opKind
	 * @param drawOnFinish
	 * @param boxes
	 */
	public void doOperation(RegistrationOperation opKind, boolean drawOnFinish, BoxGraphItemClient... boxes)
	{
		// first deselect provided boxes, if necessary
		if(opKind == RegistrationOperation.UNREGISTER)
		{
			selectionModule.doSelectionRelatedOperation(SelectionOperation.DESELECTION, false, true, boxes);
		}
		
		// then do the action
		if(opKind == RegistrationOperation.REGISTER)
		{
			for(BoxGraphItemClient box : boxes)
			{
				box.setVisibleInKinetic(true);
				allRegisteredBoxes.add(box);
			}
		}
		else
		{
			for(BoxGraphItemClient box : boxes)
			{
				box.setVisibleInKinetic(false);
				allRegisteredBoxes.remove(box);
			}
		}
		
		// send info to the server
		kineticEngine.getContext().command_itemSetChange(GraphItemSetChange.DELETION, BoxGraphItemShared.fromArray(boxes));

		// and finally, request redraw of the stage
		if(drawOnFinish)
		{
			kineticEngine.draw(EngineComponent.STAGE);
		}
	}
	
	/**
	 * Does the operation corresponding to the arguments. The following things are
	 * required to be done in the calling code:</br>
	 * <ul>
	 * <li> Proper initialization of the given items.
	 * </ul>
	 * @param opKind
	 * @param drawOnFinish
	 * @param edges
	 */
	public void doOperation(RegistrationOperation opKind, boolean drawOnFinish, EdgeGraphItemClient... edges)
	{
		boolean visible = opKind == RegistrationOperation.REGISTER;
		for(EdgeGraphItemClient edge : edges)
		{
			if(edge.isSelected())
			{
				// edges are assumed to have been deselected in the other "doOperation" method
				throw new IllegalStateException("Can not register or deregister a selected edge. Deselect first.");
			}
			if(edge.areBothEndsDefined())
			{
				edge.setEdgeRegisteredInEndpoints(visible);
			}
			if(opKind.registrationCheck(edge)) // don't register in kinetic again, if the edge is already registered
			{
				edge.setVisibleInKinetic(visible);
			}
		}
		
		// send info to the server
		kineticEngine.getContext().command_itemSetChange(GraphItemSetChange.DELETION, EdgeGraphItemShared.fromArray(edges));
		
		if(drawOnFinish)
		{
			kineticEngine.draw(EngineComponent.STAGE);
		}
	}
	
	/**
	 * Issues a command to clear the graph and remove/destroy all the items.</br>
	 * This operation only resets the engine, e.g. boxes, edges and selection. Further cleanup is expected to be done
	 * in the calling code.
	 */
	public void destroyGraphAndClearStage()
	{
		// first deselect everything - selection plugin will not merge selections should this operation be unmade
		selectionModule.doSelectionRelatedOperation(SelectionOperation.DESELECTION, false, true, getRegisteredBoxes());
		
		// then destroy edges
		Set<EdgeGraphItemClient> destroyedEdges = new HashSet<EdgeGraphItemClient>();
		for(BoxGraphItemClient box : allRegisteredBoxes)
		{
			for(EdgeGraphItemClient edge : box.connectedEdges)
			{
				if(edge.getMasterNode().isRegistered()) // only destroy each edge once (it has 2 endpoints)
				{
					edge.setVisibleInKinetic(false);
					edge.destroy();
					if(edge.areBothEndsDefined()) // destroying an item is required to keep programmatic fields intact
					{
						destroyedEdges.add(edge);
					}
					else
					{
						throw new IllegalStateException("Edges were destroyed and apparently also "
								+ "unregistered from connected boxes. The connection has to be kept intact.");
					}
				}
			}
		}
		
		// send info to the server
		kineticEngine.getContext().command_itemSetChange(GraphItemSetChange.DELETION, BoxGraphItemShared.fromArray(getRegisteredBoxes()));
		kineticEngine.getContext().command_itemSetChange(GraphItemSetChange.DELETION, EdgeGraphItemShared.fromArray(destroyedEdges.toArray(new EdgeGraphItemClient[0])));

		// destroy boxes
		for(BoxGraphItemClient box : allRegisteredBoxes)
		{
			box.destroy();
		}

		// and final cleanup
		allRegisteredBoxes.clear();
	}
	
	// *****************************************************************************************************
	// OTHER PUBLIC INTERFACE
	
	public BoxGraphItemClient[] getRegisteredBoxes()
	{
		return allRegisteredBoxes.toArray(new BoxGraphItemClient[0]);
	}
}
