package org.pikater.web.vaadin.gui.client.kineticengine.graph;

import java.util.ArrayList;
import java.util.List;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;

public abstract class AbstractGraphItemClient implements IKineticShapeWrapper
{
	/**
	 * The engine to register and call back to.
	 */
	private final KineticEngine kineticEngine;
	
	/**
	 * Indicator whether this item is currently selected in the editor.
	 */
	private boolean isSelected;
	
	/**
	 * The list to update the looks of your graph items from. Styles are applied
	 * in the order they were added to this list, so the later the more priority.
	 */
	private final List<VisualStyle> appliedVisualStyles;
	
	public enum VisualStyle
	{
		SELECTED,
		NOT_SELECTED,
		HIGHLIGHTED,
		NOT_HIGHLIGHTED;
		
		public VisualStyle getComplementStyle()
		{
			switch(this)
			{
				case HIGHLIGHTED:
					return NOT_HIGHLIGHTED;
				case NOT_HIGHLIGHTED:
					return HIGHLIGHTED;
				case NOT_SELECTED:
					return SELECTED;
				case SELECTED:
					return NOT_SELECTED;
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		public static List<VisualStyle> getDefault()
		{
			List<VisualStyle> result = new ArrayList<AbstractGraphItemClient.VisualStyle>();
			result.add(NOT_SELECTED);
			result.add(NOT_HIGHLIGHTED);
			return result;
		}
	}
	
	// ******************************************************************************************
	// CONSTRUCTOR
	
	public AbstractGraphItemClient(KineticEngine kineticEngine)
	{
		this.isSelected = false;
		this.kineticEngine = kineticEngine;
		this.appliedVisualStyles = VisualStyle.getDefault();
	}
	
	// ******************************************************************************************
	// PROTECTED INTERFACE
	
	protected KineticEngine getKineticEngine()
	{
		return kineticEngine;
	}
	
	// ******************************************************************************************
	// PUBLIC INTERFACE
	
	public void setVisualStyle(VisualStyle style)
	{
		if(appliedVisualStyles.add(style))
		{
			appliedVisualStyles.remove(style.getComplementStyle());
			reloadVisualStyles(false);
		}
	}
	
	public void reloadVisualStyles(boolean alsoDraw)
	{
		for(VisualStyle style : appliedVisualStyles)
		{
			applyVisualStyle(style);
		}
		if(alsoDraw)
		{
			kineticEngine.getContainer(getComponentToDraw()).draw();
		}
	}
	
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
		setVisualStyle(isSelected ? VisualStyle.SELECTED : VisualStyle.NOT_SELECTED);
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
				setRegisteredInKinetic(true);
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
				setRegisteredInKinetic(false);
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
	 * Used when a global setting (common for all instance of an item) changes.
	 */
	public abstract void applyUserSettings();
	
	/**
	 * Method called to change the visual style of inner components and accept some changes.
	 */
	protected abstract void applyVisualStyle(VisualStyle style);
	
	/**
	 * If to be registered, the item is guaranteed not to be selected or registered. Otherwise,
	 * it is guaranteed to be registered and not to be selected.
	 * </br></br>
	 * TIPS:
	 * <ul>
	 * <li> Access the environment using the {@link #getKineticEngine()} method.
	 * <li> Use the {@link KineticEngine#getContainer(EngineComponent engineComponent)} method
	 * to access the right kinetic container.
	 * <li> Register the item with the {@link net.edzard.kinetic.Container#add()} method.
	 * </ul> 
	 */
	protected abstract void setRegisteredInKinetic(boolean registered);
	
	/**
	 * Method to permanently destroy all inner nodes that make up this item. This method
	 * needs to do nothing else than what it states.
	 */
	protected abstract void destroyInnerNodes();
	
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