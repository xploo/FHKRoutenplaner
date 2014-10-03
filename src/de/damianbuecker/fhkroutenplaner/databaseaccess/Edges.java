package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Class Edges.
 */
@DatabaseTable(tableName = "Edges")
public class Edges {

	/** The Constant EDGE_ID. */
	public static final String EDGE_ID = "edge_id";

	/** The Constant SOURCE. */
	public static final String SOURCE = "source";

	/** The Constant DESTINATION. */
	public static final String DESTINATION = "destination";

	/** The Constant COST. */
	public static final String COST = "cost";

	/** The kante_id. */
	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = EDGE_ID)
	private int kante_id;

	/** The source. */
	@DatabaseField(canBeNull = true, useGetSet = true, columnName = SOURCE)
	private int source;

	/** The destination. */
	@DatabaseField(canBeNull = true, useGetSet = true, columnName = DESTINATION)
	private int destination;

	/** The cost. */
	@DatabaseField(canBeNull = true, useGetSet = true, columnName = COST)
	private int cost;

	/**
	 * Instantiates a new edges.
	 */
	public Edges() {

	}

	/**
	 * Gets the kante_id.
	 * 
	 * @return the kante_id
	 */
	public int getKante_id() {
		return kante_id;
	}

	/**
	 * Sets the kante_id.
	 * 
	 * @param kante_id
	 *            the new kante_id
	 */
	public void setKante_id(int kante_id) {
		this.kante_id = kante_id;
	}

	/**
	 * Gets the source.
	 * 
	 * @return the source
	 */
	public int getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 * 
	 * @param source
	 *            the new source
	 */
	public void setSource(int source) {
		this.source = source;
	}

	/**
	 * Gets the destination.
	 * 
	 * @return the destination
	 */
	public int getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 * 
	 * @param destination
	 *            the new destination
	 */
	public void setDestination(int destination) {
		this.destination = destination;
	}

	/**
	 * Gets the cost.
	 * 
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Sets the cost.
	 * 
	 * @param cost
	 *            the new cost
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

}
