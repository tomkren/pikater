package org.pikater.web.vaadin.gui.server.components.forms;

import java.io.File;
import java.util.EnumSet;

import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.web.HttpContentType;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.quartzjobs.UploadedAgentHandler;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.ManageSession;
import org.pikater.web.vaadin.ManageUserUploads;
import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.base.FormFieldFactory;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.upload.IFileUploadEvents;
import org.pikater.web.vaadin.gui.server.components.upload.MyMultiUpload;

import com.vaadin.server.VaadinSession;
import com.vaadin.server.StreamVariable.StreamingEndEvent;
import com.vaadin.server.StreamVariable.StreamingErrorEvent;
import com.vaadin.server.StreamVariable.StreamingStartEvent;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class AgentUploadForm extends CustomFormLayout
{
	private static final long serialVersionUID = -7727756941636752874L;
	
	private final TextField tf_agentName;
	private final TextField tf_agentClass;
	private final TextArea tf_agentDescription;
	private final MyMultiUpload upload;

	public AgentUploadForm(final Window parentPopup)
	{
		super(null);
		
		this.tf_agentName = FormFieldFactory.getGeneralTextField("Agent name:", "Name of the agent?", null, true, false);
		this.tf_agentClass = FormFieldFactory.getGeneralTextField("Agent class:", "Class of the agent?", null, true, false);
		this.tf_agentDescription = FormFieldFactory.getGeneralTextArea("Optional description:", "Any description of the agent for future reference?", null, false, false);
		
		this.tf_agentName.setSizeFull();
		addField("agent name", tf_agentName);
		this.tf_agentClass.setSizeFull();
		addField("class name", tf_agentClass);
		this.tf_agentDescription.setSizeFull();
		addField("description", tf_agentDescription);
		
		ManageUserUploads uploadManager = (ManageUserUploads) ManageSession.getAttribute(VaadinSession.getCurrent(), ManageSession.key_userUploads);
		this.upload = uploadManager.createUploadButton(
				"Choose file to upload (.jar)",
				EnumSet.of(HttpContentType.APPLICATION_JAR)
		);
		this.upload.addFileUploadEventsCallback(new IFileUploadEvents()
		{
			@Override
			public void uploadStarted(StreamingStartEvent event)
			{
				parentPopup.setVisible(false);
			}

			@Override
			public void uploadFailed(StreamingErrorEvent event)
			{
				/*
				 * Single file upload is assumed here.
				 */
				
				parentPopup.close();
			}

			@Override
			public void uploadFinished(StreamingEndEvent event, File uploadedTemporaryFile)
			{
				/*
				 * Single file upload is assumed here.
				 */
				
				if(!ServerConfigurationInterface.avoidUsingDBForNow())
				{
					Object[] jobParams = new Object[]
					{
							ManageAuth.getUserEntity(VaadinSession.getCurrent()),
							tf_agentName.getValue(),
							tf_agentClass.getValue(),
							tf_agentDescription.getValue(),
							uploadedTemporaryFile,
					};
					try
					{
						PikaterJobScheduler.getJobScheduler().defineJob(UploadedAgentHandler.class, jobParams);
					}
					catch (Throwable e)
					{
						PikaterLogger.logThrowable("Could not issue an uploaded agent handling job.", e);
						MyNotifications.showError("Upload failed", event.getFileName());
						return; // don't let the success notification be displayed
					}
					finally
					{
						parentPopup.close();
					}
				}
				
				MyNotifications.showSuccess("Upload successful", event.getFileName());
			}
		});
		this.upload.setEnabled(false);
		
		addCustomButtonInterface(this.upload);
	}

	@Override
	public IOnSubmit getSubmitAction()
	{
		return null; // we will not be using the clasical submit button
	}
	
	@Override
	protected synchronized void updateActionButton()
	{
		upload.setEnabled(isFormValidAndUpdated());
	}
}