package pikater.ontology.description;

import org.json.JSONObject;

import pikater.ontology.description.graph.GraphJSON;

public interface IBox {

	public String exportXML();

	public GraphJSON exportJSON();

}
