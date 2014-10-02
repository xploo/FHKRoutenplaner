package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Room")
public class Room {

	public static final String ROOM_ID = "room_id";
	public static final String DESCRIPTION = "description";
	public static final String DOCENT_ID = "docent_id";
	public static final String ROOMTYPE_ID = "roomtype_id";
	public static final String FLOOR = "floor";

	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = ROOM_ID)
	private int room_id;

	@DatabaseField(canBeNull = false, useGetSet = true, columnName = DESCRIPTION)
	private String description;

	@DatabaseField(canBeNull = false, useGetSet = true, columnName = FLOOR)
	private int floor;

	@DatabaseField(canBeNull = true, useGetSet = true, columnName = DOCENT_ID)
	private Integer docent_ID;

	@DatabaseField(canBeNull = false, useGetSet = true, columnName = ROOMTYPE_ID)
	private int roomtype_ID;

	public Room() {
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public Integer getDocent_ID() {
		return docent_ID;
	}

	public void setDocent_ID(Integer docent_ID) {
		this.docent_ID = docent_ID;
	}

	public int getRoomtype_ID() {
		return roomtype_ID;
	}

	public void setRoomtype_ID(int roomtype_ID) {
		this.roomtype_ID = roomtype_ID;
	}

	

}
