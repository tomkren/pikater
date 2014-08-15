package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content;

import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.AgentsView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.BatchesView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.DatasetsView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.UsersView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user.UserAgentsView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user.UserBatchesView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user.UserDatasetsView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user.UserProfileView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.various.TestView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.various.WelcomeView;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;

public class ContentProvider
{
	public interface IWebFeature
	{
		boolean accessAllowed(VaadinSession session);
		String toMenuCaption();
		String toNavigatorName();
		Class<? extends IContentComponent> toComponentClass();
	}
	
	public interface IContentComponent extends View
	{
		boolean hasUnsavedProgress();
		String getCloseDialogMessage();
	}
	
	public enum DefaultFeature implements IWebFeature
	{
		// IMPORTANT: always bind the default content with empty navigator name.
		WELCOME("", WelcomeView.class),
		TEST("test", TestView.class);
		
		private final Class<? extends IContentComponent> mappedComponent;
		private final String navigatorName;
		
		private DefaultFeature(String navigatorName, Class<? extends IContentComponent> mappedComponent)
		{
			this.navigatorName = navigatorName;
			this.mappedComponent = mappedComponent;
		}
		
		@Override
		public boolean accessAllowed(VaadinSession session)
		{
			switch(this)
			{
				case TEST:
					return CustomConfiguredUI.isDebugModeActive();
				case WELCOME:
					return true; // this feature set is for everyone
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		@Override
		public String toMenuCaption()
		{
			return null;
		}
		
		@Override
		public String toNavigatorName()
		{
			return navigatorName;
		}
		
		@Override
		public Class<? extends IContentComponent> toComponentClass()
		{
			return mappedComponent;
		}
	}
	
	public enum AdminFeature implements IWebFeature
	{
		// IMPORTANT: all navigator names should start with "admin". See {@link #getFeatureFromNavigatorName} below.
		VIEW_USERS("adminAllUsers", UsersView.class),
		VIEW_DATASETS("adminAllDatasets", DatasetsView.class),
		VIEW_METHODS("adminAllUserAgents", AgentsView.class),
		VIEW_EXPERIMENTS("adminAllExperiments", BatchesView.class);
		
		private final Class<? extends IContentComponent> mappedComponent;
		private final String navigatorName;
		
		private AdminFeature(String navigatorName, Class<? extends IContentComponent> mappedComponent)
		{
			this.navigatorName = navigatorName;
			this.mappedComponent = mappedComponent;
		}
		
		@Override
		public String toMenuCaption()
		{
			switch(this)
			{
				case VIEW_USERS:
					return "All users";
				case VIEW_DATASETS:
					return "All datasets";
				case VIEW_METHODS:
					return "All user agents";
				case VIEW_EXPERIMENTS:
					return "All experiments";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		@Override
		public boolean accessAllowed(VaadinSession session)
		{
			return ServerConfigurationInterface.avoidUsingDBForNow() ? true : ManageAuth.getUserEntity(session).isAdmin(); // only allowed for admins
		}
		
		@Override
		public String toNavigatorName()
		{
			return navigatorName;
		}
		
		@Override
		public Class<? extends IContentComponent>  toComponentClass()
		{
			return mappedComponent;
		}
	}
	
	public enum UserFeature implements IWebFeature
	{
		// IMPORTANT: all navigator names should start with "user". See {@link #getFeatureFromNavigatorName} below.
		VIEW_PROFILE("userProfile", UserProfileView.class),
		VIEW_DATASETS("userDatasets", UserDatasetsView.class),
		VIEW_METHODS("userAgents", UserAgentsView.class),
		VIEW_EXPERIMENTS("userExperiments", UserBatchesView.class);
		
		private final Class<? extends IContentComponent> mappedComponent;
		private final String navigatorName;
		
		private UserFeature(String navigatorName, Class<? extends IContentComponent> mappedComponent)
		{
			this.navigatorName = navigatorName;
			this.mappedComponent = mappedComponent;
		}
		
		@Override
		public boolean accessAllowed(VaadinSession session)
		{
			if(ServerConfigurationInterface.avoidUsingDBForNow())
			{
				return true;
			}
			else
			{
				return ManageAuth.isUserAuthenticated(session); // only allowed for authenticated users
			}
		}
		
		@Override
		public String toMenuCaption()
		{
			switch(this)
			{
				case VIEW_PROFILE:
					return "View & edit profile";
				case VIEW_DATASETS:
					return "My datasets";
				case VIEW_METHODS:
					return "My agents";
				case VIEW_EXPERIMENTS:
					return "My experiments";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		@Override
		public String toNavigatorName()
		{
			return navigatorName;
		}
		
		@Override
		public Class<? extends IContentComponent> toComponentClass()
		{
			return mappedComponent;
		}
	}
	
	public static IWebFeature getFeatureFromNavigatorName(String navigatorName)
	{
		if(navigatorName == null)
		{
			return null;
		}
		else if(navigatorName.startsWith("admin"))
		{
			return getFeatureFromNavigatorName(navigatorName, AdminFeature.values());
		}
		else if(navigatorName.startsWith("user"))
		{
			return getFeatureFromNavigatorName(navigatorName, UserFeature.values());
		}
		else
		{
			return getFeatureFromNavigatorName(navigatorName, DefaultFeature.values());
		}
	}
	
	private static IWebFeature getFeatureFromNavigatorName(String navigatorName, IWebFeature[] features)
	{
		for(IWebFeature feature : features)
		{
			if(feature.toNavigatorName().equalsIgnoreCase(navigatorName))
			{
				return feature;
			}
		}
		return null;
	}
}