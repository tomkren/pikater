package org.pikater.web.vaadin;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@PreserveOnRefresh // TODO: this may cause problems with debugging - restart instead of refresh might be needed
public class CustomConfiguredUI extends UI
{
	@Override
	protected void init(VaadinRequest request)
	{
		// TODO: to prevent errors from displaying on the client, rather send a notification and log the error:
		// Note: this is the last hook to catch the exception. If another error handler is set on a child component of UI, this error handler will not be used.
		setErrorHandler(new DefaultErrorHandler()
			{
				@Override
				public void error(com.vaadin.server.ErrorEvent event)
				{
					
				}
			}
		);
		
		/*
		 * NOTES ABOUT UI METHODS:
		 * - DetachListener - called when the UI's session expires
		 * 		- a UI expires when no requests are received by it from the client and after the client engine sends 3 keep-alive messages
		 * 		- all UIs of a session expire => session still remains and is cleaned up from the server when the session timeout configured in the web application expires
		 */
		
	}
}
