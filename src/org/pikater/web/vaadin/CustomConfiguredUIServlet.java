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

@SuppressWarnings("serial")
public class CustomConfiguredUIServlet extends VaadinServlet implements SessionInitListener, SessionDestroyListener
{
    @Override
    protected void servletInitialized() throws ServletException
    {
        super.servletInitialized(); // this should always be called first
        
        // Uncomment this if session listening is desirable: 
        // getService().addSessionInitListener(this);
        // getService().addSessionDestroyListener(this);
    }
    
    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException
    {
        VaadinServletService vaadinServletService = new VaadinServletService(this, deploymentConfiguration)
        {
        	@Override
            protected List<RequestHandler> createRequestHandlers() throws ServiceException
            {
        		// specify request handlers in the order they will be called (LIFO) on incoming requests:
                List<RequestHandler> requestHandlerList = super.createRequestHandlers();
                requestHandlerList.add(new CheckConfigurationRequestHandler());
                return requestHandlerList;
            }
        };
        vaadinServletService.init();
        return vaadinServletService;
    }
    
    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException
    {
        // Do session start stuff here
    }
    
    @Override
    public void sessionDestroy(SessionDestroyEvent event)
    {
        // Do session end stuff here
    }
}
