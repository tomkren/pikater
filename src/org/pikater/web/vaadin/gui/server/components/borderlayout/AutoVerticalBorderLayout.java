package org.pikater.web.vaadin.gui.server.components.borderlayout;

import org.pikater.web.vaadin.gui.client.extensions.AutoVerticalBorderLayoutExtensionClientRpc;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionMode;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Row;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;

@StyleSheet("autoVerticalBorderLayout.css")
public class AutoVerticalBorderLayout extends CustomComponent implements AutoVerticalBorderLayoutExtensionClientRpc
{
	private static final long serialVersionUID = 2971241997257911489L;
	
	private final CustomLayout innerLayout;
	private final AutoVerticalBorderLayoutExtension extension;
	
	public AutoVerticalBorderLayout()
	{
		super();
		
		this.innerLayout = new CustomLayout("autoVerticalBorderLayout");
		this.innerLayout.setSizeFull();
		this.extension = new AutoVerticalBorderLayoutExtension();
		this.extension.extend(innerLayout);
		
		setCompositionRoot(this.innerLayout);
	}
	
	public void setComponent(Border border, AbstractComponent component)
	{
		if(component == null)
		{
			throw new NullPointerException("The new component can not be null.");
		}
		innerLayout.addComponent(component, border.name());
	}
	
	public Component getComponent(Border border)
	{
		return innerLayout.getComponent(border.name());
	}
	
	public void fillWithTestPanels()
	{
		for(Border border : Border.values())
		{
			setComponent(border, createTestComponent());
		}
	}
	
	//------------------------------------------------------------------
	// CLIENT COMMANDS FORWARDED
	
	@Override
	public void setRowHeight(Row row, DimensionMode dimMode)
	{
		extension.getClientRPC().setRowHeight(row, dimMode);
	}

	@Override
	public void setColumnWidth(Column row, DimensionMode dimMode)
	{
		extension.getClientRPC().setColumnWidth(row, dimMode);
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
	
	//------------------------------------------------------------------
	// MISCELLANEOUS
	
	private Label createTestComponent()
	{
		Label lbl = new Label("something");
		lbl.setSizeFull();
		return lbl;
	}
}