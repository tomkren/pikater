package org.pikater.web.filters;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.RequestReconstructor;
import org.pikater.web.RequestReconstructor.RequestComponent;

public abstract class AbstractFilter implements Filter
{
	private boolean DEBUG;
	
	//----------------------------------------------------------
	// SINGLE (AND SIMPLE) IMPLEMENTATION OF SOME INHERITED ROUTINES
	
	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		DEBUG = Boolean.parseBoolean(arg0.getServletContext().getInitParameter("DEBUG"));
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
			httpResponse.sendRedirect(redirectString.substring(1));
		}
		else
		{
			httpResponse.sendRedirect(redirectString);
		}
	}
	
	/**
	 * Server-side forward. Doesn't affect the client browser's address bar.
	 * @param servletRequest
	 * @param servletResponse
	 * @param forwardString
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void serverForward(ServletRequest servletRequest, ServletResponse servletResponse, String forwardString) throws IOException, ServletException
	{
		servletRequest.getRequestDispatcher(forwardString).forward(servletRequest, servletResponse);
	}
	
	//----------------------------------------------------------
	// PROGRAMMATIC HELPER ROUTINES
	
	/**
	 * <h1>First a little introduction to the problem this method solves:</h1>
	 * When checking the basic request URL pattern for the purpose of redirecting, two components are the
	 * most important:</br>
	 * <ol>
	 * <li> Servlet path - defined by {@link HttpServletRequest#getServletPath()}
	 * <li> Path info - defined by {@link HttpServletRequest#getPathInfo()}
	 * </ol>
	 * These methods may work with the servlet container to find out if a servlet is mapped to the
	 * request URL. If it is, the first method returns a non-null string depicting the servlet mapping
	 * and the second method returns the rest of the request URL. However, if the request is not directly
	 * mapped to a servlet, the first method returns null and the second returns the full request path info...
	 * </br></br>
	 * <h1>Now onto what this method actually does:</h1>
	 * This method tries to overcome the above issue by always inspecting the first absolute request URL component
	 * (the string between the first and second occurrences of the '/' character).
	 * @param httpRequest
	 * @return Whether the first path component is defined:</br>
	 * <ul>
	 * <li> "http://localhost:8080/Pikater" or "http://localhost:8080/Pikater/" => it is not
	 * <li> "http://localhost:8080/Pikater/index" => it is
	 * </ul>
	 */
	protected boolean isFirstPathComponentDefined(HttpServletRequest httpRequest)
	{
		String stringContainingTheFirstComponent = httpRequest.getServletPath(); // if non-null, always starts with a '/'
		if(stringContainingTheFirstComponent == null)
		{
			stringContainingTheFirstComponent = httpRequest.getPathInfo(); // if non-null, always starts with a '/' 
		}
		if(stringContainingTheFirstComponent == null)
		{
			throw new IllegalStateException("Both servlet path and path info are null. Can this even happen?");
		}
		return stringContainingTheFirstComponent.length() > 1; // a character is after the initial '/' character
	}
	
	//----------------------------------------------------------
	// DEBUG ROUTINES
	
	protected boolean isDebugMode()
	{
		return DEBUG;
	}
	
	protected void printRequestComponents(String filterName, HttpServletRequest httpRequest)
	{
		PikaterLogger.log(Level.WARNING, String.format("Filter '%s' intercepted request:\n"
				+ "Full URL: %s\n"
				+ "Derived servlet path: %s\n"
				+ "Derived path info: %s",
				filterName,
				RequestReconstructor.getRequestPrefix(httpRequest, RequestComponent.P7_QUERYSTRING),
				httpRequest.getServletPath(),
				httpRequest.getPathInfo()
		));
	}
}
