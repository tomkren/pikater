package org.pikater.web.vaadin;

import java.util.List;

import javax.servlet.ServletException;

import org.pikater.web.vaadin.gui.server.AuthHandler;

import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;

public class CustomConfiguredUIServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener
{
	private static final long serialVersionUID = -8268135994612358127L;

	@Override
    protected void servletInitialized() throws ServletException
    {
        super.servletInitialized(); // this should always be called first
        
        getService().addSessionInitListener(this);
        getService().addSessionDestroyListener(this);
    }
    
    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException
    {
        VaadinServletService vaadinServletService = new VaadinServletService(this, deploymentConfiguration)
        {
			private static final long serialVersionUID = 8833521650509773424L;

			@Override
            protected List<RequestHandler> createRequestHandlers() throws ServiceException
            {
        		// specify request handlers in the order they will be called (LIFO) on incoming requests:
                List<RequestHandler> requestHandlerList = super.createRequestHandlers();
                // requestHandlerList.add(new CheckConfigurationRequestHandler());
                return requestHandlerList;
            }
        };
        vaadinServletService.init();
        return vaadinServletService;
    }
    
    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException
    {
        // no need to do anything
    }
    
    @Override
    public void sessionDestroy(SessionDestroyEvent event)
    {
    	/*
    	 * Manually log user out, if authenticated with this session (so that any eventual background threads
    	 * can correctly use the updated state), and detach the session from {@link NoSessionStore} since it is
    	 * Vaadin independent.
    	 */
    	
    	if(AuthHandler.isUserAuthenticated(event.getSession()))
    	{
    		AuthHandler.logout(event.getSession()); // also detaches the session from {@link NoSessionStore}
    	}
    }
}
