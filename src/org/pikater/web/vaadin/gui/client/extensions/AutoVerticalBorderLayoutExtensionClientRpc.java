package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Border;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.DimensionMode;
import org.pikater.web.vaadin.gui.shared.BorderLayoutUtil.Row;

import com.vaadin.shared.communication.ClientRpc;

public interface AutoVerticalBorderLayoutExtensionClientRpc extends ClientRpc
{
	void setRowHeight(Row row, DimensionMode dimMode);
	void setColumnWidth(Column row, DimensionMode dimMode);
	void setRowVisible(Row row, boolean visible);
	void setColumnVisible(Column column, boolean visible);
	void setComponentVisible(Border border, boolean visible);
	void setBorderSpacing(int pixels);
	void setCellSpacing(int pixels);
	void setCellPadding(int pixels);
}