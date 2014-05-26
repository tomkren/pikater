package org.pikater.web.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.pikater.shared.logging.PikaterLogger;

import com.vaadin.server.FileResource;

@SuppressWarnings("serial")
@WebServlet(value = "/staticDownload", asyncSupported = true, loadOnStartup = 1)
public class StaticDownloadServlet extends HttpServlet
{
	private static final int DEFAULT_BUFFER_SIZE = 2048;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		FileResource resource = new FileResource(new File("/Users/Smolodon/Downloads/Daisy (2006) JK HK.avi"));
		if(resource.getSourceFile().length() > Integer.MAX_VALUE) // "HttpServletResponse.setContentLength()" only accepts int type
		{
			PikaterLogger.log(Level.SEVERE, String.format("The file '%s' was not served because it is too large (larger than MAX_INT).", resource.getFilename()));
			resp.sendError(HttpStatus.SC_BAD_REQUEST, "The requested file was too large to serve.");
		}
		else
		{
			resp.setContentType(resource.getMIMEType());
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"");
			resp.setContentLength((int)resource.getSourceFile().length());
			
			// TODO: set cache time or other things, if you mean to
			
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			OutputStream output = resp.getOutputStream();
	        FileInputStream input = null;
	        int length;
	        try
	        {
	            input = new FileInputStream(resource.getSourceFile());
	            while ((length = input.read(buffer)) > 0)
	            {
	                output.write(buffer, 0, length);
	            }
	        }
	        catch (IOException e)
	        {
	        	PikaterLogger.logThrowable(
	        			String.format("Client most likely disconnected or aborted transferring the file '%s' but this needs to be logged anyway.", resource.getFilename()), e);
	        }
	        finally
	        {
	        	if(input != null)
	        	{
	        		input.close();
	        	}
	        }
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		super.doPost(req, resp);
	}
}