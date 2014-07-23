package org.pikater.web.servlets.download;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.quartzjobs.DownloadTokenExpirationJob;

public class DownloadRegistrar
{
	private static final Object lock_object = new Object();
	private static final Map<UUID, DownloadResource> uuidToResource = new HashMap<UUID, DownloadResource>();
	
	public static String issueDownloadURL(IDownloadResource resource)
	{
		synchronized(lock_object)
		{
			UUID newUUID = getNextUIID();
			uuidToResource.put(newUUID, new DownloadResource(resource, false));
			return createDownloadURL(newUUID);
		}
	}
	
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
			UUID newUUID = getNextUIID();
			uuidToResource.put(newUUID, new DownloadResource(resource, true));
			
			try
			{
				PikaterJobScheduler.getJobScheduler().defineJob(DownloadTokenExpirationJob.class, new Object[] { newUUID, resource });
				return createDownloadURL(newUUID);
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
				DownloadResource resource = uuidToResource.get(uuid);
				if(resource.isAOneTimeDownload())
				{
					uuidToResource.remove(uuid);
				}
				return resource.getResource();
			}
			catch (Throwable t)
			{
				// NullPointerException catcher - invalid token is invalid token => return null and don't log anything
				return null;
			}
		}
	}
	
	public static void downloadExpired(UUID token, IDownloadResource resource) 
	{
		synchronized(lock_object)
		{
			DownloadResource result = uuidToResource.get(token);
			if((result != null) && result.getResource().equals(resource))
			{
				uuidToResource.remove(token);
			}
		}
	}
	
	//-----------------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private static UUID getNextUIID()
	{
		UUID newUUID;
		while(uuidToResource.containsKey(newUUID = UUID.randomUUID()))
		{
		}
		return newUUID;
	}
	
	/**
	 * Common method to create a correct download URL that respects dynamic
	 * application context paths.</br>
	 * Context path can be defined (for example) as the name of the ".war"
	 * file deployed to Tomcat, excluding the extension.
	 * @param uuid
	 * @return
	 */
	private static String createDownloadURL(UUID uuid)
	{
		return String.format("/%s/download?t=%s", 
				ServerConfigurationInterface.getContext().getContextPath(),
				uuid.toString()
		);
	}
	
	//-----------------------------------------------------------------------------
	// PRIVATE TYPES
	
	private static class DownloadResource
	{
		private final IDownloadResource resource;
		private final boolean isAOneTimeDownload;
		
		public DownloadResource(IDownloadResource resource, boolean isAOneTimeDownload)
		{
			this.resource = resource;
			this.isAOneTimeDownload = isAOneTimeDownload;
		}

		public IDownloadResource getResource()
		{
			return resource;
		}

		public boolean isAOneTimeDownload()
		{
			return isAOneTimeDownload;
		}
	}
}