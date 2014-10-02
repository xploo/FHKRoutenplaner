package de.damianbuecker.fhkroutenplaner.model;

import java.util.List;

public class Graph extends Model{
	  private final List<Vertex> vertexes;
	  private final List<Edge> edges;

	  public Graph(List<Vertex> vertexes, List<Edge> edges) {
	    this.vertexes = vertexes;
	    this.edges = edges;
	  }

	  public List<Vertex> getVertexes() {
	    return vertexes;
	  }

	  public List<Edge> getEdges() {
	    return edges;
	  }
	  
	  
	  
	} 