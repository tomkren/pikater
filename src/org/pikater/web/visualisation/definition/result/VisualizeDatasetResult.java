package org.pikater.web.visualisation.definition.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;

public class VisualizeDatasetResult extends AbstractDatasetVisualizationResult implements Iterable<VisualizeDatasetSubresult>
{
	/**
	 * Collection of generated images, with some additional attached data.
	 */
	private final Collection<VisualizeDatasetSubresult> resultImageCollection;

	public VisualizeDatasetResult(IProgressDialogResultHandler taskResult)
	{
		super(taskResult);
		
		this.resultImageCollection = new ArrayList<VisualizeDatasetSubresult>();
	}
	
	@Override
	public Iterator<VisualizeDatasetSubresult> iterator()
	{
		return resultImageCollection.iterator();
	}
	
	public VisualizeDatasetSubresult createSingleImageResult(AttrMapping attrs, ImageType imageType, int imageWidth, int imageHeight)
	{
		VisualizeDatasetSubresult newImageResult = new VisualizeDatasetSubresult(attrs, imageType, imageWidth, imageHeight);
		resultImageCollection.add(newImageResult);
		return newImageResult;
	}
}