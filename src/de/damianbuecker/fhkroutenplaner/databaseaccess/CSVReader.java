package de.damianbuecker.fhkroutenplaner.databaseaccess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.controller.Controller;

public class CSVReader extends Controller {

	// CsvReader obj = new CsvReader();
	private static final String[] tables = { "edge.csv", "tag.csv", "room.csv",
			"dozent.csv", "raumart.csv" };
	private AssetManager mAssetManager;
	private static final String CSVSPLITBY = ",";
	private Dao<Edges, Integer> edgesDao;
	private Dao<Tag, Integer> tagDao;
	private Dao<Docent, Integer> docentDao;
	private Dao<Room, Integer> roomDao;
	private Dao<Roomtype, Integer> roomtypeDao;
	private SharedPreferences prefs;
	private long startTime, endTime;

	public void readCSV(DatabaseHelper databaseHelper, Context context) {
		
		
		this.startTime = this.getTime();
		for (String tableName : tables) {
			BufferedReader br = null;
			String line = "";
			

			try {

				if (this.mAssetManager == null) {
					mAssetManager = context.getAssets();
				}
				InputStream ims = mAssetManager.open(tableName);

				br = new BufferedReader(new InputStreamReader(ims));

				while ((line = br.readLine()) != null) {

					// use comma as separator
					String[] row = line.split(CSVSPLITBY);

					for (String s : row) {
						Log.d("INFO", "Element: " + s + " ");
					}

					if (tableName.equals("edge.csv")) {
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
					} else if (tableName.equals("tag.csv")) {

						if (this.tagDao == null) {
							this.tagDao = databaseHelper.getTagDataDao();
						}

						if (row.length != 0) {
							Tag v = new Tag();
							v.setTag_id(Integer.parseInt(row[0]));
							v.setRoom_ID(Integer.parseInt(row[1]));
							v.setX_pos(Double.parseDouble(row[2]));
							v.setY_pos(Double.parseDouble(row[3]));
							v.setDescription(row[4]);
							v.setFloor(Integer.parseInt(row[5]));

							this.tagDao.create(v);
						}
					} else if (tableName.equals("room.csv")) {

						if (this.roomDao == null) {
							this.roomDao = databaseHelper.getRoomDataDao();
						}

						if (row.length != 0) {
							Room v = new Room();
							v.setRoom_id(Integer.parseInt(row[0]));
							v.setFloor(Integer.parseInt(row[1]));
							v.setRoomtype_ID(Integer.parseInt(row[2]));
							if (row[3].equals("")) {
								v.setDocent_ID(null);
							} else {
								v.setDocent_ID(Integer.parseInt(row[3]));
							}
							v.setDescription(row[4]);

							this.roomDao.create(v);
						}

					} else if (tableName.equals("dozent.csv")) {

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

					} else if (tableName.equals("raumart.csv")) {

						if (this.roomtypeDao == null) {
							this.roomtypeDao = databaseHelper
									.getRoomtypeDataDao();
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
		
		this.endTime = this.getTime();
		this.log(this.getRuntime(startTime, endTime));

	}

}
