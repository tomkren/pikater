package org.pikater.web.vaadin.gui.client.extensions;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTLogger;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentConnector;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditorExtension;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.shared.ui.Connect;

@Connect(ExpEditorExtension.class)
public class ExpEditorExtensionConnector extends AbstractExtensionConnector
{
	private static final long serialVersionUID = 6766120104518020715L;
	
	// private final ExpEditorExtensionServerRpc serverRPC = RpcProxy.create(ExpEditorExtensionServerRpc.class, this);
	private int offsetWidth;
	private int offsetHeight;
	
	public ExpEditorExtensionConnector()
	{
		this.offsetWidth = 0;
		this.offsetHeight = 0;
		registerRpc(ExpEditorExtensionClientRpc.class, new ExpEditorExtensionClientRpc()
		{
			private static final long serialVersionUID = 560120982576334694L;
			
			@Override
			public void command_loadBoxPictures(final String[] pictureURLs)
			{
				// built-in image widget does exactly what we want, we only need to create an instance
				final Image[] imageWidgets = new Image[pictureURLs.length];
				for(int i = 0; i < pictureURLs.length; i++)
				{
					imageWidgets[i] = new Image(pictureURLs[i]);
				}
			}

			@Override
			public void command_resizeSelectedKineticComponent()
			{
				resizeSelectedKineticComponent();
			}
		});
	}
	
	//------------------------------------------------------------
	// INHERITED INTERFACE
	
	@Override
	protected void extend(ServerConnector target)
	{
		// first "extend" the client side widget
		// final Widget extendedWidget = ((ComponentConnector) target).getWidget(); // remember the extended client side widget
		Scheduler.get().scheduleDeferred(new ScheduledCommand()
		{
			@Override
			public void execute()
			{
				/*
				 * When the GWT event loop finishes and the widget is fully setup,
				 * remember spaces between the client window and experiment editor's
				 * master element.
				int windowWidth = Window.getClientWidth();
				int windowHeight = Window.getClientHeight();
				int expEditorWidth = extendedWidget.getOffsetWidth();
				int expEditorHeight = extendedWidget.getOffsetHeight();
				
				offsetWidth = windowWidth - expEditorWidth; 
				offsetHeight = windowHeight - expEditorHeight;
				
				GWTLogger.logWarning("Exp editor offset: " + String.valueOf(offsetWidth) + ";" + String.valueOf(offsetHeight));
				*/
			}
		});
		
		// then prevent accidental loss of work
		Window.addWindowClosingHandler(new Window.ClosingHandler()
		{
			public void onWindowClosing(Window.ClosingEvent closingEvent)
			{
				/*
				if(getState().modifiedTabsCount > 0)
				{
					closingEvent.setMessage("There's unsaved content in the editor. Really leave?");
				}
				*/
				
				closingEvent.setMessage("Really leave? Unsaved content will be lost.");
			}
		});
		
		// and finally, handle resize events
		Window.addResizeHandler(new ResizeHandler()
		{
			Timer resizeTimer = new Timer()
			{
				@Override
				public void run()
				{
					// extendedWidget.setWidth(String.valueOf(Window.getClientWidth() - offsetWidth) + "px");
					// extendedWidget.setHeight(String.valueOf(Window.getClientHeight() - offsetHeight) + "px");
					resizeSelectedKineticComponent();
				}
			};
			  
			@Override
			public void onResize(ResizeEvent event)
			{
				resizeTimer.cancel();
			    resizeTimer.schedule(250); // only actually resize when the window resize ends
			}
		});
	}
	
	@Override
	public ExpEditorExtensionSharedState getState()
	{
		return (ExpEditorExtensionSharedState) super.getState();
	}
	
	//------------------------------------------------------------
	// THE ACTUAL RESIZE RELATED INTERFACE
	
	private KineticComponentConnector getKineticConnectorByID(String id)
	{
		return (KineticComponentConnector) getConnection().getConnector(id, 0);
	}
	
	private void resizeSelectedKineticComponent()
	{
		if(getState().currentlySelectedKineticConnectorID != null)
		{
			getKineticConnectorByID(getState().currentlySelectedKineticConnectorID).getWidget().doResize();
		}
	}
}