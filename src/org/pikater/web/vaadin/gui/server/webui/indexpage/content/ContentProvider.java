package org.pikater.web.vaadin.gui.server.webui.indexpage.content;

import org.pikater.web.vaadin.gui.server.webui.indexpage.content.admin.UsersView;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Label;

public class ContentProvider
{
	public interface IWebFeature
	{
		String toMenuCaptiong();
		AbstractComponent toComponent();
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
		public AbstractComponent toComponent()
		{
			switch(this)
			{
				case VIEW_USERS:
					return new UsersView();
				default:
					return new Label("<font color=\"red\">Unimplemented yet.</font>", ContentMode.HTML);
			}
		}
	}
	
	public enum UserFeature implements IWebFeature
	{
		VIEW_DATASETS,
		VIEW_METHODS,
		EXPERIMENT_EDITOR,
		VIEW_EXPERIMENT_RESULTS;
		
		@Override
		public String toMenuCaptiong()
		{
			switch(this)
			{
				case EXPERIMENT_EDITOR:
					return "Experiment editor";
				case VIEW_DATASETS:
					return "Browse datasets";
				case VIEW_EXPERIMENT_RESULTS:
					return "Experiment results";
				case VIEW_METHODS:
					return "Browse methods";
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}

		@Override
		public AbstractComponent toComponent()
		{
			switch(this)
			{
				default:
					return new Label("<font color=\"red\">Unimplemented yet.</font>", ContentMode.HTML);
			}
		}
	}
}
