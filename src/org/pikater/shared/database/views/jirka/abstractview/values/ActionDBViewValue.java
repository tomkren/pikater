package org.pikater.shared.database.views.jirka.abstractview.values;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView.ColumnType;

public abstract class ActionDBViewValue extends AbstractDBViewValue<String>
{
	/**
	 * 
	 * @param value The name of the action, displayed in the GUI as a button.
	 */
	public ActionDBViewValue(String value)
	{
		super(ColumnType.ACTION, value, true);
	}
	
	@Override
	protected void commitEntities()
	{
		// no need to implement this in the view using this class
	}
	
	@Override
	protected void updateEntities(String newValue)
	{
		// no need to implement this in the view using this class
	}
	
	/**
	 * Method called when the user clicks the GUI button linked to this view value.
	 */
	public abstract void executeAction();
}
