package org.pikater.web.vaadin.gui.server.components.borderlayout;

import org.pikater.web.vaadin.gui.client.extensions.AutoVerticalBorderLayoutExtensionClientRpc;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionMode;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionUnit;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Row;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;

@StyleSheet("autoVerticalBorderLayout.css")
public class AutoVerticalBorderLayout extends CustomLayout implements AutoVerticalBorderLayoutExtensionClientRpc
{
	private static final long serialVersionUID = 2971241997257911489L;
	
	private final AutoVerticalBorderLayoutExtension extension;
	
	public AutoVerticalBorderLayout()
	{
		super("autoVerticalBorderLayout");
		
		this.extension = new AutoVerticalBorderLayoutExtension();
		this.extension.extend(this);
	}
	
	public void setComponent(Border border, AbstractComponent component)
	{
		if(component == null)
		{
			throw new NullPointerException("The new component can not be null.");
		}
		addComponent(component, border.name());
	}
	
	public Component getComponent(Border border)
	{
		return getComponent(border.name());
	}
	
	//------------------------------------------------------------------
	// CLIENT COMMANDS FORWARDED
	
	@Override
	public void setRowHeight(Row row, DimensionMode dimMode)
	{
		extension.getClientRPC().setRowHeight(row, dimMode);
	}
	
	@Override
	public void setColumnWidth(final Column designatedColumn, double value, DimensionUnit unit)
	{
		extension.getClientRPC().setColumnWidth(designatedColumn, value, unit);
	}
	
	@Override
	public void addRowStyleName(Row row, String styleName)
	{
		extension.getClientRPC().addRowStyleName(row, styleName);
	}

	@Override
	public void removeRowStyleName(Row row, String styleName)
	{
		extension.getClientRPC().removeRowStyleName(row, styleName);
	}
	
	@Override
	public void addColumnStyleName(Column column, String styleName)
	{
		extension.getClientRPC().addColumnStyleName(column, styleName);
	}

	@Override
	public void removeColumnStyleName(Column column, String styleName)
	{
		extension.getClientRPC().removeColumnStyleName(column, styleName);
	}

	@Override
	public void setComponentVisible(Border border, boolean visible)
	{
		extension.getClientRPC().setComponentVisible(border, visible);
	}
	
	@Override
	public void setBorderSpacing(int pixels)
	{
		extension.getClientRPC().setBorderSpacing(pixels);
	}
	
	@Override
	public void setCellSpacing(int pixels)
	{
		extension.getClientRPC().setCellSpacing(pixels);	
	}

	@Override
	public void setCellPadding(int pixels)
	{
		extension.getClientRPC().setCellPadding(pixels);
	}
	
	@Override
	public void setRowVisible(Row row, boolean visible)
	{
		extension.getClientRPC().setRowVisible(row, visible);
	}

	@Override
	public void setColumnVisible(Column column, boolean visible)
	{
		extension.getClientRPC().setColumnVisible(column, visible);
	}
}