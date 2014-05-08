package org.pikater.shared.experiment;

import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.webformat.BoxInfoCollection;
import org.pikater.shared.experiment.webformat.SchemaDataSource;

public class UniToWebFormatConverter
{
	public static SchemaDataSource convert(UniversalComputationDescription uniFormat, BoxInfoCollection boxInfoProvider) throws ConversionException
	{
		// first some checks
		if(uniFormat == null)
		{
			throw new ConversionException(new NullPointerException("The argument universal format is null."));
		}
		else if(boxInfoProvider == null)
		{
			throw new ConversionException(new NullPointerException("The argument box info provider is null."));
		}
		
		// and then onto the conversion
		
		// TODO:
		return null;
	}
}
