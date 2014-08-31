package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.datasets.upload;

import java.io.File;
import java.util.List;

import org.pikater.shared.logging.web.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.web.HttpContentType;
import org.pikater.web.config.WebAppConfiguration;
import org.pikater.web.quartzjobs.UploadedDatasetHandler;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.ManageSession;
import org.pikater.web.vaadin.ManageUserUploads;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.DialogCommons.IDialogComponent;
import org.pikater.web.vaadin.gui.server.components.upload.IFileUploadEvents;
import org.pikater.web.vaadin.gui.server.components.upload.MyMultiUpload;
import org.pikater.web.vaadin.gui.server.components.wizards.WizardForDialog;
import org.pikater.web.vaadin.gui.server.components.wizards.steps.ParentAwareWizardStep;

import com.vaadin.server.VaadinSession;
import com.vaadin.server.StreamVariable.StreamingEndEvent;
import com.vaadin.server.StreamVariable.StreamingErrorEvent;
import com.vaadin.server.StreamVariable.StreamingStartEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DatasetUploadWizard extends WizardForDialog<DatasetUploadCommons> implements IDialogComponent
{
	private static final long serialVersionUID = -2782484084003504941L;
	
	public DatasetUploadWizard() 
	{
		super(new DatasetUploadCommons());
		
		addStep(new Step1(this));
		addStep(new Step2(this));
		addStep(new Step3(this));
		
		// renders the 3 methods below useless
		getFinishButton().setEnabled(false); 
		getFinishButton().setVisible(false);
	}
	
	@Override
	public void addArgs(List<Object> arguments)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isResultReadyToBeHandled()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean handleResult(Object[] args)
	{
		throw new UnsupportedOperationException();
	}
	
	//--------------------------------------------------------------
	// INDIVIDUAL STEPS
	
	private class Step1 extends ParentAwareWizardStep<DatasetUploadCommons, DatasetUploadWizard>
	{
		private final VerticalLayout vLayout;
		private final TextArea textArea;
		
		public Step1(DatasetUploadWizard parentWizard)
		{
			super(parentWizard);
			
			this.vLayout = new VerticalLayout();
			this.vLayout.setSizeFull();
			this.vLayout.setSpacing(true);
			
			Label label = new Label("You can optionally specify some ARFF headers to be joined with the uploaded file, especially it "
					+ "is NOT an ARFF file.</br>"
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
			return true;
		}
	}

	private class Step3 extends ParentAwareWizardStep<DatasetUploadCommons, DatasetUploadWizard>
	{
		private final VerticalLayout vLayout;
		
		public Step3(DatasetUploadWizard parentWizard)
		{
			super(parentWizard);
			this.vLayout = new VerticalLayout();
			this.vLayout.addStyleName("maxWidth");
			this.vLayout.setSpacing(true);
			
			Label label = new Label("Currently, only '.xls', '.xlsx', '.csv' and '.txt' files are supported. All other extensions will be rejected.</br>"
					+ "Since there is no mime type for '.arff' files, you have to upload them as '.txt' files.", ContentMode.HTML);
			label.setSizeUndefined();
			label.setStyleName("v-label-undefWidth-wordWrap");
			
			ManageUserUploads uploadManager = (ManageUserUploads) ManageSession.getAttribute(VaadinSession.getCurrent(), ManageSession.key_userUploads); 
			MyMultiUpload mmu = uploadManager.createUploadButton(
					"Choose file to upload",
					HttpContentType.getDatasetUploadTypes()
			);
			mmu.addFileUploadEventsCallback(new IFileUploadEvents()
			{
				@Override
				public void uploadStarted(StreamingStartEvent event)
				{
					getParentPopup().setVisible(false);
				}
				
				@Override
				public void uploadFailed(StreamingErrorEvent event)
				{
					/*
					 * Single file upload is assumed here.
					 */
					getParentPopup().close();
				}
				
				@Override
				public void uploadFinished(StreamingEndEvent event, File uploadedTemporaryFile)
				{
					/*
					 * Single file upload is assumed here.
					 */
					
					if(!WebAppConfiguration.avoidUsingDBForNow())
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
							PikaterLogger.logThrowable("Could not issue an uploaded dataset handling job.", e);
							MyNotifications.showError("Upload failed", event.getFileName());
							return; // don't let the success notification be displayed
						}
						finally
						{
							getParentPopup().close();
						}
					}
					
					if(WebAppConfiguration.isCoreEnabled())
					{
						GeneralDialogs.info("Upload successful", "It may take a while before your dataset is processed and (for example) visualization "
								+ "can be invoked on it.");
					}
				}
				
				private Window getParentPopup()
				{
					return findAncestor(Window.class);
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