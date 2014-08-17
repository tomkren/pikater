package org.pikater.web.vaadin.gui.server.layouts.matrixlayout;

import com.vaadin.ui.Label;

public interface IMatrixCaptionProvider<I extends Object>
{
	Label getCaptionComponent(I index);
}