package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentConnector;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.AbstractGraphItemClient.VisualStyle;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.BoxHighlightExtension;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.visualstyle.KineticBoxSettings;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(BoxHighlightExtension.class)
public class BoxHighlightExtensionConnector extends AbstractExtensionConnector
{
	private static final long serialVersionUID = -699537664178182240L;
	
	@Override
	protected void extend(ServerConnector target)
	{
		final Widget extendedWidget = ((ComponentConnector) target).getWidget();
		extendedWidget.addDomHandler(new MouseOverHandler()
		{
			@Override
			public void onMouseOver(MouseOverEvent event)
			{
				extendedWidget.getElement().getStyle().setBackgroundColor(
						KineticBoxSettings.getColor(VisualStyle.HIGHLIGHTED_SLOT).toString()
				);
				getKineticConnectorByID(getState().kineticConnectorID).getWidget().highlightBoxes(getState().boxesToBeHighlighted);
			}
		}, MouseOverEvent.getType());
		extendedWidget.addDomHandler(new MouseOutHandler()
		{
			@Override
			public void onMouseOut(MouseOutEvent event)
			{
				extendedWidget.getElement().getStyle().clearBackgroundColor();
				getKineticConnectorByID(getState().kineticConnectorID).getWidget().cancelBoxHighlight();
			}
		}, MouseOutEvent.getType());
	}
	
	@Override
	public BoxHighlightExtensionSharedState getState()
	{
		return (BoxHighlightExtensionSharedState) super.getState();
	}
	
	private KineticComponentConnector getKineticConnectorByID(String id)
	{
		return (KineticComponentConnector) getConnection().getConnector(id, 0);
	}
}