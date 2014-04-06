package org.newpikater.util.boxes.types;

import org.newpikater.util.boxes.BoxesImporter;


public class WrapperBoxFactory
{
	public static WrapperBox getInputSearcherVisualizer()
	{
		BoxesImporter importer = new BoxesImporter();
		
		LeafBox fileInput = importer.getBoxBy("FileInput");
		LeafBox randomSearcher = importer.getBoxBy("Random-Searcher");
		LeafBox fileVisualizer = importer.getBoxBy("FileVisualizer");
		
		WrapperBox result =
				new WrapperBox("InputSearcherVisualizer",
						"picture-NAV",null, fileInput, randomSearcher, fileVisualizer);
		
		// TODO: connect the boxes somehow?
		
		return result;
	}
}
