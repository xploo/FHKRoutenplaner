package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 * Anlegen der Tabelle Edges
 */

@DatabaseTable(tableName = "Edges")
public class Edges {

	public static final String EDGE_ID = "edge_id";
	public static final String SOURCE = "source";
	public static final String DESTINATION = "destination";
	public static final String COST = "cost";
	
	/*
	 * Anlegen der Datenbank Spalten der Tabelle Edges
	 */

	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = EDGE_ID)
	private int kante_id;

	@DatabaseField(canBeNull = true, useGetSet = true, columnName = SOURCE)
	private int source;

	@DatabaseField(canBeNull = true, useGetSet = true, columnName = DESTINATION)
	private int destination;

	@DatabaseField(canBeNull = true, useGetSet = true, columnName = COST)
	private int cost;

	public Edges() {

	}

	/*
	 * Getter und Setter der Attribute
	 */
	public int getKante_id() {
		return kante_id;
	}

	public void setKante_id(int kante_id) {
		this.kante_id = kante_id;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

}
