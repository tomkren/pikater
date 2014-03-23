package pikater.tests.ontology.description;

import java.util.ArrayList;

import org.codehaus.jettison.json.JSONException;
import org.json.JSONObject;

import pikater.ontology.description.BooleanParameter;
import pikater.ontology.description.ComputationDescription;
import pikater.ontology.description.FileVisualizer;
import pikater.ontology.description.examples.SearchOnly;

public class ImportExportJSON {

	public void run() {
        
		FileVisualizer fileVis = new FileVisualizer();
/*
		BooleanParameter bParameter = new BooleanParameter("parameter1", true);
		ArrayList<Parameter> globalParameters = new ArrayList<Parameter>();
		globalParameters.add(bParameter);

		ComputationDescription compDesc = new ComputationDescription();
		compDesc.setRootElement(fileVis);
		compDesc.setGlobalParameters(globalParameters);

        GraphJSON graph = compDesc.exportJSON();
        graph.printGraph();
*/		
	}
}
