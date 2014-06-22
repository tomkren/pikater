package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.toolboxes;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.components.toolbox.Toolbox;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.Label;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.VerticalLayout;

@StyleSheet("boxBrowserToolbox.css")
public class BoxBrowserToolbox extends Toolbox
{
	private static final long serialVersionUID = -1349487568799323284L;

	public BoxBrowserToolbox(String caption, ClickListener minimizeAction)
	{
		super(caption, minimizeAction);
		setSizeFull();
		
		Accordion content = new Accordion();
		content.setStyleName("boxBrowserToolbox-accordion");
		for(BoxType type : BoxType.values())
		{
			VerticalLayout vLayout = new VerticalLayout();
			vLayout.setSizeFull();
			vLayout.setStyleName("boxBrowserToolbox-accordion-content");
			for(AgentInfo agentInfo : ServerConfigurationInterface.getKnownAgents().getByType(type))
			{
				Label lbl = new Label(agentInfo.getName());
				lbl.setSizeUndefined();
				lbl.setData(agentInfo);
				
				DragAndDropWrapper dndWrapper = new DragAndDropWrapper(lbl);
				dndWrapper.setSizeFull();
				dndWrapper.setStyleName("boxBrowserToolbox-accordion-content-draggable");
				dndWrapper.setDragStartMode(DragStartMode.COMPONENT);
				dndWrapper.setData(agentInfo);
				
				vLayout.addComponent(dndWrapper);
			}
			
			// Tab newTab = content.addTab(vLayout, type.name());
			content.addTab(vLayout, type.name());
		}
		
		setToolboxContent(content);
	}
}
