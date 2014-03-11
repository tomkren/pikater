package pikater.ontology.description;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import pikater.ontology.description.graph.GraphJSON;
import pikater.ontology.description.graph.VertexJSON;


public class Element implements IElement {


	public String exportXML() {
		return ImportExportXML.exportXML(this);
	}

	public static Element importXML(String xml) {
		return (Element) ImportExportXML.importXML(xml);
	}

	public JSONObject exportJSON() {
	
        JSONObject xmlJSONObj = null;
		try {
			xmlJSONObj = XML.toJSONObject(this.exportXML());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return xmlJSONObj;
	}
	
	public void foo() {
		System.out.println("Ahoj Franto");
	}
}
