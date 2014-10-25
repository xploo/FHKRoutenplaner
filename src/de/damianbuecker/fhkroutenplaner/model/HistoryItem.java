package de.damianbuecker.fhkroutenplaner.model;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

// TODO: Auto-generated Javadoc
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
	@DatabaseField(canBeNull = false, generatedId = true, columnName = ID)
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
	 * Instantiates a new history item.
	 * 
	 * @param id
	 *            the id
	 * @param destination
	 *            the destination
	 * @param start
	 *            the start
	 * @param timestamp
	 *            the timestamp
	 * @param date
	 *            the date
	 * @param name
	 *            the name
	 */
	public HistoryItem(int id, String destination, String start, long timestamp, long date, String name) {
		this.id = id;
		this.destination = destination;
		this.start = start;
		this.timestamp = timestamp;
		this.date = date;
		this.name = name;
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
	 * Sets the id. Not needed.
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

	/**
	 * From json.
	 * 
	 * @param json
	 *            the json
	 * @return the history item
	 */
	public HistoryItem fromJson(String json) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeHierarchyAdapter(HistoryItem.class, new HistoryItemTypeAdapter());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();

		return gson.fromJson(json, HistoryItem.class);
	}

	/**
	 * To json.
	 * 
	 * @param historyItem
	 *            the history item
	 * @return the string
	 */
	public String toJson(HistoryItem historyItem) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeHierarchyAdapter(HistoryItem.class, new HistoryItemTypeAdapter());
		gsonBuilder.setPrettyPrinting();
		Gson gson = gsonBuilder.create();

		return gson.toJson(historyItem);
	}

	/**
	 * The Class HistoryItemTypeAdapter.
	 */
	public class HistoryItemTypeAdapter extends TypeAdapter<HistoryItem> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gson.TypeAdapter#read(com.google.gson.stream.JsonReader)
		 */
		@Override
		public HistoryItem read(JsonReader in) throws IOException {
			if (in.peek() == JsonToken.NULL) {
				in.nextNull();
				return null;
			}
			String data = in.nextString();
			String[] dataParts = data.split(",");
			return new HistoryItem(Integer.parseInt(dataParts[0]), dataParts[1], dataParts[2], Long.parseLong(dataParts[3]),
					Long.parseLong(dataParts[4]), dataParts[5]);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.google.gson.TypeAdapter#write(com.google.gson.stream.JsonWriter,
		 * java.lang.Object)
		 */
		@Override
		public void write(JsonWriter out, HistoryItem value) throws IOException {
			if (value == null) {
				out.nullValue();
				return;
			}
			StringBuffer sb = new StringBuffer("");
			sb.append(value.getId()).append(",").append(value.getDestination()).append(",").append(value.getStart()).append(",")
					.append(value.getTimestamp()).append(",").append(value.getDate()).append(",").append(value.getName());
			out.value(sb.toString());
		}
	}
}
