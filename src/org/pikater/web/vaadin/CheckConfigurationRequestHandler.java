package org.pikater.web.vaadin;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;

@SuppressWarnings("serial")
public class CheckConfigurationRequestHandler implements RequestHandler
{
	@Override
	public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException
	{
		// if(ServerConfigurationInterface.instance.isEverythingOK())
		if(true)
	    {
			return false;
	    }
		else
		{
			// TODO: display login dialog to allow admins to configure and launch the application
			response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "The application has not been started correctly. Please, contact the administrators.");
			return true;
		}
		
		/*
		Iterator<UI> uis = session.getUIs().iterator();
        if (uis.hasNext())
        {
            // YOUR_LOGIC_HERE - throw an exception if any security check fails
        }
        */
	}
}
