package org.pikater.web.visualisation.definition.result;

import java.io.File;

import org.pikater.shared.util.IOUtils;
import org.pikater.web.visualisation.definition.ImageType;

public abstract class AbstractDSVisSubresult<I extends Object & Comparable<? super I>>
{
	private final ImageType imageType;
	private final File imageFile;

	public AbstractDSVisSubresult(ImageType imageType)
	{
		this.imageType = imageType;
		this.imageFile = IOUtils.createTemporaryFile("visualization", imageType.toFileExtension());
	}

	public ImageType getImageType()
	{
		return imageType;
	}
	public File getFile()
	{
		return imageFile;
	}
	
	//----------------------------------------------------
	// SOME ABSTRACT INTERFACE
	
	public abstract I toLeftIndex();
	public abstract I toTopIndex();
}