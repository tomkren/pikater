package org.pikater.web.vaadin.gui.server.components.popups;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

public class MyPopup extends Window
{
	private static final long serialVersionUID = 332265534464591922L;

	public MyPopup(String caption)
	{
		super(caption, null);
		setStyleName("myPopup");
		
		setModal(true);
		setDraggable(true);
		setResizable(true);
		setClosable(true);
		setCloseShortcut(KeyCode.ESCAPE, null);
	}
	
	public MyPopup(String caption, Component content)
	{
		this(caption);
		
		setContent(content);
	}
	
	public void show()
	{
		UI.getCurrent().addWindow(this);
		center();
		focus(); // to allow immediate close via escape key
	}
}