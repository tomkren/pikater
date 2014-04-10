package org.pikater.shared.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.options.BoxesExporter;
import org.pikater.core.options.LogicalUnitDescription;
import org.pikater.shared.AppConfig;
import org.pikater.web.experiment.box.LeafBox;

public final class BoxLoader {

	private BoxesExporter exporter = null;
	
	public BoxLoader() {
		exporter = new BoxesExporter();
	}
	
	// PUBLIC INTERFACE
	public ArrayList<Box> getBoxexOfType(BoxType type) {
		return exporter.getBoxexOfType(type);
		
	}
}