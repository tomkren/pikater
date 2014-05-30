package org.pikater.web.vaadin.gui.server.webui.indexpage;

import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.MyResources;
import org.pikater.web.vaadin.gui.server.AuthHandler;
import org.pikater.web.vaadin.gui.server.components.linklabel.LinkLabel;
import org.pikater.web.vaadin.gui.server.components.linklabel.LinkLabel.LinkLabelColorConf;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.UserFeature;

import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent; 
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class BannerArea extends HorizontalLayout
{
	private static final long serialVersionUID = 8822379309622158669L;
	
	private final LinkLabel link_loggedInAs;

	public BannerArea(final IndexPage parentPage)
	{
		super();
		setStyleName("bannerArea");
		setSizeFull();
		
		Image checkIcon = new Image(null, MyResources.img_checkIcon16);
		Label lbl_loggedInAs = new Label("Logged in as:");
		link_loggedInAs = new LinkLabel(null, LinkLabelColorConf.BANNER_AREA, new MouseEvents.ClickListener()
		{
			private static final long serialVersionUID = -7249554724660903953L;

			@Override
			public void click(ClickEvent event)
			{
				parentPage.setContentAreaComponent(UserFeature.VIEW_PROFILE);
			}
		});
		
		HorizontalLayout loggedInAs = new HorizontalLayout();
		loggedInAs.setSpacing(true);
		loggedInAs.addComponent(checkIcon);
		loggedInAs.addComponent(lbl_loggedInAs);
		loggedInAs.addComponent(link_loggedInAs);
		loggedInAs.setComponentAlignment(checkIcon, Alignment.MIDDLE_CENTER);
		loggedInAs.setComponentAlignment(lbl_loggedInAs, Alignment.MIDDLE_CENTER);
		loggedInAs.setComponentAlignment(link_loggedInAs, Alignment.MIDDLE_CENTER);
		
		Button btn_logout = new Button("Logout", new Button.ClickListener()
		{
			private static final long serialVersionUID = 1401413857588241059L;

			@Override
			public void buttonClick(com.vaadin.ui.Button.ClickEvent event)
			{
				AuthHandler.logout(getSession());
				getUI().getPage().reload();
			}
		}); 
		
		VerticalLayout loginStatusComponent = new VerticalLayout();
		loginStatusComponent.setSizeUndefined();
		loginStatusComponent.setSpacing(true);
		loginStatusComponent.setStyleName("bannerArea-authinfo");
		loginStatusComponent.addComponent(loggedInAs);
		loginStatusComponent.addComponent(btn_logout);
		loginStatusComponent.setComponentAlignment(loggedInAs, Alignment.MIDDLE_LEFT);
		loginStatusComponent.setComponentAlignment(btn_logout, Alignment.MIDDLE_RIGHT);
		
		Image banner = new Image(null, MyResources.img_banner);
		banner.setStyleName("bannerArea-image");
		addComponent(banner);
		addComponent(loginStatusComponent);
		setComponentAlignment(banner, Alignment.MIDDLE_CENTER);
		setComponentAlignment(loginStatusComponent, Alignment.MIDDLE_RIGHT);
		setExpandRatio(banner, 1);
	}
	
	@Override
	public void attach()
	{
		super.attach();
		
		if(ServerConfigurationInterface.avoidUsingDBForNow())
		{
			link_loggedInAs.setText("some_account");
		}
		else
		{
			link_loggedInAs.setText(AuthHandler.getUserEntity(getSession()).getLogin());
		}
	}
}
