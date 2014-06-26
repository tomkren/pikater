package org.pikater.web.vaadin.gui.server.components.tabledbview;

import org.pikater.shared.database.views.base.values.BooleanDBViewValue;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;

public class DBTableItemPropertyCheck implements Property<CheckBox>
{
	private static final long serialVersionUID = 8460699686386766346L;
	
	private final CheckBox checkBox;
	
	public DBTableItemPropertyCheck(final DBTable parentTable, final BooleanDBViewValue valueWrapper)
	{
		this.checkBox = new CheckBox(null, valueWrapper.getValue());
		this.checkBox.setImmediate(true);
		this.checkBox.addValueChangeListener(new ValueChangeListener()
		{
			private static final long serialVersionUID = -7967404862064544415L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				valueWrapper.setValue((Boolean) event.getProperty().getValue());
				if(parentTable.isImmediate())
				{
					valueWrapper.commit();
				}
			}
		});
	}

	@Override
	public CheckBox getValue()
	{
		return checkBox;
	}

	@Override
	public void setValue(CheckBox newValue) throws com.vaadin.data.Property.ReadOnlyException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<? extends CheckBox> getType()
	{
		return CheckBox.class;
	}

	@Override
	public boolean isReadOnly()
	{
		return true;
	}

	@Override
	public void setReadOnly(boolean newStatus)
	{
		throw new UnsupportedOperationException();
	}
}