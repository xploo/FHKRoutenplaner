package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Class Tag.
 */
@DatabaseTable(tableName = "Tag")
public class Tag {

	/** The Constant TAG_ID. */
	public static final String TAG_ID = "tag_id";

	/** The Constant FLOOR. */
	public static final String FLOOR = "FLOOR";

	/** The Constant ROOM_ID. */
	public static final String ROOM_ID = "room_id";

	/** The Constant X_POS. */
	public static final String X_POS = "x_pos";

	/** The Constant Y_POS. */
	public static final String Y_POS = "y_pos";

	/** The Constant DESCRIPTION. */
	public static final String DESCRIPTION = "description";

	/** The tag_id. */
	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = TAG_ID)
	private int tag_id;

	/** The room_ id. */
	@DatabaseField(canBeNull = true, useGetSet = true, columnName = ROOM_ID)
	private int room_ID;

	/** The x_pos. */
	@DatabaseField(useGetSet = true, canBeNull = false, columnName = X_POS)
	private double x_pos;

	/** The y_pos. */
	@DatabaseField(useGetSet = true, canBeNull = false, columnName = Y_POS)
	private double y_pos;

	/** The description. */
	@DatabaseField(useGetSet = true, columnName = DESCRIPTION)
	private String description;

	/** The floor. */
	@DatabaseField(useGetSet = true, columnName = FLOOR, canBeNull = false)
	private Integer floor;

	/**
	 * Instantiates a new tag.
	 */
	public Tag() {

	}

	/**
	 * Gets the tag_id.
	 * 
	 * @return the tag_id
	 */
	public int getTag_id() {
		return tag_id;
	}

	/**
	 * Sets the tag_id.
	 * 
	 * @param tag_id
	 *            the new tag_id
	 */
	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	/**
	 * Gets the room_ id.
	 * 
	 * @return the room_ id
	 */
	public int getRoom_ID() {
		return room_ID;
	}

	/**
	 * Sets the room_ id.
	 * 
	 * @param room_ID
	 *            the new room_ id
	 */
	public void setRoom_ID(int room_ID) {
		this.room_ID = room_ID;
	}

	/**
	 * Gets the x_pos.
	 * 
	 * @return the x_pos
	 */
	public double getX_pos() {
		return x_pos;
	}

	/**
	 * Sets the x_pos.
	 * 
	 * @param x_pos
	 *            the new x_pos
	 */
	public void setX_pos(double x_pos) {
		this.x_pos = x_pos;
	}

	/**
	 * Gets the y_pos.
	 * 
	 * @return the y_pos
	 */
	public double getY_pos() {
		return y_pos;
	}

	/**
	 * Sets the y_pos.
	 * 
	 * @param y_pos
	 *            the new y_pos
	 */
	public void setY_pos(double y_pos) {
		this.y_pos = y_pos;
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
	public Integer getFloor() {
		return floor;
	}

	/**
	 * Sets the floor.
	 * 
	 * @param floor
	 *            the new floor
	 */
	public void setFloor(Integer floor) {
		this.floor = floor;
	}

}
