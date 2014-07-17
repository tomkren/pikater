package org.pikater.web.vaadin.gui.client.anchor;

import com.vaadin.shared.AbstractComponentState;

public class AnchorState extends AbstractComponentState
{
	private static final long serialVersionUID = -7896962673311413906L;
	
	public String text = "";
	public String hrefAttrContent = "";
	public boolean forwardClickToServer = false;
}