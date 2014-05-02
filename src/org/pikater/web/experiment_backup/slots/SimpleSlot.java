package org.pikater.web.experiment_backup.slots;

import org.pikater.web.experiment_backup.resources.DataResource;
import org.pikater.web.experiment_backup.resources.ParamResource;
import org.pikater.web.experiment_backup.resources.Resource;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Convenience class representing one to one connections.
 * Each instance creating static method provides a description field to programmatically assign semantic meaning to slots but
 * these descriptions are actually never used since they're not needed by the model itself.
 */
@XStreamAlias("slot")
public class SimpleSlot extends ArraySlot
{
	protected SimpleSlot(SlotContent content, Resource resource)
	{
		super(content, 1, resource);
	}
	
	public static SimpleSlot getDataSlot(DataResource resource, String description)
	{
		return new SimpleSlot(SlotContent.DATA, resource);
	}
	
	public static SimpleSlot getParamSlot(ParamResource resource, String description)
	{
		return new SimpleSlot(SlotContent.PARAMETER, resource);
	}
	
	public static SimpleSlot getErrorSlot(Resource resource, String description)
	{
		return new SimpleSlot(SlotContent.ERROR, resource);
	}
}
