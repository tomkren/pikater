package pikater.ontology.description;

import java.util.ArrayList;

import org.reflections.Reflections;

import com.thoughtworks.xstream.XStream;

public class ImportExportXML {

	public static String exportXML( Object object) {

		XStream xstream = new XStream();		

		String PACKAGE = "pikater.ontology.description";
		Reflections reflections = new Reflections(PACKAGE);
		
		ArrayList<Class<? extends Object>> allClassesArray =
				 new ArrayList<Class<? extends Object>>();
		allClassesArray.addAll(reflections.getSubTypesOf(IBox.class));
		allClassesArray.addAll(reflections.getSubTypesOf(IBoxWraper.class));
		allClassesArray.addAll(reflections.getSubTypesOf(IElement.class));

		for (Class<? extends Object> b : allClassesArray) {
			 String packageI = b.getPackage().toString()
					 .substring("package ".length());
			 xstream.aliasPackage("", packageI);
		}
		
		String xml = xstream.toXML(object);
		xml = xml.replace("class=\"", "type=\"");
		 
		return xml;
	}

	public static Object importXML(String xml) {

		xml = xml.replace("type=\"", "class=\"");
		
		XStream xstream = new XStream();
		
		String PACKAGE = "pikater.ontology.description";
		Reflections reflections = new Reflections(PACKAGE);
		
		ArrayList<Class<? extends Object>> allClassesArray =
				 new ArrayList<Class<? extends Object>>();
		allClassesArray.addAll(reflections.getSubTypesOf(IBox.class));
		allClassesArray.addAll(reflections.getSubTypesOf(IBoxWraper.class));
		allClassesArray.addAll(reflections.getSubTypesOf(IElement.class));

		for (Class<? extends Object> b : allClassesArray) {

			Class<?> clazz = null;
			try {
				clazz = Class.forName(b.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			xstream.alias(b.getSimpleName(), clazz);			 
		}


		return xstream.fromXML(xml);
	}
/*
	public String exportJSON() throws JSONException {

		JSONObject rootJSON = new JSONObject();

		JSONObject attrs = new JSONObject();
		rootJSON.put("attrs", attrs);
		rootJSON.put("className", "Layer");
		
		JSONObject child1Attrs = new JSONObject();
		child1Attrs.put("x", "0");
		child1Attrs.put("y", "0");

		JSONObject child1 = new JSONObject();
		child1.put("attrs", child1Attrs);
		child1.put("className", "Group");
		
		JSONArray childrenArray = new JSONArray();
		childrenArray.put(child1);
		childrenArray.put(child1);
		
		rootJSON.put("children", childrenArray);
		
		return rootJSON.toString();
	}
*/

}
