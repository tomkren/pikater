package org.pikater.web.servlets.download;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.web.quartzjobs.DownloadTokenExpirationJob;
import org.pikater.web.servlets.download.resources.IDownloadResource;

public class DownloadRegistrar
{
	private static final Object lock_object = new Object();
	private static final Map<UUID, IDownloadResource> uuidToResource = new HashMap<UUID, IDownloadResource>();
	
	public enum DownloadLifespan
	{
		ONE_DOWNLOAD,
		INDETERMINATE
	}
	
	/**
	 * This is the base method for creating download URLs. First, a resource mapping needs
	 * to be created, resource ID returned (which is what this method returns) and then
	 * a download URL may be constructed with {@link #getDownloadURL(UUID)}.
	 * @param resource
	 * @param lifeSpan
	 * @return
	 */
	public static UUID issueDownload(IDownloadResource resource)
	{
		synchronized(lock_object)
		{
			UUID newUUID = getNextUIID();
			uuidToResource.put(newUUID, resource);
			if(resource.getLifeSpan() == DownloadLifespan.ONE_DOWNLOAD)
			{
				try
				{
					PikaterJobScheduler.getJobScheduler().defineJob(DownloadTokenExpirationJob.class, new Object[] { newUUID, resource });
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
			return newUUID;
		}
	}
	
	/**
	 * Returns whether a resource is mapped to the given ID. 
	 * @param resourceID
	 * @return
	 */
	public static boolean isResourceRegistered(UUID resourceID)
	{
		return uuidToResource.containsKey(resourceID); 
	}
	
	/**
	 * Gets the downloadable resource associated with the given ID.
	 * @param resourceID
	 * @return
	 */
	public static IDownloadResource getResource(UUID resourceID)
	{
		return uuidToResource.get(resourceID); 
	}
	
	/**
	 * Creates a download URL for the given resource ID. The URL points to {@link DynamicDownloadServlet}.
	 * @param uuid
	 * @return
	 */
	public static String getDownloadURL(UUID uuid)
	{
		return String.format("./download?t=%s", exportIDToDownloadToken(uuid));
	}
	
	/**
	 * Exports a resource ID to download token (String), so it can be passed (for instance) to another UI
	 * as an URL parameter.
	 * @param uuid
	 * @return
	 */
	public static String exportIDToDownloadToken(UUID uuid)
	{
		return uuid.toString();
	}
	
	/**
	 * Exports a download token into a resource ID so that (for example) download URLs
	 * can be constructed from it.
	 * @param uuid
	 * @return
	 */
	public static UUID exportDownloadTokenToID(String downloadToken)
	{
		return UUID.fromString(downloadToken);
	}
	
	/**
	 * A method to be used by {@link DynamicDownloadServlet}, when a download
	 * is requested.
	 * @param token
	 * @return
	 */
	public static IDownloadResource downloadPickedUp(String downloadToken) 
	{
		synchronized(lock_object)
		{
			try
			{
				UUID uuid = exportDownloadTokenToID(downloadToken);
				IDownloadResource resource = uuidToResource.get(uuid);
				if(resource.getLifeSpan() == DownloadLifespan.ONE_DOWNLOAD)
				{
					uuidToResource.remove(uuid);
				}
				return resource;
			}
			catch (Throwable t)
			{
				// NullPointerException catcher - invalid token is invalid token => return null and don't log anything
				return null;
			}
		}
	}
	
	/**
	 * Method to be used by expiration jobs, when a download expires.
	 * @param token
	 * @param resource
	 */
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
}