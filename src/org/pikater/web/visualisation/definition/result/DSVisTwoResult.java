package org.pikater.web.visualisation.definition.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.pikater.shared.util.Tuple;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;

public class DSVisTwoResult extends AbstractDSVisResult implements Iterable<DSVisTwoSubresult>
{
	/**
	 * Collection of generated images, with some additional attached data.
	 */
	private final Collection<DSVisTwoSubresult> resultImageCollection;
	
	public DSVisTwoResult(IProgressDialogResultHandler taskResult, int imageWidth, int imageHeight)
	{
		super(taskResult, imageWidth, imageHeight);
		
		this.resultImageCollection = new ArrayList<DSVisTwoSubresult>();
	}
	
	@Override
	public Iterator<DSVisTwoSubresult> iterator()
	{
		return resultImageCollection.iterator();
	}
	
	public DSVisTwoSubresult createSingleImageResult(Tuple<AttrMapping, AttrMapping> attrInfo, ImageType imageType)
	{
		DSVisTwoSubresult newImageResult = new DSVisTwoSubresult(imageType, attrInfo);
		resultImageCollection.add(newImageResult);
		return newImageResult;
	}
}