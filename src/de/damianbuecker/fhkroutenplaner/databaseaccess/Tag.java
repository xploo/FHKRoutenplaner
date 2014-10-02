package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Tag")
public class Tag {

	public static final String TAG_ID = "tag_id";
	public static final String FLOOR = "FLOOR";
	public static final String ROOM_ID = "room_id";
	public static final String X_POS = "x_pos";
	public static final String Y_POS = "y_pos";
	public static final String DESCRIPTION = "description";

	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = TAG_ID)
	private int tag_id;

	@DatabaseField(canBeNull = true, useGetSet = true, columnName = ROOM_ID)
	private int room_ID;

	@DatabaseField(useGetSet = true, canBeNull = false, columnName = X_POS)
	private double x_pos;

	@DatabaseField(useGetSet = true, canBeNull = false, columnName = Y_POS)
	private double y_pos;

	@DatabaseField(useGetSet = true, columnName = DESCRIPTION)
	private String description;

	@DatabaseField(useGetSet = true, columnName = FLOOR, canBeNull = false)
	private Integer floor;

	public Tag() {

	}

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public int getRoom_ID() {
		return room_ID;
	}

	public void setRoom_ID(int room_ID) {
		this.room_ID = room_ID;
	}

	public double getX_pos() {
		return x_pos;
	}

	public void setX_pos(double x_pos) {
		this.x_pos = x_pos;
	}

	public double getY_pos() {
		return y_pos;
	}

	public void setY_pos(double y_pos) {
		this.y_pos = y_pos;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}

}
