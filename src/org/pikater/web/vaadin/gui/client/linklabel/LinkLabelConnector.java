package org.pikater.web.vaadin.gui.client.linklabel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

import org.pikater.web.vaadin.gui.server.components.linklabel.LinkLabel;
import org.pikater.web.vaadin.gui.client.linklabel.LinkLabelServerRpc;

import com.vaadin.client.communication.RpcProxy;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.vaadin.client.MouseEventDetailsBuilder;

import org.pikater.web.vaadin.gui.client.linklabel.LinkLabelClientRpc;
import org.pikater.web.vaadin.gui.client.linklabel.LinkLabelState;

import com.vaadin.client.communication.StateChangeEvent;

@Connect(LinkLabel.class)
public class LinkLabelConnector extends AbstractComponentConnector
{
	private static final long serialVersionUID = 5557617858638230393L;
	
	private final LinkLabelServerRpc rpc = RpcProxy.create(LinkLabelServerRpc.class, this);
	
	public LinkLabelConnector()
	{
		registerRpc(LinkLabelClientRpc.class, new LinkLabelClientRpc()
		{
			private static final long serialVersionUID = -8432862667383416641L;
		});

		getWidget().addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				if(isEnabled())
				{
					rpc.clicked(MouseEventDetailsBuilder.buildMouseEventDetails(
							event.getNativeEvent(),
							getWidget().getElement()
					));
				}
			}
		});
	}

	@Override
	protected Label createWidget()
	{
		return GWT.create(Label.class);
	}

	@Override
	public Label getWidget()
	{
		return (Label) super.getWidget();
	}

	@Override
	public LinkLabelState getState()
	{
		return (LinkLabelState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent)
	{
		super.onStateChanged(stateChangeEvent);
		 
		getWidget().setText(getState().text);
	}
}
