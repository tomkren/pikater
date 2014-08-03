package org.pikater.web.vaadin.gui.server.components.flowlayout;

import org.pikater.web.vaadin.gui.server.StyleBuilder;

import com.vaadin.ui.Component;

public interface IFlowLayoutStyleProvider
{
	void setStylesForInnerComponent(Component c, StyleBuilder builder);
}