package org.pikater.web.vaadin.gui.server.components;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Image;

public class IconButton extends CustomComponent
{
	private static final long serialVersionUID = 4029033501431550617L;
	
	private final Image img;

	public IconButton(Resource source)
	{
		this.img = new Image(null, source);
		img.setStyleName("icon-button");
		setCompositionRoot(this.img);
	}
	
	public void addClickListener(ClickListener clickListener)
	{
		img.addClickListener(clickListener);
	}
}
