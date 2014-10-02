package de.damianbuecker.fhkroutenplaner.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.databaseaccess.JSONParser;


public class StartupContoller extends Controller {

	private ConnectivityManager connManager;
	private NetworkInfo mWifi;
	private static final String[] tables = { "edge", "tag", "room", "dozent",
			"raumart" };
	private JSONParser jParser = new JSONParser();
	private JSONArray externalData;
	private AssetManager mAssetManager;
	private List<NameValuePair> params;
	private String version;
	private SharedPreferences prefs;
	

	private String url_getDatabase = "http://fhkrp.in24.de/BA/index.php";
	private String url_getVersion = "http://fhkrp.in24.de/BA/index2.php";
	private static final String TAG_SUCCESS = "success";
	protected static final String TAG_DOCENTID = "docentID";
	protected static final String TAG_ROOMID = "roomID";
	protected static final String TAG_FLOOR = "floor";
	protected static final String TAG_ROOMTYPEID = "roomtypeID";
	protected static final String TAG_DESCRIPTION = "description";
	protected static final String TAG_TAGID = "tagID";
	protected static final String TAG_XOS = "x_pos";
	protected static final String TAG_YPOS = "y_pos";
	protected static final String TAG_NAME = "name";
	protected static final String TAG_LASTNAME = "lastname";
	protected static final String TAG_EDGESID = "edgesID";
	protected static final String TAG_SOURCE = "source";
	protected static final String TAG_DESTINATION = "destination";
	protected static final String TAG_COST = "cost";
	protected static final String TAG_VERSION = "version";

	public StartupContoller(Context context) {
		super(context);
	}

	public boolean isWifiConnected() {

		this.connManager = (ConnectivityManager) this.getContext()
				.getSystemService(this.getContext().CONNECTIVITY_SERVICE);
		this.mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return (mWifi.isConnected()) ? true : false;
	}

	public void getExternalDatabase() {

		new Thread(new Runnable() {

			public void run() {

				for (String tblnames : tables) {

					// Übergabeparameter festlegen für http-Request
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("tablename", tblnames));
					try {
						// JSON objekte per http-Request holen
						JSONObject json = jParser.makeHttpRequest(
								url_getDatabase, "GET", params);

						// Ausgabe der geholten Daten in der Log cat
						Log.d("Kompletter Inhalt extern: ", json.toString());

						try {
							// Überprüfen ob HTTP-Request erfolgreich
							int success = json.getInt(TAG_SUCCESS);

							if (success == 1) {
								// Abfrage erfolgreich.
								// Abgefragte Daten in einem JSON-Array
								// speichern

								externalData = json.getJSONArray(tblnames);

								// Per Schleife durch alle Klausuren
								for (int i = 0; i < externalData.length(); i++) {
									JSONObject c = externalData
											.getJSONObject(i);

									if (tblnames.equals("room")) {

										// 1. Tabelle auf Server mit (Name |
										// hashwert)
										// 2. Lokalen hashwert mit Server
										// Hashwert vergleichen
										// 3. Bei gefundener änderung ->

										try {
											File myFile = new File(
													"/sdcard/FMS/room.txt");
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(
													myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(
													fOut);
											myOutWriter.append(c
													.getString(TAG_ROOMID)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_FLOOR) + ";");
											myOutWriter.append(c
													.getString(TAG_ROOMTYPEID)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_DOCENTID)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_DESCRIPTION)
													+ "\n");
											myOutWriter.close();
											fOut.close();

										} catch (Exception e) {
											e.printStackTrace();
										}

									} else if (tblnames.equals("tag")) {

										try {
											File myFile = new File(
													"/sdcard/FMS/tag.txt");
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(
													myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(
													fOut);
											myOutWriter.append(c
													.getString(TAG_TAGID) + ";");
											myOutWriter.append(c
													.getString(TAG_ROOMID)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_XOS) + ";");
											myOutWriter.append(c
													.getString(TAG_YPOS) + ";");
											myOutWriter.append(c
													.getString(TAG_DESCRIPTION)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_FLOOR)
													+ "\n");
											myOutWriter.close();
											fOut.close();

										} catch (Exception e) {
											e.printStackTrace();
										}

									} else if (tblnames.equals("roomtype")) {

										try {
											File myFile = new File(
													"/sdcard/FMS/roomtype.txt");
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(
													myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(
													fOut);
											myOutWriter.append(c
													.getString(TAG_ROOMTYPEID)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_DESCRIPTION)
													+ "\n");
											myOutWriter.close();
											fOut.close();

										} catch (Exception e) {
											e.printStackTrace();
										}

									} else if (tblnames.equals("docent")) {
										try {
											File myFile = new File(
													"/sdcard/FMS/docent.txt");
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(
													myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(
													fOut);
											myOutWriter.append(c
													.getString(TAG_DOCENTID)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_NAME) + ";");
											myOutWriter.append(c
													.getString(TAG_LASTNAME)
													+ "\n");
											myOutWriter.close();
											fOut.close();

										} catch (Exception e) {
											e.printStackTrace();
										}

									} else if (tblnames.equals("edges")) {

										try {
											File myFile = new File(
													"/sdcard/FMS/edges.txt");
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(
													myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(
													fOut);
											myOutWriter.append(c
													.getString(TAG_EDGESID)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_SOURCE)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_DESTINATION)
													+ ";");
											myOutWriter.append(c
													.getString(TAG_COST) + "\n");
											myOutWriter.close();
											fOut.close();

										} catch (Exception e) {
											e.printStackTrace();
										}

									}

								}
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} catch (Exception ex) {
					}
				}

			}
		}).start();

	}

	public String getDatabaseVersion() {

		new Thread(new Runnable() {

			public void run() {

				params = new ArrayList<NameValuePair>();

				try {
					// JSON objekte per http-Request holen
					JSONObject json = jParser.makeHttpRequest(url_getVersion,
							"GET", params);

					// Ausgabe der geholten Daten in der Log cat
					Log.d("Kompletter Inhalt extern: ", json.toString());

					// Überprüfen ob HTTP-Request erfolgreich
					int success = json.getInt(TAG_SUCCESS);

					if (success == 1) {
						// Abfrage erfolgreich.
						// Abgefragte Daten in einem JSON-Array
						// speichern
						externalData = json.getJSONArray("version");

						// Per Schleife durch alle Klausuren
						for (int i = 0; i < externalData.length(); i++) {
							JSONObject c = externalData.getJSONObject(i);

							version = c.getString(TAG_VERSION);
							Log.v("VERSIONSTEST",version);
							
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}).start();
		
		return version;
		
	}

	public Integer checkForUpdate() {
		
		this.prefs = this.getContext().getSharedPreferences(
			      "de.damianbuecker.fhkroutenplaner", this.getContext().MODE_PRIVATE);
		
		String externalVersion = "0";
		Integer internalVersion = 0;	
		
		externalVersion = getDatabaseVersion();
		internalVersion = prefs.getInt("databaseVersion", 0);
		
		Log.v("SharedPrefCHECK",internalVersion.toString());
		
		
		if(externalVersion != "0" || internalVersion != 0)
		{
		if(Integer.parseInt(externalVersion) == internalVersion){
			
			//No Update available
			return 1;
		}else if(Integer.parseInt(externalVersion) > internalVersion){
			//Update			
			return 2;
		}else{
			//error
			return 3;
		}
		}else{
			
			//error
			return 3;
		}
	}	

	
}
