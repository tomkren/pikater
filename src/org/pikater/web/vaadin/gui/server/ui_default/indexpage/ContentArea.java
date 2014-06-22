package org.pikater.web.vaadin.gui.server.ui_default.indexpage;

import java.util.HashSet;
import java.util.Set;

import org.pikater.shared.util.SimpleIDGenerator;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.AdminFeature;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.DefaultFeature;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IWebFeatureSet;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.UserFeature;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public class ContentArea extends Panel
{
	private static final long serialVersionUID = 7642456908975377869L;
	
	private final Navigator navigator;
	
	private final SimpleIDGenerator idGenerator;
	private final Set<IWebFeatureSet> registeredFeatures;
	
	public ContentArea()
	{
		super();
		setSizeFull();
		setStyleName("contentArea");
		
		this.navigator = new Navigator(UI.getCurrent(), this);
		this.navigator.addViewChangeListener(new ViewChangeListener()
		{
			private static final long serialVersionUID = -6954284010979732570L;
			
			private boolean result;

			/**
			 * Called after {@link Navigator#navigateTo(String)}, e.g. after {@link ContentArea#setContent(Component content)} or
			 * {@link ContentArea#setContent(IWebFeatureSet feature)} method.
			 */
			@Override
			public boolean beforeViewChange(ViewChangeEvent event)
			{
				result = false; // block the change by default
				if((event.getOldView() != null) && (event.getOldView() instanceof IContentComponent))
				{
					IContentComponent currentView = (IContentComponent) event.getOldView();
					if(currentView.hasUnsavedProgress())
					{
						MyDialogs.confirm("Navigate away?", currentView.getCloseDialogMessage(), new MyDialogs.IDialogResultHandler()
						{
							@Override
							public boolean handleResult(Object[] args)
							{
								result = true;
								return true; // close the dialog
							}
						});
					}
					else
					{
						result = true;
					}
				}
				else
				{
					result = true;
				}
				return result;
			}
			
			@Override
			public void afterViewChange(ViewChangeEvent event)
			{
			}
		});
		
		this.idGenerator = new SimpleIDGenerator();
		this.registeredFeatures = new HashSet<IWebFeatureSet>();
	}
	
	//---------------------------------------------------------------
	// PUBLIC INTERFACE
	
	/**
	 * A little hack-around of the navigation system. Should only be used for testing purposes.
	 * @param content
	 */
	public void setContentView(Component content)
	{
		if(content instanceof IContentComponent)
		{
			throw new IllegalArgumentException("The provided component implements the IContentComponent interface. "
					+ "Use the 'setContent(IWebFeature)' method instead.");
		}
		else if(content instanceof View)
		{
			String dynamicallyAssignedViewName = getNextViewName(); 
			navigator.addView(dynamicallyAssignedViewName, (View) content);
			navigator.navigateTo(dynamicallyAssignedViewName);
		}
		else
		{
			throw new IllegalArgumentException("The provided component does not implement the View interface.");
		}
	}
	
	/**
	 * The main routine for setting the content component.
	 * @param feature
	 */
	public void setContentView(IWebFeatureSet feature)
	{
		if(feature.accessAllowed(VaadinSession.getCurrent()))
		{
			if(!registeredFeatures.contains(feature)) // lazy individual registering
			{
				registerFeature(feature);
			}
			navigator.navigateTo(feature.toNavigatorName());
		}
		else
		{
			MyDialogs.error("Access denied", "Contact the administrators.");
		}
	}
	
	//---------------------------------------------------------------
	// PRIVATE INTERFACE
	
	@SuppressWarnings("unused")
	private void registerAllAvailableViews()
	{
		registerAllViewsFromFeatureSet(DefaultFeature.class);
		registerAllViewsFromFeatureSet(AdminFeature.class);
		registerAllViewsFromFeatureSet(UserFeature.class);
	}
	
	private void registerAllViewsFromFeatureSet(Class<? extends IWebFeatureSet> clazz)
	{
		for(IWebFeatureSet feature : clazz.getEnumConstants())
		{
			if(feature.accessAllowed(VaadinSession.getCurrent()))
			{
				registerFeature(feature);
			}
		}
	}
	
	private void registerFeature(IWebFeatureSet feature)
	{
		navigator.addView(feature.toNavigatorName(), feature.toComponent());
	}
	
	/**
	 * Creates view names for components that do not implement the IContentComponent interface.
	 * @see #setContent(Component content)
	 * @return
	 */
	private String getNextViewName()
	{
		return "UnnamedView" + String.valueOf(idGenerator.getAndIncrement());
	}
}