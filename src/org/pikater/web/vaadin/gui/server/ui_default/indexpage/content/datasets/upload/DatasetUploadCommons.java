package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.datasets.upload;

import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;

public class DatasetUploadCommons implements IWizardCommon
{
	public String optionalARFFHeaders;
	public String optionalDatasetDescription;
	
	public DatasetUploadCommons()
	{
		this.optionalARFFHeaders = null;
		this.optionalDatasetDescription = null;
	}
}