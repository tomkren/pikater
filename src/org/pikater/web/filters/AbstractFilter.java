package org.pikater.web.filters;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pikater.shared.logging.web.PikaterLogger;
import org.pikater.web.requests.HttpRequestUtils;

public abstract class AbstractFilter implements Filter
{
	//----------------------------------------------------------
	// SINGLE (AND SIMPLE) IMPLEMENTATION OF SOME INHERITED ROUTINES
	
	private ServletContext context = null;
	
	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		context = arg0.getServletContext();
	}
	
	@Override
	public void destroy()
	{
	}
	
	//----------------------------------------------------------
	// REQUEST DISPATCHING ROUTINES
	
	/**
	 * Client-side redirect. Affects the client browser's address bar.
	 * @param servletResponse
	 * @param redirectString
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void clientRedirect(ServletResponse servletResponse, String redirectString) throws IOException, ServletException
	{
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		if(redirectString.startsWith("/"))
		{
			redirectString = redirectString.substring(1);
		}
		httpResponse.sendRedirect(redirectString);
	}
	
	/**
	 * Deprecated: debug first - see client reditect above
	 * 
	 * Server-side forward. Doesn't affect the client browser's address bar.
	 * @param servletRequest
	 * @param servletResponse
	 * @param forwardString
	 * @throws IOException
	 * @throws ServletException
	 */
	@Deprecated
	protected void serverForward(ServletRequest servletRequest, ServletResponse servletResponse, String forwardString) throws IOException, ServletException
	{
		servletRequest.getRequestDispatcher(forwardString).forward(servletRequest, servletResponse);
	}
	
	//----------------------------------------------------------
	// PROGRAMMATIC HELPER ROUTINES
	
	protected boolean isServletPathDefined(HttpServletRequest httpRequest)
	{
		return HttpRequestUtils.getServletPathWhetherMappedOrNot(httpRequest) != null;
	}
	
	//----------------------------------------------------------
	// DEBUG ROUTINES
	
	protected boolean isApplicationInProductionMode()
	{
		return Boolean.parseBoolean(context.getInitParameter("productionMode"));
	}
	
	protected boolean isDebugMode()
	{
		return Boolean.parseBoolean(context.getInitParameter("printDebugInfoInFilters")); // TODO: rewrite to use ServerConfiguration
	}
	
	protected void printRequestComponents(String filterName, HttpServletRequest httpRequest)
	{
		PikaterLogger.log(Level.WARNING, String.format("Filter '%s' intercepted request:\n"
				+ "Full URL: %s\n"
				+ "Derived servlet path: %s\n",
				filterName,
				HttpRequestUtils.getFullURL(httpRequest),
				HttpRequestUtils.getServletPathWhetherMappedOrNot(httpRequest)
		));
	}
}