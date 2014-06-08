package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content;

import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.UsersView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user.DatasetsAndMethodsView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user.ProfileView;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;

public class ContentProvider
{
	public interface IWebFeatureSet
	{
		String toMenuCaption();
		boolean accessAllowed(VaadinSession session);
		boolean shouldOpenInSeperateTab();
		String toNavigatorName();
		IContentComponent toComponent();
	}
	
	public interface IContentComponent extends View
	{
		boolean hasUnsavedProgress();
		String getCloseDialogMessage();
	}
	
	public enum DefaultFeature implements IWebFeatureSet
	{
		WELCOME;
		
		@Override
		public String toMenuCaption()
		{
			return null;
		}
		
		@Override
		public boolean accessAllowed(VaadinSession session)
		{
			return true; // this feature set is for everyone
		}
		
		@Override
		public boolean shouldOpenInSeperateTab()
		{
			return false;
		}

		@Override
		public String toNavigatorName()
		{
			return "welcome";
		}

		@Override
		public IContentComponent toComponent()
		{
			return new WelcomeContent();
		}
	}
	
	public enum AdminFeature implements IWebFeatureSet
	{
		VIEW_USERS,
		VIEW_SCHEDULED_EXPERIMENTS,
		VIEW_DATASETS_METHODS,
		VIEW_SYSTEM_STATUS;
		
		@Override
		public String toMenuCaption()
		{
			switch(this)
			{
				case VIEW_DATASETS_METHODS:
					return "New datasets & methods";
				case VIEW_SCHEDULED_EXPERIMENTS:
					return "Scheduled experiments";
				case VIEW_SYSTEM_STATUS:
					return "System status";
				case VIEW_USERS:
					return "Users";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		@Override
		public boolean accessAllowed(VaadinSession session)
		{
			return ManageAuth.getUserEntity(session).isAdmin(); // only allowed for admins
		}
		
		@Override
		public boolean shouldOpenInSeperateTab()
		{
			return false;
		}
		
		@Override
		public String toNavigatorName()
		{
			switch(this)
			{
				case VIEW_DATASETS_METHODS:
					return "admin-ToApprove";
				case VIEW_SCHEDULED_EXPERIMENTS:
					return "adminDisplaySchedule";
				case VIEW_SYSTEM_STATUS:
					return "adminSystemStatus";
				case VIEW_USERS:
					return "adminDisplayUsers";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}

		@Override
		public IContentComponent toComponent()
		{
			switch(this)
			{
				case VIEW_USERS:
					return new UsersView();
				default:
					return new UnimplementedContent();
			}
		}
	}
	
	public enum UserFeature implements IWebFeatureSet
	{
		VIEW_PROFILE,
		VIEW_DATASETS,
		VIEW_METHODS,
		EXPERIMENT_EDITOR,
		VIEW_EXPERIMENT_RESULTS;
		
		@Override
		public String toMenuCaption()
		{
			switch(this)
			{
				case VIEW_PROFILE:
					return "View & edit profile";
				case EXPERIMENT_EDITOR:
					return "Experiment editor";
				case VIEW_DATASETS:
					return "Available datasets";
				case VIEW_EXPERIMENT_RESULTS:
					return "Experiment results";
				case VIEW_METHODS:
					return "Available methods";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
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
		public boolean shouldOpenInSeperateTab()
		{
			return this == EXPERIMENT_EDITOR;
		}
		
		@Override
		public String toNavigatorName()
		{
			switch(this)
			{
				case VIEW_PROFILE:
					return "userProfile";
				case VIEW_DATASETS:
				case VIEW_METHODS:
					return "displayDataSetsAndMethods";
				case EXPERIMENT_EDITOR:
					return null; // always opened in a new tab or window (UI), so history support is not needed
				case VIEW_EXPERIMENT_RESULTS:
					return "displayResults";
				
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}

		@Override
		public IContentComponent toComponent()
		{
			switch(this)
			{
				case VIEW_PROFILE:
					return new ProfileView();
				case VIEW_DATASETS:
				case VIEW_METHODS:
					return new DatasetsAndMethodsView();
				default:
					return new UnimplementedContent();
			}
		}
	}
}
