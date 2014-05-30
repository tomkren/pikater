package org.pikater.web.vaadin.gui.server.webui.indexpage.content;

import org.pikater.web.vaadin.gui.server.webui.indexpage.content.user.ProfileView;

public class ContentProvider
{
	public interface IWebFeature
	{
		String toMenuCaptiong();
		IContentComponent toComponent();
	}
	
	public interface IContentComponent
	{
		boolean hasUnsavedProgress();
		String getCloseDialogMessage();
	}
	
	public enum DefaultFeature implements IWebFeature
	{
		DEFAULT;

		@Override
		public String toMenuCaptiong()
		{
			return null;
		}

		@Override
		public IContentComponent toComponent()
		{
			return new DefaultContent();
		}
	}
	
	public enum AdminFeature implements IWebFeature
	{
		VIEW_USERS,
		VIEW_SCHEDULED_EXPERIMENTS,
		VIEW_DATASETS_METHODS,
		VIEW_SYSTEM_STATUS;
		
		@Override
		public String toMenuCaptiong()
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
		public IContentComponent toComponent()
		{
			switch(this)
			{
				default:
					return new UnimplementedContent();
			}
		}
	}
	
	public enum UserFeature implements IWebFeature
	{
		VIEW_PROFILE,
		VIEW_DATASETS,
		VIEW_METHODS,
		EXPERIMENT_EDITOR,
		VIEW_EXPERIMENT_RESULTS;
		
		@Override
		public String toMenuCaptiong()
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
		public IContentComponent toComponent()
		{
			switch(this)
			{
				case VIEW_PROFILE:
					return new ProfileView();
				default:
					return new UnimplementedContent();
			}
		}
	}
}
