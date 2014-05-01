package org.pikater.shared.experiment.box;

import java.util.ArrayList;

import org.pikater.core.options.BoxesExporter;
import org.pikater.shared.experiment.box.BoxInfo.BoxType;

// TODO: merge this with BoxesExporter. Pointless to have 2 classes do the same.
public final class BoxLoader {

	private BoxesExporter exporter = null;
	
	public BoxLoader() {
		exporter = new BoxesExporter();
	}
	
	// PUBLIC INTERFACE
	public ArrayList<BoxInfo> getBoxexOfType(BoxType type) {
		return exporter.getBoxexOfType(type);
		
	}
}