package org.pikater.web.visualisation.definition.result;

import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;

public class DSVisOneResult extends AbstractDSVisResult<String, DSVisOneSubresult>
{
	public DSVisOneResult(IProgressDialogResultHandler taskResult, int imageWidth, int imageHeight)
	{
		super(taskResult, imageWidth, imageHeight);
	}
	
	public DSVisOneSubresult createSingleImageResult(AttrMapping attrs, ImageType imageType)
	{
		DSVisOneSubresult newImageResult = new DSVisOneSubresult(attrs, imageType);
		registerSubresult(attrs.getAttrY(), attrs.getAttrX(), newImageResult);
		return newImageResult;
	}
}