package org.pikater.web;

import javax.servlet.http.HttpServletRequest;

public class RequestReconstructor
{
	public enum RequestComponent
	{
		/**
		 * E.g. "http".
		 */
		P1_SCHEME,
		
		/**
		 * E.g. "http://example.com".
		 */
		P2_SERVERNAME,
		
		/**
		 * E.g. "http://example.com:8080".
		 */
		P3_PORT,
		
		/**
		 * E.g. "http://example.com:8080/MyApplication".
		 */
		P4_APPCONTEXT,
		
		/**
		 * E.g. "http://example.com:8080/MyApplication/MyServlet".
		 */
		P5_SERVLETCONTEXT,
		
		/**
		 * E.g. "http://example.com:8080/MyApplication/MyServlet/{MyPath-with/}".
		 */
		P6_PATHINFO,
		
		/**
		 * E.g. "http://example.com:8080/MyApplication/MyServlet/{MyPath-with/}?MyQueryString". Including URI fragments that are contained within query string.
		 */
		P7_QUERYSTRING;
		
		/**
		 * <font color="red">RED ALERT:</font> this method assumes that we use HTTP/HTTPS.
		 * @param request
		 * @param builder
		 */
		public void writeRequestFragment(HttpServletRequest request, StringBuilder builder)
		{
			switch(this)
			{
				case P1_SCHEME:
					if(request.getScheme().equalsIgnoreCase("http") || request.getScheme().equalsIgnoreCase("https"))
					{
						builder.append(request.getScheme());
					}
					else
					{
						builder.append("http");
					}
					break;
				case P2_SERVERNAME:
					builder.append("://").append(request.getServerName());
					break;
				case P3_PORT:
					// port number 80 is omitted for HTTP and 443 for HTTPS
					if((request.getServerPort() != 80) && (request.getServerPort() != 443)) // we assume HTTP(S) is used
					{
						builder.append(":").append(request.getServerPort()); 
					}
					break;
				case P4_APPCONTEXT:
					builder.append(request.getContextPath());
					break;
				case P5_SERVLETCONTEXT:
					builder.append(request.getServletPath());
					break;
				case P6_PATHINFO:
					if(request.getPathInfo() != null)
					{
						builder.append(request.getPathInfo());
					}
					break;
				case P7_QUERYSTRING:
					if(request.getQueryString() != null)
					{
						builder.append("?").append(request.getQueryString());
					}
					break;
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}
	
	public static String getRequestPrefix(HttpServletRequest request, RequestComponent stopAt)
	{
		StringBuilder result = new StringBuilder();
		for(RequestComponent currentComponent : RequestComponent.values()) // returns the constants in the order they're declared!
		{
			if(currentComponent.compareTo(stopAt) <= 0)
			{
				currentComponent.writeRequestFragment(request, result);
			}
			else
			{
				break;
			}
		}
		return result.toString();
	}
}
