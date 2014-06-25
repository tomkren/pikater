package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content;

import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.DatasetsView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.UsersView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user.UserDatasetsView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user.UserProfileView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.various.TestView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.various.UnimplementedView;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.various.WelcomeView;

import com.vaadin.navigator.View;
import com.vaadin.server.VaadinSession;

public class ContentProvider
{
	public interface IWebFeatureSet
	{
		String toMenuCaption();
		String toNavigatorName();
		boolean accessAllowed(VaadinSession session);
		boolean shouldOpenInSeperateTab();
		IContentComponent toComponent();
	}
	
	public interface IContentComponent extends View
	{
		boolean hasUnsavedProgress();
		String getCloseDialogMessage();
	}
	
	public enum DefaultFeature implements IWebFeatureSet
	{
		WELCOME,
		TEST;
		
		@Override
		public String toMenuCaption()
		{
			return null;
		}
		
		@Override
		public String toNavigatorName()
		{
			switch(this)
			{
				case TEST:
					return "test";
				case WELCOME:
					return "welcome";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		@Override
		public boolean accessAllowed(VaadinSession session)
		{
			switch(this)
			{
				case TEST:
					return CustomConfiguredUI.isDebugModeActive(); // TODO: test content won't display but an error will... ban this in filters!
				case WELCOME:
					return true; // this feature set is for everyone
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		@Override
		public boolean shouldOpenInSeperateTab()
		{
			return false;
		}

		@Override
		public IContentComponent toComponent()
		{
			switch(this)
			{
				case TEST:
					return new TestView();
				case WELCOME:
					return new WelcomeView();
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}
	
	public enum AdminFeature implements IWebFeatureSet
	{
		VIEW_USERS,
		VIEW_DATASETS,
		VIEW_METHODS,
		VIEW_SCHEDULED_EXPERIMENTS,
		VIEW_SYSTEM_STATUS;
		
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
					return "All methods";
				case VIEW_SCHEDULED_EXPERIMENTS:
					return "All experiments";
				case VIEW_SYSTEM_STATUS:
					return "View system status";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		@Override
		public String toNavigatorName()
		{
			switch(this)
			{
				case VIEW_USERS:
					return "adminAllUsers";
				case VIEW_DATASETS:
					return "adminAllDatasets";
				case VIEW_METHODS:
					return "adminAllMethods";
				case VIEW_SCHEDULED_EXPERIMENTS:
					return "adminAllExperiments";
				case VIEW_SYSTEM_STATUS:
					return "adminSystemStatus";
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
		public IContentComponent toComponent()
		{
			switch(this)
			{
				case VIEW_USERS:
					return new UsersView();
				case VIEW_DATASETS:
					return new DatasetsView();
				default:
					return new UnimplementedView();
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
					return "Go to experiment editor";
				case VIEW_DATASETS:
					return "My datasets";
				case VIEW_METHODS:
					return "My methods";
				case VIEW_EXPERIMENT_RESULTS:
					return "My experiment results";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		@Override
		public String toNavigatorName()
		{
			switch(this)
			{
				case VIEW_PROFILE:
					return "userProfile";
				case VIEW_DATASETS:
					return "userDatasets";
				case VIEW_METHODS:
					return "userMethods";
				case EXPERIMENT_EDITOR:
					return null; // always opened in a new tab or window (UI), so history support is not needed
				case VIEW_EXPERIMENT_RESULTS:
					return "experimentResults";
				
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
		public IContentComponent toComponent()
		{
			switch(this)
			{
				case VIEW_PROFILE:
					return new UserProfileView();
				case VIEW_DATASETS:
					return new UserDatasetsView();
				default:
					return new UnimplementedView();
			}
		}
	}
}
