package org.pikater.web.vaadin.gui.client.kineticengine.graphitems;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;

public abstract class ExperimentGraphItem implements IShapeWrapper
{
	/**
	 * Indicator whether this item is currently selected in the editor.
	 */
	private boolean isSelected;
	
	/**
	 * The engine to register and call back to.
	 */
	private final KineticEngine kineticEngine;
	
	public ExperimentGraphItem(KineticEngine kineticEngine)
	{
		this.isSelected = false;
		this.kineticEngine = kineticEngine;
	}
	
	protected KineticEngine getKineticEngine()
	{
		return kineticEngine;
	}
	
	// ******************************************************************************************
	// PUBLIC INTERFACE
	
	/**
	 * Gets the selection state.
	 * @return Indicator, whether this item is currently selected or not.
	 */
	public boolean isSelected()
	{
		return isSelected;
	}
	
	/**
	 * Mandatory method to be called when selecting/deselecting an item, however it only
	 * changes the visual style and inner item's state. To achieve full selection/deselection,
	 * selection plugin must also be used.
	 */
	public void invertSelection()
	{
		// IMPORTANT: don't violate the call order. Not trivial code editing prone to errors would have to follow.
		
		// TODO: outsource the selection state to selection plugin?
		
		isSelected = !isSelected;
		invertSelectionProgrammatically();
		invertSelectionVisually();
	}
	
	public void setVisibleInKinetic(boolean visible)
	{
		if(visible)
		{
			if(getMasterNode().isRegistered())
			{
				throw new IllegalStateException("Item is already registered.");
			}
			else if(isSelected())
			{
				throw new IllegalStateException("Can not register a selected box.");
			}
			else
			{
				registerInKinetic();
			}
		}
		else
		{
			if(!getMasterNode().isRegistered())
			{
				throw new IllegalStateException("Can not remove an item that has not been registered.");
			}
			else if(isSelected())
			{
				throw new IllegalStateException("Can not remove a selected item.");
			}
			else
			{
				unregisterInKinetic();
			}
		}
	}
	
	/**
	 * Permanently destroys this item.
	 */
	public void destroy()
	{
		if(getMasterNode().isRegistered())
		{
			destroyInnerNodes();
		}
		else
		{
			throw new IllegalStateException("Can not destroy an item that is registered in the environment. Unregister first.");
		}
	}
	
	// ******************************************************************************************
	// ABSTRACT INTERFACE
	
	/**
	 * Register the item in kinetic's environment. The item is guaranteed not to be selected
	 * or registered.
	 * </br></br>
	 * TIPS:
	 * <ul>
	 * <li> Access the environment using the {@link #getKineticEngine()} method.
	 * <li> Use the {@link KineticEngine#getContainer(EngineComponent engineComponent)} method
	 * to access the right kinetic container.
	 * <li> Register the item with the {@link net.edzard.kinetic.Container#add()} method.
	 * </ul> 
	 */
	protected abstract void registerInKinetic();
	
	/**
	 * Unregister (remove) the item from kinetic's environment. The item is guaranteed to be
	 * registered and not to be selected.
	 */
	protected abstract void unregisterInKinetic();
	
	/**
	 * Method to permanently destroy all inner nodes that make up this item. This method
	 * needs to do nothing else than what it states.
	 */
	protected abstract void destroyInnerNodes();
	
	/**
	 * Method to change visual style of the child class and update its data structure.
	 */
	protected abstract void invertSelectionVisually();
	
	/**
	 * Method for any additional programmatic changes (exclusive to this item), if any
	 * are needed.
	 */
	protected abstract void invertSelectionProgrammatically();
	
	/**
	 * Gets the layer in which the item currently resides.
	 * @return the container layer
	 */
	public abstract EngineComponent getComponentToDraw();
}
