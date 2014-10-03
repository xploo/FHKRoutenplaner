package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Class Roomtype.
 */
@DatabaseTable(tableName = "Roomtype")
public class Roomtype {

	/** The Constant ROOMTYPE_ID. */
	public static final String ROOMTYPE_ID = "roomtype_id";

	/** The Constant DESCRIPTION. */
	public static final String DESCRIPTION = "description";

	/** The roomtype_id. */
	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = ROOMTYPE_ID)
	private int roomtype_id;

	/** The description. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = DESCRIPTION)
	private String description;

	/**
	 * Instantiates a new roomtype.
	 */
	public Roomtype() {

	}

	/**
	 * Gets the roomtype_id.
	 * 
	 * @return the roomtype_id
	 */
	public int getRoomtype_id() {
		return roomtype_id;
	}

	/**
	 * Sets the roomtype_id.
	 * 
	 * @param roomtype_id
	 *            the new roomtype_id
	 */
	public void setRoomtype_id(int roomtype_id) {
		this.roomtype_id = roomtype_id;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
