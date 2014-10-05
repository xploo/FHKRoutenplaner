package de.damianbuecker.fhkroutenplaner.model;

/**
 * The Class Edge.
 */
public class Edge extends Model {

	/** The id. */
	private String id;

	/** The source. */
	private Vertex source;

	/** The destination. */
	private Vertex destination;

	/** The weight. */
	private int weight;

	/**
	 * Instantiates a new edge.
	 * 
	 * @param id
	 *            the id
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @param weight
	 *            the weight
	 */
	public Edge(String id, Vertex source, Vertex destination, int weight) {
		this.id = id;
		this.source = source;
		this.destination = destination;
		this.weight = weight;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the destination.
	 * 
	 * @return the destination
	 */
	public Vertex getDestination() {
		return destination;
	}

	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public Vertex getSource() {
		return source;
	}

	/**
	 * Gets the weight.
	 * 
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the source.
	 * 
	 * @param source
	 *            the new source
	 */
	public void setSource(Vertex source) {
		this.source = source;
	}

	/**
	 * Sets the destination.
	 * 
	 * @param destination
	 *            the new destination
	 */
	public void setDestination(Vertex destination) {
		this.destination = destination;
	}

	/**
	 * Sets the weight.
	 * 
	 * @param weight
	 *            the new weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return source + " " + destination;
	}
}