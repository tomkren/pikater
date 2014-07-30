package org.pikater.web.visualisation.definition.result;

import java.io.File;

import org.pikater.shared.util.IOUtils;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.ImageType;

public class VisualizeDatasetSubresult
{
	private final AttrMapping attrs;
	private final ImageType imageType;
	private final File imageFile;
	private final int imageWidth;
	private final int imageHeight;

	public VisualizeDatasetSubresult(AttrMapping attrs, ImageType imageType, int imageWidth, int imageHeight)
	{
		this.attrs = attrs;
		this.imageType = imageType;
		this.imageFile = IOUtils.createTemporaryFile("visualization", imageType.toFileExtension());
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}

	public AttrMapping getAttrs()
	{
		return attrs;
	}
	public ImageType getImageType()
	{
		return imageType;
	}
	public File getFile()
	{
		return imageFile;
	}
	public int getImageWidth()
	{
		return imageWidth;
	}
	public int getImageHeight()
	{
		return imageHeight;
	}
}