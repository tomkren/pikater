package org.pikater.shared.experiment;

import java.util.HashMap;
import java.util.Map;

import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.webformat.BoxInfo;
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
		if(uniFormat.isGUICompatible())
		{
			SchemaDataSource webFormat = new SchemaDataSource();

			// first convert all boxes
			Map<UniversalElement, Integer> uniBoxToWebBoxID = new HashMap<UniversalElement, Integer>();
			for(UniversalElement element : uniFormat.getElements())
			{
				BoxInfo info = boxInfoProvider.getByID(element.getElement().getType().getName()); 
				if(info != null)
				{
					Integer convertedBoxID = webFormat.addLeafBoxAndReturnID(element.getGui(), info);
					uniBoxToWebBoxID.put(element, convertedBoxID);
				}
				else
				{
					throw new ConversionException(new IllegalStateException(String.format(
							"No box definitions for ontology '%s'.", element.getElement().getType().getName())));
				}
			}
			
			// then convert all edges
			for(UniversalElement element : uniFormat.getElements())
			{
				for(UniversalConnector edge : element.getElement().getInputSlots())
				{
					webFormat.connect(
							uniBoxToWebBoxID.get(edge.getUniversalDataProvider()),
							uniBoxToWebBoxID.get(element)
					);
				}
			}
			
			// TODO: wrapper boxes, options & stuff
			
			return webFormat;
		}
		else
		{
			throw new ConversionException(new IllegalArgumentException(String.format(
					"The universal format below is not fully compatible with the GUI (web) format.\n%s", uniFormat.exportXML())));
		}
	}
}
