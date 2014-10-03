package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Class Room.
 */
@DatabaseTable(tableName = "Room")
public class Room {

	/** The Constant ROOM_ID. */
	public static final String ROOM_ID = "room_id";

	/** The Constant DESCRIPTION. */
	public static final String DESCRIPTION = "description";

	/** The Constant DOCENT_ID. */
	public static final String DOCENT_ID = "docent_id";

	/** The Constant ROOMTYPE_ID. */
	public static final String ROOMTYPE_ID = "roomtype_id";

	/** The Constant FLOOR. */
	public static final String FLOOR = "floor";

	/** The room_id. */
	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = ROOM_ID)
	private int room_id;

	/** The description. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = DESCRIPTION)
	private String description;

	/** The floor. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = FLOOR)
	private int floor;

	/** The docent_ id. */
	@DatabaseField(canBeNull = true, useGetSet = true, columnName = DOCENT_ID)
	private Integer docent_ID;

	/** The roomtype_ id. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = ROOMTYPE_ID)
	private int roomtype_ID;

	/**
	 * Instantiates a new room.
	 */
	public Room() {
	}

	/**
	 * Gets the room_id.
	 * 
	 * @return the room_id
	 */
	public int getRoom_id() {
		return room_id;
	}

	/**
	 * Sets the room_id.
	 * 
	 * @param room_id
	 *            the new room_id
	 */
	public void setRoom_id(int room_id) {
		this.room_id = room_id;
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

	/**
	 * Gets the floor.
	 * 
	 * @return the floor
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * Sets the floor.
	 * 
	 * @param floor
	 *            the new floor
	 */
	public void setFloor(int floor) {
		this.floor = floor;
	}

	/**
	 * Gets the docent_ id.
	 * 
	 * @return the docent_ id
	 */
	public Integer getDocent_ID() {
		return docent_ID;
	}

	/**
	 * Sets the docent_ id.
	 * 
	 * @param docent_ID
	 *            the new docent_ id
	 */
	public void setDocent_ID(Integer docent_ID) {
		this.docent_ID = docent_ID;
	}

	/**
	 * Gets the roomtype_ id.
	 * 
	 * @return the roomtype_ id
	 */
	public int getRoomtype_ID() {
		return roomtype_ID;
	}

	/**
	 * Sets the roomtype_ id.
	 * 
	 * @param roomtype_ID
	 *            the new roomtype_ id
	 */
	public void setRoomtype_ID(int roomtype_ID) {
		this.roomtype_ID = roomtype_ID;
	}

}
