package pikater.ontology.description;

import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.ArrayList;

import org.json.JSONObject;

import pikater.ontology.description.graph.GraphJSON;
import pikater.ontology.description.graph.VertexJSON;

public class BoxWraper implements IBoxWraper {

	int x = 0;
	int y = 0;
	
	public String exportXML() {
		return ImportExportXML.exportXML(this);
	}

	public static BoxWraper importXML(String xml) {
		return (BoxWraper) ImportExportXML.importXML(xml);
	}


	public GraphJSON exportJSON() {

		GraphJSON graph = new GraphJSON();
		int newId = graph.getNextID();

		VertexJSON vertex = new VertexJSON();
		vertex.setId(newId);
		vertex.setX(this.x);
		vertex.setY(this.y);
		vertex.setClassName(this.getClass().getName());
		
		graph.addVertex(vertex);

		String className = this.getClass().getName();		
		Class<?> thisClass = null;
		try {
			thisClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		for (Field field : thisClass.getDeclaredFields()) {

			String classNameI = field.getType().getName();
			
			Class<?> classI = null;
			try {
				classI = Class.forName(classNameI);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			

	 	    if (AbstractCollection.class.isAssignableFrom(classI) ) {
	 	    	//System.out.println("OK - ArrayList");

	 	        this.exportJSONcollection(field, vertex, graph);

	 	    } else if (IBoxWraper.class.isAssignableFrom(classI) ) {
	 	    	//System.out.println("OK - IBoxWrapper");

	 	    	IBoxWraper boxWraper = null;
	 	    	try {
					boxWraper = (IBoxWraper) field.get(this);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	 	    	GraphJSON graphBoxWraper = boxWraper.exportJSON();
	 	    	VertexJSON newVertex = graphBoxWraper.getVertexId(0);

	 	    	graph.addGraph(graphBoxWraper);
	 	    	graph.addEdge(vertex, newVertex);

	 	    } else if (IBox.class.isAssignableFrom(classI) ) {
	 	    	//System.out.println("OK - IBox");

	 	    	IBox box = null;
	 	    	try {
					box = (IBox) field.get(this);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	 	    	GraphJSON graphBox = box.exportJSON();
	 	    	VertexJSON newVertex = graphBox.getVertexId(0);

	 	    	graph.addGraph(graphBox);
	 	    	graph.addEdge(vertex, newVertex);

	 	    } else if (IElement.class.isAssignableFrom(classI) ) {
	 	    	//System.out.println("OK - IElement");

	 	    	IElement element = null;
	 	    	try {
	 	    		element = (IElement) field.get(this);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	 	    	JSONObject elementJSON = element.exportJSON();
	 	    	vertex.addAttribute(elementJSON);
	 	    }

		}
		
		return graph;
	}


	private void exportJSONcollection(Field field, VertexJSON vertex, GraphJSON graph) {
				
		try {
			field.setAccessible(true);
			AbstractCollection<Object> collection =
					(AbstractCollection<Object>) field.get(this);
			Object[] array = collection.toArray();

			for (Object object : array) {

		 	    if (IBox.class.isAssignableFrom(object.getClass()) ) {

		 	    	IBox item = (IBox) object;
		 	    	GraphJSON graphItem = item.exportJSON();

		 	    }
		 	    else if ( IElement.class.isAssignableFrom(object.getClass()) ) {

		 	    	IElement element = (IElement) object;
		 	    	JSONObject graphElement = element.exportJSON();

		 	    	ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
		 	    	arr.add(graphElement);
		 	    	vertex.addAttributes(arr);
		 	    }
			}

		} catch (IllegalArgumentException e) {
			System.out.println("Bug JSON - IllegalArgumentException");
		} catch (IllegalAccessException e) {
			System.out.println("Bug JSON - IllegalAccessException");
		}
 	    
	}

	
}
