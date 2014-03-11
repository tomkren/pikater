package pikater.ontology.description;


import pikater.ontology.description.graph.GraphJSON;
import pikater.ontology.description.graph.VertexJSON;

public class Box implements IBox {

	public int x = 0;
	public int y = 0;

	public String exportXML() {
		return ImportExportXML.exportXML(this);
	}

	public static Box importXML(String xml) {
		return (Box) ImportExportXML.importXML(xml);
	}

	public GraphJSON exportJSON() {
		
		GraphJSON graph = new GraphJSON();

		VertexJSON vertex = new VertexJSON();
		vertex.setId(0);
		vertex.setX(this.x);
		vertex.setY(this.y);
		vertex.setClassName(this.getClass().getName());
		
		graph.addVertex(vertex);
		
		return graph;
	}
	
}