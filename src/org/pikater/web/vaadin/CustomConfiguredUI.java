package org.pikater.web.vaadin;

import org.pikater.shared.logging.PikaterLogger;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

public class CustomConfiguredUI extends UI
{
	private static final long serialVersionUID = 3280691990478021417L;

	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * Prevent errors from displaying on the client. Log the errors and rather send a notification
		 * to the client.
		 * NOTE: this is the last hook to catch the exception. If another error handler is set on a child
		 * component of a UI, this error handler will not be used.
		 */
		
		setErrorHandler(new DefaultErrorHandler()
		{
			private static final long serialVersionUID = -4395506937938101756L;

			@Override
			public void error(com.vaadin.server.ErrorEvent event)
			{
				PikaterLogger.logThrowable("UI default error handler caught the following error:", event.getThrowable());
				Notification.show("An error occured while processing your last request. Either try again or contact the administrators.", Type.ERROR_MESSAGE);
			}
		});
		
		/*
		 * NOTES ABOUT UI METHODS:
		 * - DetachListener - called when the UI's session expires
		 * 		- a UI expires when no requests are received by it from the client and after the client engine sends 3 keep-alive messages
		 * 		- all UIs of a session expire => session still remains and is cleaned up from the server when the session timeout configured in the web application expires
		 */
	}
}
