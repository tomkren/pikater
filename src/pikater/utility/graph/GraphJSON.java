package pikater.utility.graph;

import java.util.ArrayList;


public class GraphJSON {
	
	private ArrayList<VertexJSON> vertices = new ArrayList<VertexJSON>();
	public ArrayList<Integer[]> edges = new ArrayList<Integer []>();

	public void addGraph(GraphJSON graph) {
		
		int idDIff = this.getNextID();
		
		ArrayList<VertexJSON> newVertices = graph.getVertices();
		
		for (VertexJSON newV : newVertices) {
			newV.setId(newV.getId() + idDIff);
		}
		
		this.vertices.addAll(graph.getVertices());
	}
	
	public void addVertex(VertexJSON newVertex) {
		this.vertices.add(newVertex);
	}

	public void addVertex(ArrayList<VertexJSON> newVertices) {
		this.vertices.addAll(newVertices);
	}

	public VertexJSON getVertexId(int id) {
		
		for (VertexJSON vertex : this.vertices) {
			if (vertex.getId() == id) {
				return vertex;
			}
		}
		return null;
	}

	public ArrayList<VertexJSON> getVertices() {
		return this.vertices;
	}

	public int getNextID() {
		return this.vertices.size();
	}

	public void addEdge(VertexJSON vertex1, VertexJSON vertex2) {
		
		int id1 = vertex1.getId();
		int id2 = vertex2.getId();

		Integer[] edge = new Integer[2];
		edge[0] = id1;
		edge[1] = id2;
		
		edges.add(edge);
	}
	
	public void printGraph() {

		System.out.println("Vertices:");
		
		for (VertexJSON vertex : vertices) {
			vertex.printVertex();
		}

		System.out.println("Edges:");
		
		for (Integer[] edge : edges) {
			System.out.print("{" + edge[0] + "," + edge[1] + "}" );
		}
		
	}
}
