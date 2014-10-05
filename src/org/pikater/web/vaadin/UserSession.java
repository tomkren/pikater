package org.pikater.web.vaadin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.pikater.web.sharedresources.ResourceExpiration;
import org.pikater.web.sharedresources.ResourceRegistrar;

import com.vaadin.server.VaadinSession;

/**
 * A utility class that manages all attributes the application stores in
 * user {@link VaadinSession sessions}. Do note, however, that
 * it is not a "session implementation". On the contrary, 
 * the session in question is always required as a method argument.
 * 
 * @author SkyCrawl
 */
public class UserSession {
	private static final String key_userID = "k_uid";
	private static final String key_userUploads = "k_uu";
	private static final String key_sharedResources = "k_sr";

	// ----------------------------------------------------------
	// PRIVATE INTERFACE (INFRASTRUCTURE)

	private static void setAttribute(VaadinSession session, String key, Object value) {
		session.setAttribute(key, value);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Object> T getAttribute(VaadinSession session, String key) {
		return (T) session.getAttribute(key);
	}

	// ----------------------------------------------------------
	// INTERFACE MANIPULATING WITH INDIVIDUAL ATTRIBUTES

	public static int getUserID(VaadinSession session) {
		return getAttribute(session, UserSession.key_userID);
	}

	public static void storeUserID(VaadinSession session, Integer userID) {
		setAttribute(session, UserSession.key_userID, userID);
	}

	public static UserUploads getUserUploadManager(VaadinSession session) {
		return getAttribute(session, UserSession.key_userUploads);
	}

	public static void storeUserUploadManager(VaadinSession session, UserUploads manager) {
		setAttribute(session, UserSession.key_userUploads, new UserUploads());
	}

	/**
	 * <p>All shared resources registered with {@link ResourceRegistrar} and
	 * {@link ResourceExpiration#ON_SESSION_END} are also required to be
	 * stored in the user's session for eventual cleanup.</p>
	 * 
	 * <p>Other resources may be stored too.</p>
	 * 
	 * @param session user's session
	 * @param resourceID the identifier assigned to the given resource by {@link ResourceRegistrar}
	 */
	public static void rememberSharedResource(VaadinSession session, UUID resourceID) {
		Set<UUID> sharedResources = getSharedResources(session);
		if (sharedResources == null) {
			sharedResources = new HashSet<UUID>();
		}
		if (sharedResources.add(resourceID)) {
			session.setAttribute(key_sharedResources, sharedResources);
		}
	}

	/**
	 * Returns all identifiers stored by {@link #rememberSharedResource(VaadinSession, UUID)}
	 * method. 
	 * @param session user's session
	 * @return
	 */
	public static Set<UUID> getSharedResources(VaadinSession session) {
		return getAttribute(session, key_sharedResources);
	}
}