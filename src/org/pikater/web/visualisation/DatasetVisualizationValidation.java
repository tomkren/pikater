package org.pikater.web.visualisation;

import org.pikater.web.visualisation.definition.AttrMapping;

public class DatasetVisualizationValidation
{
	/**
	 * Checks whether the given attributes can be used as input for visualization.</br>
	 * The visualization output is supposed to be a single image.
	 * @param attributes
	 * @return
	 */
	public static boolean areCompatible(AttrMapping attributes)
	{
		return true;
	}
	
	/**
	 * Checks whether the given attributes can be used as input for comparison.</br>
	 * The visualization output is supposed to be a single image. 
	 * @param attributes1
	 * @param attributes2
	 * @return
	 */
	public static boolean areCompatible(AttrMapping attributes1, AttrMapping attributes2)
	{
		return attributes1.getAttrX().isVisuallyCompatible(attributes2.getAttrX()) &&
				attributes1.getAttrY().isVisuallyCompatible(attributes2.getAttrY()) &&
				attributes1.getAttrTarget().isVisuallyCompatible(attributes2.getAttrTarget());
	}
}