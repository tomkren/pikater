package org.pikater.web.vaadin.gui.server.components.dbviews.tableview;

import org.pikater.shared.database.views.base.values.RepresentativeDBViewValue;

import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;

public class DBTableItemPropertyCombo implements Property<ComboBox>
{
	private static final long serialVersionUID = -5919644785774086999L;
	
	private final ComboBox comboBox;

	public DBTableItemPropertyCombo(final DBTable parentTable, final RepresentativeDBViewValue valueWrapper)
	{
		this.comboBox = new ComboBox(null, valueWrapper.getValues());
		this.comboBox.setValue(valueWrapper.getValue());
		this.comboBox.setNullSelectionAllowed(false);
		this.comboBox.setTextInputAllowed(false);
		this.comboBox.setNewItemsAllowed(false);
		this.comboBox.setImmediate(true);
		this.comboBox.addValueChangeListener(new ValueChangeListener()
		{
			private static final long serialVersionUID = -6175606221977226773L;

			@Override
			public void valueChange(ValueChangeEvent event)
			{
				valueWrapper.setValue((String) event.getProperty().getValue());
				if(parentTable.isImmediate())
				{
					valueWrapper.commit();
				}
			}
		});
	}
	
	@Override
	public ComboBox getValue()
	{
		return comboBox;
	}
	
	@Override
	public void setValue(ComboBox newValue) throws com.vaadin.data.Property.ReadOnlyException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<? extends ComboBox> getType()
	{
		return ComboBox.class;
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