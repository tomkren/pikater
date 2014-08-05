package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views;

import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.web.vaadin.gui.server.components.anchor.Anchor;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.IContextForViews;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class BoxManagerSlotView extends AbstractBoxManagerView<Slot>
{
	private static final long serialVersionUID = 7575836463215779077L;
	
	private final Anchor link_slotName;

	public BoxManagerSlotView(IContextForViews context)
	{
		super(context);
		setStyleName("boxManagerSlotView");
		
		Label lbl_valueType = new Label("Slot:");
		lbl_valueType.setStyleName("emphasizedLabel");
		
		this.link_slotName = new Anchor("", "");
		this.link_slotName.addStyleName("emphasizedLabel");
		
		HorizontalLayout hl_slotIdentification = new HorizontalLayout();
		hl_slotIdentification.setStyleName("viewItem");
		hl_slotIdentification.setSpacing(true);
		hl_slotIdentification.addComponent(lbl_valueType);
		hl_slotIdentification.addComponent(this.link_slotName);
		
		addComponent(getBoxIdentificatinComponent());
		addComponent(hl_slotIdentification);
	}

	@Override
	protected void validateSource(Slot source) throws IllegalArgumentException
	{
		// accept all instances
	}

	@Override
	protected void refreshContent()
	{
		// TODO Auto-generated method stub
	}
}