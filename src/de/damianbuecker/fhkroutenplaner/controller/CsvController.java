package de.damianbuecker.fhkroutenplaner.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import android.content.Context;
import android.content.res.AssetManager;

import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Docent;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Edges;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Room;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Roomtype;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;

/**
 * The Class CSVReader.
 */
public class CsvController extends Controller {

	/** The Constant tables. */
	private static final String[] tables = { "edge.csv", "tag.csv", "room.csv", "dozent.csv", "raumart.csv" };

	/** The m asset manager. */
	private AssetManager mAssetManager;

	/** The Constant CSVSPLITBY. */
	private static final String CSVSPLITBY = ",";

	/** The Constant EDGE_CSV. */
	private static final String EDGE_CSV = "edge.csv";

	/** The Constant TAG_CSV. */
	private static final String TAG_CSV = "tag.csv";

	/** The Constant ROOM_CSV. */
	private static final String ROOM_CSV = "room.csv";

	/** The Constant DOZENT_CSV. */
	private static final String DOZENT_CSV = "dozent.csv";

	/** The Constant RAUMART_CSV. */
	private static final String RAUMART_CSV = "raumart.csv";

	/** The edges dao. */
	private Dao<Edges, Integer> edgesDao;

	/** The tag dao. */
	private Dao<Tag, Integer> tagDao;

	/** The docent dao. */
	private Dao<Docent, Integer> docentDao;

	/** The room dao. */
	private Dao<Room, Integer> roomDao;

	/** The roomtype dao. */
	private Dao<Roomtype, Integer> roomtypeDao;

	/** The end time. */
	private long startTime, endTime;

	/**
	 * Instantiates a new CSV reader.
	 * 
	 * @param context
	 *            the context
	 */
	public CsvController(Context context) {
		super(context);
	}

	/**
	 * Read csv.
	 * 
	 * @param databaseHelper
	 *            the database helper
	 */
	public void readCSV(DatabaseHelper databaseHelper) {

		this.startTime = DateTimeController.getTime();
		for (String tableName : tables) {
			BufferedReader br = null;
			String line = "";
			int i = 1000;

			try {

				if (this.mAssetManager == null) {
					mAssetManager = this.getContext().getAssets();
				}
				InputStream ims = mAssetManager.open(tableName);

				br = new BufferedReader(new InputStreamReader(ims));

				while ((line = br.readLine()) != null) {

					// use comma as separator
					String[] row = line.split(CSVSPLITBY);

					for (String s : row) {
						this.logMessage("INFO", "Row elements: " + s + " \n");
					}

					if (tableName.equals(EDGE_CSV)) {
						if (this.edgesDao == null) {
							this.edgesDao = databaseHelper.getEdgesDataDao();
						}

						if (row.length != 0) {
							Edges v = new Edges();
							v.setKante_id(Integer.parseInt(row[0]));
							v.setSource(Integer.parseInt(row[1]));
							v.setDestination(Integer.parseInt(row[2]));
							v.setCost(Integer.parseInt(row[3]));

							this.edgesDao.create(v);
						}
					} else if (tableName.equals(TAG_CSV)) {

						if (this.tagDao == null) {
							this.tagDao = databaseHelper.getTagDataDao();
						}

						if (row.length != 0) {
							Tag v = new Tag();
							v.setTag_id(Integer.parseInt(row[0]));
							if (row[1].equals("")) {
								v.setRoom_ID(null);
							} else {
								v.setRoom_ID(Integer.parseInt(row[1]));
							}
							v.setX_pos(Double.parseDouble(row[2]));
							v.setY_pos(Double.parseDouble(row[3]));
							v.setDescription(row[4]);
							v.setFloor(Integer.parseInt(row[5]));

							this.tagDao.create(v);
						}
					} else if (tableName.equals(ROOM_CSV)) {

						if (this.roomDao == null) {
							this.roomDao = databaseHelper.getRoomDataDao();
						}
						if (row.length != 0) {
							Room v = new Room();
							v.setRoom_id(Integer.parseInt(row[0]));
							v.setFloor(Integer.parseInt(row[1]));
							v.setRoomtype_ID(Integer.parseInt(row[2]));
							if (row[3].equals("")) {
								v.setDocent_ID(i);
							} else {
								v.setDocent_ID(Integer.parseInt(row[3]));
							}
							v.setDescription(row[4]);

							this.roomDao.create(v);
							i++;
						}

					} else if (tableName.equals(DOZENT_CSV)) {

						if (this.docentDao == null) {
							this.docentDao = databaseHelper.getDocentDataDao();
						}
						if (row.length != 0) {
							Docent v = new Docent();
							v.setDozent_id(Integer.parseInt(row[0]));
							v.setD_name(row[1]);
							v.setD_lastname(row[2]);

							this.docentDao.create(v);
						}
					} else if (tableName.equals(RAUMART_CSV)) {

						if (this.roomtypeDao == null) {
							this.roomtypeDao = databaseHelper.getRoomtypeDataDao();
						}
						if (row.length != 0) {
							Roomtype v = new Roomtype();
							v.setRoomtype_id(Integer.parseInt(row[0]));
							v.setDescription(row[1]);

							this.roomtypeDao.create(v);
						}
					}
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		this.endTime = DateTimeController.getTime();
		this.logMessage("INFO", DateTimeController.getRuntime(startTime, endTime));
	}
}
