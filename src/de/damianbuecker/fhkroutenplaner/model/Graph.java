package de.damianbuecker.fhkroutenplaner.model;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Graph.
 */
public class Graph extends Model {

	/** The vertexes. */
	private final List<Vertex> vertexes;

	/** The edges. */
	private final List<Edge> edges;

	/**
	 * Instantiates a new graph.
	 * 
	 * @param vertexes
	 *            the vertexes
	 * @param edges
	 *            the edges
	 */
	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	/**
	 * Gets the vertexes.
	 * 
	 * @return the vertexes
	 */
	public List<Vertex> getVertexes() {
		return vertexes;
	}

	/**
	 * Gets the edges.
	 * 
	 * @return the edges
	 */
	public List<Edge> getEdges() {
		return edges;
	}

}