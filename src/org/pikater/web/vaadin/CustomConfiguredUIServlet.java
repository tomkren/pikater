package org.pikater.web.vaadin;

import java.util.List;

import javax.servlet.ServletException;

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
        		/*
        		 * Specify request handlers in the order they will be called (LIFO) on incoming requests. This
        		 * is useful for constructing dynamic content where no UIs are necessary.
        		 */
                List<RequestHandler> requestHandlerList = super.createRequestHandlers();
                // requestHandlerList.add(new RequestHandler());
                return requestHandlerList;
            }
        };
        vaadinServletService.init();
        return vaadinServletService;
    }
    
    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException
    {
    	// do nothing
    }
    
    @Override
    public void sessionDestroy(SessionDestroyEvent event)
    {
    	/*
    	 * Manually log user out (if authenticated with this session) so that any eventual background threads
    	 * can correctly use the updated state. Also detach the session from {@link NoSessionStore} since it is
    	 * Vaadin independent.
    	 * IMPORTANT NOTE: this method might get called several times when closing a session because VaadinServlet
    	 * implementations of our UI objects extend this class. 
    	 */
    	
    	if(ManageAuth.isUserAuthenticated(event.getSession()))
    	{
    		ManageAuth.logout(event.getSession()); // also detaches the session from {@link NoSessionStore}
    	}
    	
    	/*
		 * IMPORTANT NOTES:
		 * - UIs are bound to a VaadinSession. We don't need to close and destroy the UIs because
    	 * that is already done automatically for us - their detach listeners are called.
		 * - A UI expires when no requests are received by it from the client and after the client engine sends 3 keep-alive messages.
		 * - All UIs of a session expire => session still remains and is cleaned up from the server when the session timeout configured in the web application expires.
		 */
    }
}
