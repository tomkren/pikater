package org.pikater.web.experiment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.options.BoxesExporter;
import org.pikater.core.options.LogicalUnit;
import org.pikater.web.experiment.box.LeafBox;

public class BoxesImporter
{
	private List<LeafBox> boxes = new ArrayList<LeafBox>();
	
	public BoxesImporter() {

		BoxesExporter exporter = new BoxesExporter();

		List<LogicalUnit> logicalUnits = null;
		try {
			logicalUnits = exporter.export();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (LogicalUnit logUnit : logicalUnits) {

			String displayName = logUnit.getName();
			String picture = logUnit.getPicture();
			String description = logUnit.getDescription();
			String boxType = logUnit.getType();

			LeafBox leafboxI = new LeafBox(displayName, boxType, picture, description);
			boxes.add(leafboxI);
		}

	}

	public List<LeafBox> getAllBoxes() {
		return this.boxes;
	}
	
	public LeafBox getBoxBy(String name) {
		
		for (LeafBox boxI : this.boxes) {
			
			if (boxI.getDisplayName().equals(name)) {
				return boxI;
			}
		}
		
		return null;
	}
	
}
