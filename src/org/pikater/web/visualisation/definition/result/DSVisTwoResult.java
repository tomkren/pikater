package org.pikater.web.visualisation.definition.result;

import org.pikater.shared.util.Tuple;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;

public class DSVisTwoResult extends AbstractDSVisResult<AttrMapping, DSVisTwoSubresult>
{
	public DSVisTwoResult(IProgressDialogResultHandler taskResult, int imageWidth, int imageHeight)
	{
		super(taskResult, imageWidth, imageHeight);
	}
	
	public DSVisTwoSubresult createSingleImageResult(Tuple<AttrMapping, AttrMapping> attrInfo, ImageType imageType)
	{
		DSVisTwoSubresult newImageResult = new DSVisTwoSubresult(imageType, attrInfo);
		registerSubresult(newImageResult.toLeftIndex(), newImageResult.toTopIndex(), newImageResult);
		return newImageResult;
	}
}