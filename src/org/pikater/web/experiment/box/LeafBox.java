package org.pikater.web.experiment.box;

import java.util.HashMap;
import java.util.Map;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.resources.ParamResource;
import org.pikater.shared.experiment.resources.Resource;
import org.pikater.shared.experiment.slots.AbstractSlot;
import org.pikater.shared.experiment.slots.SimpleSlot;

public class LeafBox extends AbstractBox
{
	public enum LeafBoxCategory
	{
		INPUT,
		SEARCHER,
		COMPUTING,
		VISUALIZER
	};
	
	public enum ParameterVisibility
	{
		USER_EDITABLE,
		INTERNAL
	};
	
	public enum SlotType
	{
		INPUT,
		OUTPUT
	};
	
	// -----------------------------------------------------------
	// EDITOR RELATED FIELDS SUITABLE TO BE DISPLAYED TO THE USER
	
	/**
	 * The collection of parameters that ARE NOT taken from input slots and ARE editable by the user.
	 * The map uses the "id" field of type @ParamInfo as key. See @ParamInfo for details.
	 */
	private final Map<ParamResource, AbstractParameter> editableNonInputParameters;
	
	// -----------------------------------------------------------
	// INTERNAL FIELDS NOT BEING DISPLAYED TO THE USER
	
	/**
	 * The collection of parameters that MAY OR MAY NOT be taken from input slots and ARE NOT editable by the user. This
	 * feature is useful for box inheritance and changing a select few parameters to ensure a different behaviour.
	 * The map uses the "id" field of type @ParamInfo as key. See @ParamInfo for details.
	 */
	private final Map<ParamResource, AbstractParameter> internalParameters;
	
	/**
	 * Input slots defined by this box. By default, all underlying data is required to be provided by connected boxes (incoming edges).
	 */
	private final Map<Resource, AbstractSlot> inputSlots;
	
	/**
	 * Output slots defined by this box. By default, all underlying data is automatically served to connected boxes (outgoing edges).
	 */
	private final Map<Resource, AbstractSlot> outputSlots;
	
	// -----------------------------------------------------------
	// CONSTRUCTOR
	
	public LeafBox(LogicalUnit boxConfig)
	{
		super(boxConfig.getDisplayName(), boxConfig.getDescription(), boxConfig.getPicture(), boxConfig.getType());
		
		this.editableNonInputParameters = new HashMap<ParamResource, AbstractParameter>();
		this.internalParameters = new HashMap<ParamResource, AbstractParameter>();
		this.inputSlots = new HashMap<Resource, AbstractSlot>();
		this.outputSlots = new HashMap<Resource, AbstractSlot>();
	}
	
	// ---------------------------------------------------------------------------
	// PUBLIC GETTERS
	
	public Map<ParamResource, AbstractParameter> getParameters(ParameterVisibility visibility)
	{
		switch (visibility)
		{
			case INTERNAL:
				return new HashMap<ParamResource, AbstractParameter>(internalParameters);
			case USER_EDITABLE:
				return new HashMap<ParamResource, AbstractParameter>(editableNonInputParameters);
			default:
				throw new IllegalStateException();
		}
	}
	
	public AbstractSlot getSlotByResource(SlotType type, Resource resource)
	{
		switch (type)
		{
			case INPUT:
				return inputSlots.get(resource);
			case OUTPUT:
				return outputSlots.get(resource);
			default:
				throw new IllegalStateException();
		}
	}
	
	public Map<Resource, AbstractSlot> getSlots(SlotType type)
	{
		switch (type)
		{
			case INPUT:
				return new HashMap<Resource, AbstractSlot>(inputSlots);
			case OUTPUT:
				return new HashMap<Resource, AbstractSlot>(outputSlots);
			default:
				throw new IllegalStateException();
		}
	}

	// ---------------------------------------------------------------------------
	// OTHER PUBLIC METHODS

	public void addParameter(ParameterVisibility visibility, ParamResource paramInfo, AbstractParameter param)
	{
		switch (visibility)
		{
			case INTERNAL:
				this.internalParameters.put(paramInfo, param);
				break;
			case USER_EDITABLE:
				this.editableNonInputParameters.put(paramInfo, param);
				break;
			default:
				throw new IllegalStateException();
		} 
	}
	
	public void addInputSlot(AbstractSlot slot)
	{
		addSlot(inputSlots, slot);
	}
	
	public void addOutputSlot(AbstractSlot slot)
	{
		addSlot(outputSlots, slot);
	}
	
	public void addOutputSlotFromInternalParam(ParameterVisibility visibility, ParamResource paramResource)
	{
		Map<ParamResource, AbstractParameter> map = null;
		switch (visibility)
		{
			case INTERNAL:
				map = this.internalParameters;
				break;
			case USER_EDITABLE:
				map = this.editableNonInputParameters;
				break;
			default:
				throw new IllegalStateException();
		}
		
		if(map.containsKey(paramResource))
		{
			addOutputSlot(SimpleSlot.getParamSlot(paramResource, null));
		}
		else
		{
			throw new IllegalStateException("Can not provide this parameter in an output slot because it is not defined on the specified visibility.");
		}
	}
	
	public boolean isOnlyConsumer()
	{
		return !inputSlots.isEmpty() && outputSlots.isEmpty();
	}
	
	public boolean isOnlyProvider()
	{
		return inputSlots.isEmpty() && !outputSlots.isEmpty();
	}
	
	// ---------------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void addSlot(Map<Resource, AbstractSlot> collectionByType, AbstractSlot slot)
	{
		if(collectionByType.containsKey(slot.resource))
		{
			throw new IllegalStateException("Slot was already added with the specified type.");
		}
		else
		{
			collectionByType.put(slot.resource, slot);
		}
	}	
}
