package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.web.vaadin.gui.shared.borderlayout.BorderLayoutUtil.Column;
import org.pikater.web.vaadin.gui.shared.borderlayout.BorderLayoutUtil.Row;
import org.pikater.web.vaadin.gui.shared.borderlayout.Dimension;

import com.vaadin.shared.communication.ClientRpc;

public interface AutoVerticalBorderLayoutExtensionClientRpc extends ClientRpc
{
	void setRowHeight(Row row, Dimension dimension);
	void setColumnWidth(Column designatedColumn, Dimension dimension);
	
	void addRowStyleName(Row row, String styleName);
	void removeRowStyleName(Row row, String styleName);
	void addColumnStyleName(Column column, String styleName);
	void removeColumnStyleName(Column column, String styleName);
	
	void setRowInvisible(Row affectedRow, Row rowToTakeUpTheAffectedRowsSpace);
	void setRowVisible(Row affectedRow);
	void setColumnInvisible(Column affectedColumn, Column columnToTakeUpTheAffectedColumnsSpace);
	void setColumnVisible(Column affectedColumn);
	
	void setBorderSpacing(int pixels);
	void setCellSpacing(int pixels);
	void setCellPadding(int pixels);
	void setFixedLayout(Dimension westDimension, Dimension centerDimension, Dimension eastDimension);
}