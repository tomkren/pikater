package org.pikater.web.vaadin.gui.server.components.linklabel;

import org.pikater.web.vaadin.gui.client.linklabel.LinkLabelServerRpc;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractComponent;

import org.pikater.web.vaadin.gui.client.linklabel.LinkLabelState;

@StyleSheet("linkLabel.css")
public class LinkLabel extends AbstractComponent
{
	private static final long serialVersionUID = 2302390089747233688L;
	
	public enum LinkLabelColorConf
	{
		STANDARD,
		BANNER_AREA;
		
		public String toStyleName()
		{
			switch(this)
			{
				case BANNER_AREA:
					return "linkLabel-bannerArea";
				case STANDARD:
					return "linkLabel-standard";
				default:
					throw new IllegalStateException("Unknown state: " + this.name());
			}
		}
	}
	
	public LinkLabel(String text, final ClickListener listener)
	{
		this(text, LinkLabelColorConf.STANDARD, listener);
	}
	
	public LinkLabel(String text, LinkLabelColorConf conf, final ClickListener listener)
	{
		super();
		setStyleName(conf.toStyleName());
		
		registerRpc(new LinkLabelServerRpc()
		{
			private static final long serialVersionUID = 8647290803389019408L;

			@Override
			public void clicked(MouseEventDetails mouseDetails)
			{
				listener.click(new ClickEvent(LinkLabel.this, mouseDetails));
			}
		});
		
		setText(text);
	}
	
	public void setText(String text)
	{
		getState().text = text;
	}
	
	@Override
	protected LinkLabelState getState()
	{
		return (LinkLabelState) super.getState();
	}
}
