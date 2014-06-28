package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user;

import java.io.File;
import java.util.EnumSet;

import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.shared.quartz.jobs.web.UploadedDatasetHandler;
import org.pikater.web.HttpContentType;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.ManageUserUploads;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.MyPopup;
import org.pikater.web.vaadin.gui.server.components.upload.IFileUploadEvents;
import org.pikater.web.vaadin.gui.server.components.upload.MyMultiUpload;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardWithOutput;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.ParentAwareWizardStep;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.admin.DatasetsView;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamVariable.StreamingEndEvent;
import com.vaadin.server.StreamVariable.StreamingErrorEvent;
import com.vaadin.server.StreamVariable.StreamingStartEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@StyleSheet("userDatasetsView.css")
public class UserDatasetsView extends DatasetsView
{
	private static final long serialVersionUID = 5568928739598297585L;
	
	public UserDatasetsView()
	{
		super();
		
		this.mainDatasetsLayout.addCustomActionComponent(new Button("Upload new dataset", new Button.ClickListener()
		{
			private static final long serialVersionUID = -1045335713385385849L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				MyPopup datasetUploadWizardWindow = new MyPopup("Dataset upload guide");
				datasetUploadWizardWindow.setWidth("600px");
				datasetUploadWizardWindow.setHeight("500px");
				datasetUploadWizardWindow.setContent(new DatasetUploadWizard(datasetUploadWizardWindow));
				datasetUploadWizardWindow.show();
			}
		}));
	}
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		if(ManageAuth.isUserAuthenticated(VaadinSession.getCurrent()))
		{
			this.mainDatasetsLayout.setCommitImmediately(false);
			this.underlyingView.setDatasetOwner(ManageAuth.getUserEntity(VaadinSession.getCurrent()));
			
			// always call these last, when you're absolutely ready to display the content
			this.mainDatasetsLayout.setView(new DatasetDBViewRoot());
			super.finishInit(); // don't forget to!
		}
		else
		{
			throw new IllegalStateException("User is not logged in.");
		}
	}
	
	//----------------------------------------------------------------------------
	// UPLOAD WIZARD

	private class DatasetUploadCommons implements IWizardCommon
	{
		public String optionalARFFHeaders;
		public String optionalDatasetDescription;
		
		public DatasetUploadCommons()
		{
			this.optionalARFFHeaders = null;
			this.optionalDatasetDescription = null;
		}
	}
	
	private class DatasetUploadWizard extends WizardWithOutput<DatasetUploadCommons>
	{
		private static final long serialVersionUID = -2782484084003504941L;
		
		private final Window parentPopup;
		
		public DatasetUploadWizard(Window parentPopup)
		{
			super(new DatasetUploadCommons());
			setSizeFull();
			DatasetUploadWizard.this.addStyleName("datasetUploadWizard");
			
			this.parentPopup = parentPopup;
			
			addStep(new Step1(this));
			addStep(new Step2(this));
			addStep(new Step3(this));
			
			getCancelButton().addClickListener(new Button.ClickListener()
			{
				private static final long serialVersionUID = 7767062741423812667L;

				@Override
				public void buttonClick(ClickEvent event)
				{
					closeWizardAndTheParentPopup();
				}
			});
			getFinishButton().setEnabled(false);
			getFinishButton().setVisible(false);
		}
		
		public void closeWizardAndTheParentPopup()
		{
			parentPopup.close();
		}
	}
	
	private class Step1 extends ParentAwareWizardStep<DatasetUploadCommons, DatasetUploadWizard>
	{
		private final VerticalLayout vLayout;
		private final TextArea textArea;
		
		public Step1(DatasetUploadWizard parentWizard)
		{
			super(parentWizard);
			
			this.vLayout = new VerticalLayout();
			this.vLayout.setSizeFull();
			this.vLayout.setStyleName("datasetUploadWizardStep");
			this.vLayout.setSpacing(true);
			
			Label label = new Label("You can optionally enter some ARFF headers that will be joined with the file you upload. "
					+ "Specify them when you're going to upload a non-ARFF file because the parser can not mine the headers then.</br>"
					+ "When you're done, click the 'Next' button.", ContentMode.HTML);
			label.setSizeUndefined();
			label.setStyleName("v-label-undefWidth-wordWrap");
			
			textArea = new TextArea();
			textArea.setWordwrap(false);
			textArea.setSizeFull();
			
			this.vLayout.addComponent(label);
			this.vLayout.addComponent(textArea);
			this.vLayout.setExpandRatio(textArea, 1);
		}

		@Override
		public String getCaption()
		{
			return "ARFF headers";
		}

		@Override
		public Component getContent()
		{
			return vLayout;
		}

		@Override
		public boolean onAdvance()
		{
			getOutput().optionalARFFHeaders = textArea.getValue().trim();
			return true;
		}

		@Override
		public boolean onBack()
		{
			return false;
		}
	}
	
	private class Step2 extends ParentAwareWizardStep<DatasetUploadCommons, DatasetUploadWizard>
	{
		private final VerticalLayout vLayout;
		private final TextArea textArea;
		
		public Step2(DatasetUploadWizard parentWizard)
		{
			super(parentWizard);
			
			this.vLayout = new VerticalLayout();
			this.vLayout.setSizeFull();
			this.vLayout.setStyleName("datasetUploadWizardStep");
			this.vLayout.setSpacing(true);
			
			Label label = new Label("Any special description to attach to the dataset for future reference?");
			label.setSizeUndefined();
			label.setStyleName("v-label-undefWidth-wordWrap");
			
			textArea = new TextArea();
			textArea.setWordwrap(false);
			textArea.setSizeFull();
			
			this.vLayout.addComponent(label);
			this.vLayout.addComponent(textArea);
			this.vLayout.setExpandRatio(textArea, 1);
		}

		@Override
		public String getCaption()
		{
			return "Description";
		}

		@Override
		public Component getContent()
		{
			return vLayout;
		}

		@Override
		public boolean onAdvance()
		{
			getOutput().optionalDatasetDescription = textArea.getValue().trim();
			return true;
		}

		@Override
		public boolean onBack()
		{
			return false;
		}
	}
	
	private class Step3 extends ParentAwareWizardStep<DatasetUploadCommons, DatasetUploadWizard>
	{
		private final VerticalLayout vLayout;
		
		public Step3(DatasetUploadWizard parentWizard)
		{
			super(parentWizard);
			this.vLayout = new VerticalLayout();
			this.vLayout.setStyleName("datasetUploadWizardStep");
			this.vLayout.addStyleName("maxWidth");
			this.vLayout.setSpacing(true);
			
			Label label = new Label("Currently, only '.xls', '.xlsx', '.csv' and '.txt' files are supported. All other extensions will be rejected.</br>"
					+ "Since there is no mime type for '.arff' files, you have to upload it as a '.txt' file.", ContentMode.HTML);
			label.setSizeUndefined();
			label.setStyleName("v-label-undefWidth-wordWrap");
			
			MyMultiUpload mmu = new ManageUserUploads().createUploadButton(
					"Choose file to upload",
					EnumSet.of(HttpContentType.APPLICATION_MS_EXCEL, HttpContentType.APPLICATION_MS_OFFICE_OPEN_SPREADSHEET, 
							HttpContentType.TEXT_CSV, HttpContentType.TEXT_PLAIN)
			);
			mmu.addFileUploadEventsCallback(new IFileUploadEvents()
			{
				@Override
				public void uploadStarted(StreamingStartEvent event)
				{
					getParentWizard().parentPopup.setVisible(false);
				}
				
				@Override
				public void uploadFailed(StreamingErrorEvent event)
				{
					/*
					 * Single file upload is assumed here.
					 */
					
					getParentWizard().closeWizardAndTheParentPopup();
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
								getOutput().optionalARFFHeaders,
								getOutput().optionalDatasetDescription,
								uploadedTemporaryFile,
						};
						try
						{
							PikaterJobScheduler.getJobScheduler().defineJob(UploadedDatasetHandler.class, jobParams);
						}
						catch (Throwable e)
						{
							PikaterLogger.logThrowable("Could not issue a dataset upload job.", e);
							MyNotifications.showError("Upload failed", event.getFileName());
							return; // don't let the success notification be displayed
						}
						finally
						{
							getParentWizard().closeWizardAndTheParentPopup();
						}
					}
					
					MyNotifications.showSuccess("Upload successful", event.getFileName());
				}
			});
			
			this.vLayout.addComponent(label);
			this.vLayout.addComponent(mmu);
		}

		@Override
		public String getCaption()
		{
			return "File to upload";
		}

		@Override
		public Component getContent()
		{
			return vLayout;
		}

		@Override
		public boolean onAdvance()
		{
			return false;
		}

		@Override
		public boolean onBack()
		{
			return true;
		}
	}
}