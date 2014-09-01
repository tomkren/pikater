package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content;

import com.vaadin.navigator.View;

public interface IContentComponent extends View
{
	boolean isReadyToClose();
	String getCloseMessage();
	void beforeClose();
}