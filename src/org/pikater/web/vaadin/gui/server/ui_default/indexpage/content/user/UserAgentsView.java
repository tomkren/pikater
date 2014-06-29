package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user;

import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.forms.AgentUploadForm;
import org.pikater.web.vaadin.gui.server.components.popups.MyPopup;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.AgentsView;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

@StyleSheet("userAgentsView.css")
public class UserAgentsView extends AgentsView
{
	private static final long serialVersionUID = 1257881871718854102L;

	public UserAgentsView()
	{
		super();
		
		addCustomActionComponent(new Button("Upload a new agent", new Button.ClickListener()
		{
			private static final long serialVersionUID = 2295909499889740333L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				MyPopup agentsUploadWizardWindow = new MyPopup("Agent upload guide");
				agentsUploadWizardWindow.setWidth("500px");
				agentsUploadWizardWindow.setHeight("300px");
				
				AgentUploadForm form = new AgentUploadForm(agentsUploadWizardWindow);
				form.setStyleName("agentsUploadForm");
				agentsUploadWizardWindow.setContent(form);
				agentsUploadWizardWindow.show();
			}
		}));
	}
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		this.underlyingDBView.setAgentOwner(ManageAuth.getUserEntity(VaadinSession.getCurrent()));
		super.enter(event);
	}
}