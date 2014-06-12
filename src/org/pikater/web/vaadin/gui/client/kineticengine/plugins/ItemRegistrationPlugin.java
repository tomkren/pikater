package org.pikater.web.vaadin.gui.client.kineticengine.plugins;

import java.util.HashSet;
import java.util.Set;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTMisc;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.SelectionPlugin.SelectionOperation;

public class ItemRegistrationPlugin implements IEnginePlugin
{
	public static String pluginID;
	
	/**
	 * The engine instance to work with.
	 */
	private final KineticEngine kineticEngine;
	
	/**
	 * Selection plugin is needed for manual deselection.
	 */
	private final SelectionPlugin selectionPlugin;
	
	/**
	 * A self-explanatory variable.
	 */
	private final Set<BoxPrototype> allRegisteredBoxes;
	
	/**
	 * Constructor.
	 * @param kineticEngine
	 */
	public ItemRegistrationPlugin(KineticEngine engine)
	{
		pluginID = GWTMisc.getSimpleName(this.getClass());
		this.kineticEngine = engine;
		this.selectionPlugin = (SelectionPlugin) engine.getPlugin(SelectionPlugin.pluginID);
		this.allRegisteredBoxes = new HashSet<BoxPrototype>();
	}
	
	// **********************************************************************************************
	// INHERITED INTERFACE
	
	@Override
	public String getPluginID()
	{
		return pluginID;
	}

	@Override
	public String[] getItemsToAttachTo()
	{
		return null;
	}

	@Override
	public void attachEventListeners(ExperimentGraphItem graphItem)
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
		
		public boolean registrationCheck(EdgePrototype edge)
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
	public void doOperation(RegistrationOperation opKind, boolean drawOnFinish, BoxPrototype... boxes)
	{
		// first deselect provided boxes, if necessary
		if(opKind == RegistrationOperation.UNREGISTER)
		{
			selectionPlugin.doSelectionRelatedOperation(SelectionOperation.DESELECTION, false, true, boxes);
		}
		
		// then do the action
		if(opKind == RegistrationOperation.REGISTER)
		{
			for(BoxPrototype box : boxes)
			{
				box.setVisibleInKinetic(true);
				allRegisteredBoxes.add(box);
			}
		}
		else
		{
			for(BoxPrototype box : boxes)
			{
				box.setVisibleInKinetic(false);
				allRegisteredBoxes.remove(box);
			}
		}

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
	public void doOperation(RegistrationOperation opKind, boolean drawOnFinish, EdgePrototype... edges)
	{
		boolean visible = opKind == RegistrationOperation.REGISTER;
		for(EdgePrototype edge : edges)
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
			if(opKind.registrationCheck(edge)) // only add each edge once (it has 2 endpoints)
			{
				edge.setVisibleInKinetic(visible);
			}
		}
		
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
		/*
		 * First deselect everything - selection plugin will not merge selections should this operation be unmade.
		 */
		selectionPlugin.doSelectionRelatedOperation(SelectionOperation.DESELECTION, false, true, getRegisteredBoxes());
		
		// then destroy edges
		for(BoxPrototype box : allRegisteredBoxes)
		{
			for(EdgePrototype edge : box.connectedEdges)
			{
				if(edge.getMasterNode().isRegistered()) // only destroy each edge once (it has 2 endpoints)
				{
					edge.setVisibleInKinetic(false);
					edge.destroy();
				}
			}
		}

		// destroy boxes
		for(BoxPrototype box : allRegisteredBoxes)
		{
			box.destroy();
		}

		// and final cleanup
		allRegisteredBoxes.clear();
	}
	
	// *****************************************************************************************************
	// OTHER PUBLIC INTERFACE
	
	public BoxPrototype[] getRegisteredBoxes()
	{
		return allRegisteredBoxes.toArray(new BoxPrototype[0]);
	}
}
