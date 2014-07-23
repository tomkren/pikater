package org.pikater.web.servlets.download;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.web.quartzjobs.DownloadTokenExpirationJob;

public class DownloadRegistrar
{
	private static final Object lock_object = new Object();
	private static final Map<UUID, IDownloadResource> uuidToResource = new HashMap<UUID, IDownloadResource>();
	
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
				PikaterJobScheduler.getJobScheduler().defineJob(DownloadTokenExpirationJob.class, new Object[] { newUUID, resource });
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
	
	public static IDownloadResource downloadPickedUp(String token) 
	{
		synchronized(lock_object)
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
	}
	
	public static void downloadExpired(UUID token, IDownloadResource resource) 
	{
		synchronized(lock_object)
		{
			IDownloadResource result = uuidToResource.get(token);
			if((result != null) && result.equals(resource))
			{
				uuidToResource.remove(token);
			}
		}
	}
}