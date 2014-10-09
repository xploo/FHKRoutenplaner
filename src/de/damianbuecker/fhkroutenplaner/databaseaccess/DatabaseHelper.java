package de.damianbuecker.fhkroutenplaner.databaseaccess;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "database.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Tag, Integer> tagDataDao;
	private Dao<HistoryItem, Integer> historyItemDataDao;

	/** The room data dao. */
	private Dao<Room, Integer> roomDataDao;
	private Dao<Docent, Integer> docentDataDao;
	private Dao<Roomtype, Integer> roomtypeDataDao;
	private Dao<Edges, Integer> edgesDataDao;
	private Dao<Roomtype, Integer> roomtypeSpinner;
	private Dao<Tag, Integer> tagLocationAll;
	private Dao<Edges, Integer> edgesAll;
	private Dao<Tag, Integer> tagLocationById;
	private Dao<Room, Integer> roomSpinner;
	private List spinnerRoomList = null;
	private List spinnerRoomtypeList = new ArrayList<Integer>();
	private StringBuilder sb = new StringBuilder();
	private QueryBuilder<Room, Integer> queryBuilder = null;
	private QueryBuilder<Tag, Integer> queryBuilderTag = null;
	private QueryBuilder<Edges, Integer> queryBuilderEdges = null;
	private List<Tag> Tagresult = null;
	private List<Edges> edgesResult = null;
	private List<Edges> edgeResultRemain = null;
	private List<Edges> edgesResultDestination = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			/**
			 * Dropping table in order to clean up db on restart. Normally you
			 * wouldn't drop the table every time you run the onCreate.
			 */
			TableUtils.dropTable(connectionSource, Tag.class, true);
			TableUtils.dropTable(connectionSource, Room.class, true);
			TableUtils.dropTable(connectionSource, Docent.class, true);
			TableUtils.dropTable(connectionSource, Roomtype.class, true);
			TableUtils.dropTable(connectionSource, Edges.class, true);
			TableUtils.dropTable(connectionSource, HistoryItem.class, true);

			/**
			 * Creates the table.
			 */
			TableUtils.createTable(connectionSource, Tag.class);
			TableUtils.createTable(connectionSource, Room.class);
			TableUtils.createTable(connectionSource, Docent.class);
			TableUtils.createTable(connectionSource, Roomtype.class);
			TableUtils.createTable(connectionSource, Edges.class);
			TableUtils.createTable(connectionSource, HistoryItem.class);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			/**
			 * In case of an update.
			 */
			TableUtils.dropTable(connectionSource, Tag.class, true);
			TableUtils.dropTable(connectionSource, Room.class, true);
			TableUtils.dropTable(connectionSource, Docent.class, true);
			TableUtils.dropTable(connectionSource, Roomtype.class, true);
			TableUtils.dropTable(connectionSource, Edges.class, true);
			TableUtils.dropTable(connectionSource, HistoryItem.class, true);

			this.onCreate(database, connectionSource);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Dao<Tag, Integer> getTagDataDao() throws SQLException {
		if (this.tagDataDao == null) {
			tagDataDao = getDao(Tag.class);
		}

		return this.tagDataDao;
	}

	public Dao<Room, Integer> getRoomDataDao() throws SQLException {
		if (this.roomDataDao == null) {
			roomDataDao = getDao(Room.class);
		}

		return this.roomDataDao;
	}

	public Dao<Docent, Integer> getDocentDataDao() throws SQLException {
		if (this.docentDataDao == null) {
			docentDataDao = getDao(Docent.class);
		}

		return this.docentDataDao;
	}

	public Dao<Roomtype, Integer> getRoomtypeDataDao() throws SQLException {
		if (this.roomtypeDataDao == null) {
			roomtypeDataDao = getDao(Roomtype.class);
		}

		return this.roomtypeDataDao;
	}

	public Dao<Edges, Integer> getEdgesDataDao() throws SQLException {
		if (this.edgesDataDao == null) {
			edgesDataDao = getDao(Edges.class);
		}

		return this.edgesDataDao;
	}

	public Dao<HistoryItem, Integer> getHistoryItemDataDao()
			throws SQLException {
		if (this.historyItemDataDao == null) {
			historyItemDataDao = getDao(HistoryItem.class);
		}
		return this.historyItemDataDao;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getRoomtypeSpinner() throws SQLException {
		if (this.roomtypeSpinner == null) {
			this.roomtypeSpinner = getRoomtypeDataDao();

			for (Roomtype v : roomtypeSpinner) {
				spinnerRoomtypeList.add(String.valueOf(v.getRoomtype_id())
						+ "  " + String.valueOf(v.getDescription()));
			}

		}

		return spinnerRoomtypeList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getRoomSpinner(Integer roomid) throws SQLException {
		Log.v("LOL", "BIN IN DATABASEHANDLER");
		if (this.roomSpinner == null) {
			this.roomSpinner = this.getRoomDataDao();
		}
		this.spinnerRoomList = null;
		this.spinnerRoomList = new ArrayList<Integer>();
		this.queryBuilder = roomSpinner.queryBuilder();
		this.queryBuilder.where().eq(Room.ROOMTYPE_ID, roomid);
		PreparedQuery<Room> preparedQuery = queryBuilder.prepare();
		List<Room> RoomList = roomSpinner.query(preparedQuery);
		for (Room v : RoomList) {

			spinnerRoomList.add(String.valueOf(v.getRoom_id()) + " "
					+ String.valueOf(v.getDescription()));
			Log.v("LOL", String.valueOf(v.getRoom_id()));
		}
		RoomList = null;
		roomSpinner = null;

		return spinnerRoomList;

	}

	public List<Tag> getTagById(String ID) throws SQLException {

		if (this.tagLocationAll == null) {
			this.tagLocationAll = this.getTagDataDao();
		}

		this.queryBuilderTag = tagLocationAll.queryBuilder();
		this.queryBuilderTag.where().eq(Tag.TAG_ID, ID);
		PreparedQuery<Tag> preparedQueryTag = queryBuilderTag.prepare();
		Tagresult = tagLocationAll.query(preparedQueryTag);

		return Tagresult;

	}

	public List<Edges> getEdgesBySource(String sourceId) throws SQLException {

		if (this.edgesAll == null) {
			this.edgesAll = this.getEdgesDataDao();
		}
		this.queryBuilderEdges = edgesAll.queryBuilder();
		this.queryBuilderEdges.where().eq(Edges.SOURCE, sourceId);
		PreparedQuery<Edges> preparedQueryEdges = queryBuilderEdges.prepare();
		edgesResult = edgesAll.query(preparedQueryEdges);

		return edgesResult;
	}

	public List<Edges> getEdgesByDestination(String destinationId)
			throws SQLException {

		this.edgesAll = null;
		this.queryBuilderEdges = null;

		if (this.edgesAll == null) {
			this.edgesAll = this.getEdgesDataDao();
		}
		this.queryBuilderEdges = edgesAll.queryBuilder();
		this.queryBuilderEdges.where().eq(Edges.SOURCE, destinationId);
		PreparedQuery<Edges> preparedQueryEdges = queryBuilderEdges.prepare();
		edgesResultDestination = edgesAll.query(preparedQueryEdges);

		return edgesResultDestination;
	}

	@SuppressWarnings("rawtypes")
	public List<Edges> getRemainingEdges(String startSource,
			String endDestination) throws SQLException {
		this.edgesAll = null;
		this.queryBuilderEdges = null;

		if (this.edgesAll == null) {
			this.edgesAll = this.getEdgesDataDao();
		}
		this.queryBuilderEdges = edgesAll.queryBuilder();
		Where where = queryBuilderEdges.where();
		where.not().like(Edges.SOURCE, startSource).and().not()
				.like(Edges.DESTINATION, endDestination);
		PreparedQuery<Edges> preparedEdgesRemain = queryBuilderEdges.prepare();
		edgeResultRemain = edgesAll.query(preparedEdgesRemain);

		return edgeResultRemain;

	}

	public List<HistoryItem> getHistoryItems() {

		List<HistoryItem> result = new ArrayList<HistoryItem>();
		try {
			PreparedQuery<HistoryItem> preparedQuery = this.getHistoryItemDataDao().queryBuilder().prepare();
			result = this.getHistoryItemDataDao().query(preparedQuery);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return result;
	}
}
