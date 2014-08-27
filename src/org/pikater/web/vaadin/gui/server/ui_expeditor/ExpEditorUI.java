package org.pikater.web.vaadin.gui.server.ui_expeditor;

import org.pikater.core.agents.gateway.WebToCoreEntryPoint;
import org.pikater.shared.logging.web.PikaterLogger;
import org.pikater.web.config.AgentInfoCollection;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;

@Title("Experiments")
@Theme("pikater")
@Push(value = PushMode.AUTOMATIC)
public class ExpEditorUI extends CustomConfiguredUI
{
	private static final long serialVersionUID = -797960197800185978L;
	
	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * Don't forget to call this.
		 * IMPORTANT:
		 * 1) You shouldn't update the UI in this method. You only provide the content component
		 * when you're asked to in the {@link #displayChildContent()} method.
		 * 2) When {@link #displayChildContent()} is called, this method is still not finished.
		 * You shouldn't have any initializing code after the super.init() call.
		 */
		super.init(request);
	}
	
	@Override
	protected void displayChildContent()
	{
		/*
		 * Display editor if authenticated or make the user authenticate first and then display it.
		 */
		if(ManageAuth.isUserAuthenticated(VaadinSession.getCurrent()))
		{
			displayExperimentEditor();
		}
		else
		{
			forceUserToAuthenticate(new CustomConfiguredUI.IAuthenticationSuccessful()
			{
				@Override
				public void onSuccessfulAuth()
				{
					displayExperimentEditor();
				}
			});
		}
	}
	
	private AgentInfoCollection createAgentInfoProvider()
	{
		// fetch information about available agents from core or create dummy
		AgentInfoCollection agentInfoProvider = null;
		if(ServerConfigurationInterface.getConfig().coreEnabled)
		{
			try
			{
				agentInfoProvider = AgentInfoCollection.getFrom(WebToCoreEntryPoint.getAgentInfosVisibleForUser(
						ManageAuth.getUserID(VaadinSession.getCurrent())));
			}
			catch (Throwable t)
			{
				PikaterLogger.logThrowable("No information about agents received from core.", t);
				GeneralDialogs.error("Not available at this moment", "Information about agents has not been received from core.");
			}
		}
		else
		{
			agentInfoProvider = AgentInfoCollection.getDummyBoxes();
		}
		return agentInfoProvider;
	}
	
	private void displayExperimentEditor()
	{
		final AgentInfoCollection agentInfoProvider = createAgentInfoProvider();
		if(agentInfoProvider != null)
		{
			// disable regular page layout
			setPageCroppedAndHorizontallyCentered(false);
			
			// simply create a new empty editor and let the user handle the rest
			ExpEditor editor = new ExpEditor(agentInfoProvider);
			setContent(editor);
			
			// display an empty experiment by default
			editor.addEmptyTab();
		}
	}
}