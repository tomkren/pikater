package org.pikater.web.sharedresources;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.web.quartzjobs.ResourceExpirationJob;
import org.pikater.web.servlets.DynamicDownloadServlet;
import org.pikater.web.sharedresources.download.IDownloadResource;
import org.pikater.web.vaadin.ManageSession;

import com.vaadin.server.VaadinSession;

public class ResourceRegistrar
{
	private static final Object lock_object = new Object();
	private static final Map<UUID, IRegistrarResource> uuidToResource = new HashMap<UUID, IRegistrarResource>();
	
	//-------------------------------------------------------------
	// GENERAL RESOURCE ROUTINES
	
	/**
	 * This is the base method for creating download URLs. First, a resource mapping needs
	 * to be created, resource ID returned (which is what this method returns) and then
	 * a download URL may be constructed with {@link #getDownloadURL(UUID)}.
	 * @param resource
	 * @param lifeSpan
	 * @return
	 */
	public static UUID registerResource(VaadinSession session, IRegistrarResource resource)
	{
		if(resource == null)
		{
			throw new NullPointerException("Given resource can not be null.");
		}
		else
		{
			synchronized(lock_object)
			{
				UUID newUUID = getNextUIID();
				uuidToResource.put(newUUID, resource);
				switch(resource.getLifeSpan())
				{
					case ON_FIRST_PICKUP:
						try
						{
							// resource will expire on its own if not picked up
							PikaterJobScheduler.getJobScheduler().defineJob(ResourceExpirationJob.class, new Object[] { newUUID, resource });
						}
						catch (Throwable e)
						{
							/*
							 * Send a runtime error that will be caught by the default error handler on Vaadin UI,
							 * logged and client will see a notification of an error with 500 status code (internal
							 * server error).
							 */
							throw new RuntimeException("Could not issue a resource expiration job.", e);
						}
						break;
						
					case ON_SESSION_END: 
						// resource will be expired manually, when session ends - only remember it for now
						ManageSession.rememberSharedResource(VaadinSession.getCurrent(), newUUID);
						break;
						
					default:
						throw new IllegalStateException("Unknown state: " + resource.getLifeSpan().name());
				}
				return newUUID;
			}
		}
	}
	
	/**
	 * Returns whether a resource is mapped to the given ID. 
	 * @param resourceID
	 * @return
	 */
	public static boolean isResourceRegistered(UUID resourceID)
	{
		synchronized(lock_object)
		{
			return uuidToResource.containsKey(resourceID);
		}
	}
	
	/**
	 * Gets the resource associated with the given ID. Mind that resource may expire
	 * on first pickup and using this method may expire the returned resource.
	 * @param resourceID
	 * @return
	 */
	public static IRegistrarResource getResource(UUID resourceID)
	{
		synchronized(lock_object)
		{
			if(uuidToResource.containsKey(resourceID))
			{
				IRegistrarResource resource = uuidToResource.get(resourceID);
				if(resource.getLifeSpan() == ResourceExpiration.ON_FIRST_PICKUP)
				{
					uuidToResource.remove(resourceID);
				}
				return resource;
			}
			else
			{
				return null;
			}
		}
	}
	
	/**
	 * Expires a resource that was to be expired on first pickup.
	 * @param resourceID
	 * @param resource
	 */
	public static void expireOnFirstPickupResource(UUID resourceID, IRegistrarResource resource) 
	{
		synchronized(lock_object)
		{
			if(uuidToResource.containsKey(resourceID) && uuidToResource.get(resourceID).equals(resource))
			{
				uuidToResource.remove(resourceID); // TODO: call "expired()" method on the resource
			}
		}
	}
	
	/**
	 * Method to be used when a resource expires.
	 * @param resourceID
	 * @param resource
	 */
	public static void expireSessionResources(VaadinSession session) 
	{
		synchronized(lock_object)
		{
			for(UUID resourceID : ManageSession.getSharedResources(session))
			{
				if(uuidToResource.containsKey(resourceID) && uuidToResource.get(resourceID).getLifeSpan() == ResourceExpiration.ON_SESSION_END)
				{
					uuidToResource.remove(resourceID); // TODO: call "expired()" method on the resource
				}
			}
		}
	}
	
	/**
	 * Translates the given resource ID to a token (String) so it can be passed (for instance) to another UI
	 * as an URL parameter.
	 * @param resourceID
	 * @return
	 */
	public static String fromResourceID(UUID resourceID)
	{
		return resourceID.toString();
	}
	
	/**
	 * Translates the given token constructed with the {@link #fromResourceID(UUID)} method
	 * into a resource ID.
	 * @param resourceToken
	 * @return
	 */
	public static UUID toResourceID(String resourceToken)
	{
		return UUID.fromString(resourceToken);
	}
	
	//-------------------------------------------------------------
	// DOWNLOADABLE RESOURCE ROUTINES
	
	/**
	 * Creates a download URL for the given resource ID. The associated resource
	 * must be an instance of {@link IDownloadResource}. The returned URL points to
	 * {@link DynamicDownloadServlet}.
	 * @param uuid
	 * @return
	 */
	public static String getDownloadURL(UUID resourceID)
	{
		synchronized(lock_object)
		{
			if(uuidToResource.containsKey(resourceID))
			{
				IRegistrarResource resource = getResource(resourceID);
				if(resource instanceof IDownloadResource)
				{
					return String.format("./download?t=%s", fromResourceID(resourceID));
				}
				else
				{
					throw new IllegalStateException("Resource associated with the given ID is not downloadable.");
				}
			}
			else
			{
				throw new IllegalStateException("No resource found for the given ID.");
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