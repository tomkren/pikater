package org.pikater.web.vaadin.gui.server.webui.indexpage;

import org.pikater.shared.util.SimpleIDGenerator;
import org.pikater.web.vaadin.gui.server.webui.MyDialogs;
import org.pikater.web.vaadin.gui.server.webui.MyDialogs.OnOkClicked;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.AdminFeature;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.DefaultFeature;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.IContentComponent;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.IWebFeatureSet;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.UserFeature;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;

public class ContentArea extends CustomComponent
{
	private static final long serialVersionUID = 7642456908975377869L;
	private static final boolean registerAllViewsOnInit = true;
	
	private final Panel innerLayout;
	private final SimpleIDGenerator idGenerator;
	private final Navigator navigator;
	
	public ContentArea()
	{
		super();
		setSizeFull();
		
		this.innerLayout = new Panel();
		this.innerLayout.setSizeFull();
		this.innerLayout.setStyleName("contentArea");
		this.idGenerator = new SimpleIDGenerator();
		this.navigator = new Navigator(UI.getCurrent(), this.innerLayout);
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
						MyDialogs.createSimpleConfirmDialog(getUI(), currentView.getCloseDialogMessage(), new OnOkClicked()
						{
							@Override
							public boolean handleOkEvent()
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
		if(registerAllViewsOnInit)
		{
			registerAllAvailableViews();
		}
		
		setCompositionRoot(this.innerLayout);
	}
	
	//---------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public void setContent(Component content)
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
	
	public void setContent(IWebFeatureSet feature)
	{
		String featureNameForNavigator = feature.toNavigatorName(); 
		if(!registerAllViewsOnInit)
		{
			// lazy individual registering
			// TODO: has the feature been registered before?
			navigator.addView(featureNameForNavigator, feature.toComponent());
		}
		navigator.navigateTo(featureNameForNavigator);
	}
	
	//---------------------------------------------------------------
	// PRIVATE INTERFACE
	
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
				navigator.addView(feature.toNavigatorName(), feature.toComponent());
			}
		}
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