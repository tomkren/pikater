package org.pikater.web.vaadin.gui.server.webui.indexpage;

import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.AuthHandler;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.AdminFeature;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.IWebFeatureSet;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.UserFeature;

import com.vaadin.ui.MenuBar;

public class LeftMenu extends MenuBar
{
	private static final long serialVersionUID = -3876199300842530858L;
	
	/*
	 * Programmatic fields.
	 */
	private final IndexPage parent;

	public LeftMenu(IndexPage parent)
	{
		super();
		setAutoOpen(true);
		
		this.parent = parent;
	}
	
	@Override
	public void attach()
	{
		super.attach();
		
		if(ServerConfigurationInterface.avoidUsingDBForNow())
		{
			buildMenu(true);
		}
		else
		{
			buildMenu(AuthHandler.getUserEntity(getSession()).isAdmin());
		}
	}
	
	private void buildMenu(boolean withAdminMenu)
	{
		if(withAdminMenu)
		{
			buildAdminMenu();
		}
		buildUserMenu();
	}
	
	private void buildAdminMenu()
	{
		MenuItem adminMenu = addItem("Admin features", null);
		for(AdminFeature feature : AdminFeature.values())
		{
			buildMenuItem(adminMenu, feature);
		}
	}
	
	private void buildUserMenu()
	{
		MenuItem userMenu = addItem("User features", null);
		for(UserFeature feature : UserFeature.values())
		{
			buildMenuItem(userMenu, feature);
		}
	}
	
	private void buildMenuItem(MenuItem parentMenuItem, final IWebFeatureSet feature)
	{
		parentMenuItem.addItem(feature.toMenuCaption(), new Command()
		{
			private static final long serialVersionUID = -2161709698587068923L;

			@Override
			public void menuSelected(MenuItem selectedItem)
			{
				parent.setContentAreaComponent(feature);
			}
		});
	}
}
