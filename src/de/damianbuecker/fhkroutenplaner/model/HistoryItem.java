package de.damianbuecker.fhkroutenplaner.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Class HistoryItem.
 */
@DatabaseTable(tableName = "HistoryItems")
public class HistoryItem extends Model {

	/** The Constant ID. */
	public static final String ID = "id";

	/** The Constant DESTINATION. */
	public static final String DESTINATION = "destination";

	/** The Constant START. */
	public static final String START = "start";

	/** The Constant TIMESTAMP. */
	public static final String TIMESTAMP = "timestamp";

	/** The Constant DATE. */
	public static final String DATE = "date";

	/** The Constant NAME. */
	public static final String NAME = "name";

	/** The id. */
	@DatabaseField(canBeNull = false, useGetSet = true, generatedId = true, columnName = ID)
	private int id;

	/** The destination. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = DESTINATION)
	private String destination;

	/** The start. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = START)
	private String start;

	/** The timestamp. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = TIMESTAMP)
	private long timestamp;

	/** The date. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = DATE)
	private long date;

	/** The name. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = NAME)
	private String name;

	/**
	 * Instantiates a new history item.
	 */
	public HistoryItem() {
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the destination.
	 * 
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 * 
	 * @param destination
	 *            the new destination
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * Gets the start.
	 * 
	 * @return the start
	 */
	public String getStart() {
		return start;
	}

	/**
	 * Sets the start.
	 * 
	 * @param start
	 *            the new start
	 */
	public void setStart(String start) {
		this.start = start;
	}

	/**
	 * Gets the timestamp.
	 * 
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 * 
	 * @param timestamp
	 *            the new timestamp
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date
	 *            the new date
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

}
