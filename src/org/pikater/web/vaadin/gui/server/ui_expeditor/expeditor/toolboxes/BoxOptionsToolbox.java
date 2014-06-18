package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.toolboxes;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.web.vaadin.gui.server.components.toolbox.Toolbox;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Label;

@StyleSheet("boxOptionsToolbox.css")
public class BoxOptionsToolbox extends Toolbox
{
	private static final long serialVersionUID = -4029184252337221526L;

	public BoxOptionsToolbox(String caption, ClickListener minimizeAction)
	{
		super(caption, minimizeAction);
		setSizeFull();
		
		setToolboxContent(new Label("Poliket"));
	}
	
	public void setContentFrom(AgentInfo[] selectedBoxesInformation)
	{
		// TODO:
	}
}