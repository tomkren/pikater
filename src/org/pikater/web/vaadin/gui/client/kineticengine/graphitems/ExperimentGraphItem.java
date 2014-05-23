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
	 * Mandatory method to be called when selecting/deselecting an item.
	 */
	public void invertSelection()
	{
		// IMPORTANT: don't violate the call order. Untrivial code editing prone to errors would have to follow.
		isSelected = !isSelected;
		invertSelectionProgrammatically();
		invertSelectionVisually();
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
	 * Register the item in kinetic's environment. Access the environment using the "getKinetic()" method.
	 */
	public abstract void registerInKinetic();
	
	/**
	 * Unregister (remove) the item from kinetic environment.
	 */
	public abstract void unregisterInKinetic();
	
	/**
	 * Method to permanently destroy all inner nodes that make up this item.
	 */
	protected abstract void destroyInnerNodes();
	
	/**
	 * Method to change visual style of the child class and update its data structure.
	 */
	protected abstract void invertSelectionVisually();
	
	/**
	 * Method for any additional programmatic changes, if needed.
	 */
	protected abstract void invertSelectionProgrammatically();
	
	/**
	 * Gets the layer in which the item currently resides.
	 * @return the container layer
	 */
	public abstract EngineComponent getComponentToDraw();
}
