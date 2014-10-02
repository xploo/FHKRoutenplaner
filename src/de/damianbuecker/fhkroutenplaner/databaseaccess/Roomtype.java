package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Roomtype")
public class Roomtype {

	public static final String ROOMTYPE_ID = "roomtype_id";
	public static final String DESCRIPTION = "description";

	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = ROOMTYPE_ID)
	private int roomtype_id;

	@DatabaseField(canBeNull = false, useGetSet = true, columnName = DESCRIPTION)
	private String description;

	public Roomtype() {

	}

	public int getRoomtype_id() {
		return roomtype_id;
	}

	public void setRoomtype_id(int roomtype_id) {
		this.roomtype_id = roomtype_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
