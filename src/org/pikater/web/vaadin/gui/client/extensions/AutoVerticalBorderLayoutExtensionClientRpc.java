package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionMode;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionUnit;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Row;

import com.vaadin.shared.communication.ClientRpc;

public interface AutoVerticalBorderLayoutExtensionClientRpc extends ClientRpc
{
	void setRowHeight(Row row, DimensionMode dimMode);
	void setColumnWidth(final Column designatedColumn, double value, DimensionUnit unit);
	
	void addRowStyleName(Row row, String styleName);
	void removeRowStyleName(Row row, String styleName); 
	void addColumnStyleName(Column column, String styleName);
	void removeColumnStyleName(Column column, String styleName);
	
	void setRowVisible(Row row, boolean visible);
	void setColumnVisible(Column column, boolean visible);
	void setComponentVisible(Border border, boolean visible);
	
	void setBorderSpacing(int pixels);
	void setCellSpacing(int pixels);
	void setCellPadding(int pixels);
}