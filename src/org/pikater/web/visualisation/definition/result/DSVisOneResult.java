package org.pikater.web.visualisation.definition.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;

public class DSVisOneResult extends AbstractDSVisResult implements Iterable<DSVisOneSubresult>
{
	/**
	 * Collection of generated images, with some additional attached data.
	 */
	private final Collection<DSVisOneSubresult> resultImageCollection;
	
	public DSVisOneResult(IProgressDialogResultHandler taskResult, int imageWidth, int imageHeight)
	{
		super(taskResult, imageWidth, imageHeight);
		
		this.resultImageCollection = new ArrayList<DSVisOneSubresult>();
	}
	
	@Override
	public Iterator<DSVisOneSubresult> iterator()
	{
		return resultImageCollection.iterator();
	}
	
	public DSVisOneSubresult createSingleImageResult(AttrMapping attrs, ImageType imageType)
	{
		DSVisOneSubresult newImageResult = new DSVisOneSubresult(attrs, imageType);
		resultImageCollection.add(newImageResult);
		return newImageResult;
	}
}