package org.pikater.web.servlets.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;

@WebServlet(value = "/download", asyncSupported = true, loadOnStartup = 1)
public class DynamicDownloadServlet extends HttpServlet
{
	private static final long serialVersionUID = 8511309463555430804L;
	
	private static final int DEFAULT_BUFFER_SIZE = 2048;
	public static final int EXPIRATION_TIME_IN_SECONDS = 15;
	
	//--------------------------------------------------------------
	// INHERITED INTERFACE

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String token = req.getParameter("t");
		if((token != null) && !token.isEmpty())
		{
			IDownloadResource resource = DownloadRegistrar.downloadPicked(token);
			if(resource != null)
			{
				if(resource.getSize() > Integer.MAX_VALUE) // "HttpServletResponse.setContentLength()" only accepts int type
				{
					PikaterLogger.log(Level.SEVERE, String.format("The file '%s' was not served because it is too large (larger than MAX_INT).", resource.getFilename()));
					resp.sendError(HttpStatus.SC_BAD_REQUEST, "The requested file is too large to serve.");
				}
				else
				{
					resp.setContentType(resource.getMimeType());
					resp.setHeader("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"");
					resp.setContentLength((int) resource.getSize());
					
					// TODO: set cache time or other things, if you mean to
					
					InputStream input = null;
			        try
			        {
			        	byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
						OutputStream output = resp.getOutputStream();
						input = resource.getStream();
				        int length;
			            while ((length = input.read(buffer)) > 0)
			            {
			                output.write(buffer, 0, length);
			            }
			        }
			        catch (IOException e)
			        {
			        	PikaterLogger.logThrowable(
			        			String.format("Client most likely disconnected or aborted transferring the file '%s' but this needs to be logged anyway.", resource.getFilename()), e);
			        	MyNotifications.showError("Download ended with error", resource.getFilename());
			        }
			        catch (Throwable t)
			        {
			        	PikaterLogger.logThrowable(String.format("Unknown error occured while uploading file '%s'.", resource.getFilename()), t);
			        	MyNotifications.showError("Download ended with error", resource.getFilename());
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
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		doGet(req, resp);
	}
}