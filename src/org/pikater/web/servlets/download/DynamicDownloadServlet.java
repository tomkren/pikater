package org.pikater.web.servlets.download;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.web.quartzjobs.DownloadTokenExpirationjob;

@WebServlet(value = "/download", asyncSupported = true, loadOnStartup = 1)
public class DynamicDownloadServlet extends HttpServlet
{
	private static final long serialVersionUID = 8511309463555430804L;
	
	private static final int DEFAULT_BUFFER_SIZE = 2048;
	public static final int EXPIRATION_TIME_IN_SECONDS = 15;
	
	private static final Object lock_object = new Object();
	private static final Map<UUID, IDownloadResource> uuidToResource = new HashMap<UUID, IDownloadResource>();
	
	//--------------------------------------------------------------
	// INHERITED INTERFACE

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		String token = req.getParameter("t");
		if((token != null) && !token.isEmpty())
		{
			IDownloadResource resource = downloadPicked(token);
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
					// TODO: stream, file name, mime type, content length
					
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
	
	//--------------------------------------------------------------
	// INTERFACE TO MAKE IT QUITE SECURE AND THREAD-SAFE
	
	/**
	 * Associates a download resource with a unique download URL.
	 * @param resource the resource to associate
	 * @return The download URL. Feed it to the {@link com.vaadin.server.Page#setLocation setLocation} method
	 * and observe what happens :).
	 */
	public static String issueAOneTimeDownloadURL(IDownloadResource resource)
	{
		synchronized(lock_object)
		{
			UUID newUUID;
			while(uuidToResource.containsKey(newUUID = UUID.randomUUID()))
			{
			}
			uuidToResource.put(newUUID, resource);
			
			try
			{
				PikaterJobScheduler.getJobScheduler().defineJob(DownloadTokenExpirationjob.class, new Object[] { newUUID, resource });
				return "/Pikater/download?t=" + newUUID.toString();
			}
			catch (Throwable e)
			{
				/*
				 * Send a runtime error that will be caught by the default error handler on Vaadin UI,
				 * logged and client will see a notification of an error with 500 status code (internal
				 * server error).
				 */
				throw new RuntimeException("Could not issue the current download expiration job.", e);
			}
		}
	}
	
	public static IDownloadResource downloadPicked(String token) 
	{
		try
		{
			UUID uuid = UUID.fromString(token);
			IDownloadResource result = uuidToResource.get(uuid);
			if(result != null)
			{
				uuidToResource.remove(uuid);
			}
			return result;
		}
		catch (Throwable t)
		{
			// no need to log this... invalid token is invalid token
			return null;
		}
	}
	
	public static void downloadExpired(UUID token, IDownloadResource resource) 
	{
		IDownloadResource result = uuidToResource.get(token);
		if((result != null) && result.equals(resource))
		{
			uuidToResource.remove(token);
		}
	}
}