package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user;

import java.io.InputStream;
import java.util.EnumSet;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.jirka.datasets.DataSetTableDBView;
import org.pikater.web.HttpContentType;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.ManageUserUploads;
import org.pikater.web.vaadin.gui.server.components.tabledbview.DBTableLayout;
import org.pikater.web.vaadin.gui.server.components.upload.IUploadedFileHandler;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class UserDatasetsView extends DBTableLayout implements IContentComponent
{
	private static final long serialVersionUID = -1564809345462937610L;
	
	public UserDatasetsView()
	{
		super(new DataSetTableDBView(ServerConfigurationInterface.avoidUsingDBForNow() ? JPAUser.getDummy() : ManageAuth.getUserEntity(VaadinSession.getCurrent())));
		setSizeFull();
		
		Button btn_uploadNewDatasets = new Button("Upload", new Button.ClickListener()
		{
			private static final long serialVersionUID = -1045335713385385849L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				new ManageUserUploads().getNewComponent(
						EnumSet.of(HttpContentType.APPLICATION_MS_EXCEL, HttpContentType.TEXT_CSV, HttpContentType.TEXT_PLAIN),
						new IUploadedFileHandler()
						{
							@Override
							public void handleFile(InputStream streamToLocalFile, String fileName, String mimeType, long sizeInBytes)
							{
								// TODO: upload the file to DB
								
							}
						}
				);
			}
		});
		// TODO: display help description that mentions renaming arff files to txt files because no such mime type exists
		addCustomActionButton(btn_uploadNewDatasets);
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
		// TODO: no views in constructors or does it work?
	}

	@Override
	public boolean hasUnsavedProgress()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCloseDialogMessage()
	{
		// TODO Auto-generated method stub
		return null;
	}
}