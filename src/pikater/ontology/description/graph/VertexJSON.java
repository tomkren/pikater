package pikater.ontology.description.graph;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class VertexJSON {

	private ArrayList<JSONObject> attributes = new ArrayList<JSONObject>();

	private int id = -1;
	private int x = 0;	
	private int y = 0;
	private String clasName = "noName";

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return this.id;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setClassName(String className) {
		this.clasName = className;
	}

	public void addAttribute(JSONObject newAttribute) {
		attributes.add(newAttribute);
	}
	public void addAttributes(ArrayList<JSONObject> newAttributes) {
		attributes.addAll(newAttributes);
	}
	public ArrayList<JSONObject> getAttributes() {
		return attributes;
	}
	
	public JSONObject getVertex() {
		
		JSONObject boxJSON = new JSONObject();
		try {
			JSONObject boxAttrsJSON = new JSONObject();

			boxAttrsJSON.put("id", this.id);
			boxAttrsJSON.put("x", this.x);
			boxAttrsJSON.put("y", this.y);
			boxAttrsJSON.put("class", this.clasName);

			boxJSON.put("box", boxAttrsJSON);

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return boxJSON;
	}

	public void printVertex() {

		System.out.println(this.getVertex().toString());

		for (JSONObject attributeI : attributes) {
			System.out.println("  " + attributeI.toString());			
		}
	}
}
