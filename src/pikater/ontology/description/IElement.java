package pikater.ontology.description;

import org.json.JSONObject;

import pikater.ontology.description.graph.GraphJSON;
import jade.content.Concept;

public interface IElement extends Concept {

	public String exportXML();

	public JSONObject exportJSON();
}
