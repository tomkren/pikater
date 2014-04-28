package org.pikater.web.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class GeneralServletInterface
{
	// ***************************************************************************************************
	// OTHER HELPFUL INTERFACE
	
	public static void setTextResponse(HttpServletResponse resp, String message) throws IOException
	{
		resp.setContentType("text/plain; charset=UTF-8");
		resp.getWriter().println(message);
	}

	public static void setHTMLResponse(HttpServletResponse resp, String html) throws IOException
	{
		resp.setContentType("text/html; charset=UTF-8");
		resp.getWriter().println(html);
	}
}
