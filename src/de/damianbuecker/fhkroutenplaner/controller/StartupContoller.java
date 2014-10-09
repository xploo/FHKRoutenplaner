package de.damianbuecker.fhkroutenplaner.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import de.damianbuecker.fhkroutenplaner.databaseaccess.JSONParser;

/**
 * The Class StartupContoller.
 */
public class StartupContoller extends Controller {

	/** The conn manager. */
	private ConnectivityManager connManager;

	/** The m wifi. */
	private NetworkInfo mWifi;

	/** The Constant tables. */
	private static final String[] tables = { "edge", "tag", "room", "dozent", "raumart" };

	/** The j parser. */
	private JSONParser jParser = new JSONParser();

	/** The external data. */
	private JSONArray externalData;

	/** The params. */
	private List<NameValuePair> params;

	/** The version. */
	private String version;

	/** The m shared preferences controller. */
	private SharedPreferencesController mSharedPreferencesController;

	/** The url_get database. */
	private String url_getDatabase = "http://fhkrp.in24.de/BA/index.php";

	/** The url_get version. */
	private String url_getVersion = "http://fhkrp.in24.de/BA/index2.php";

	/** The Constant TAG_SUCCESS. */
	private static final String TAG_SUCCESS = "success";

	/** The Constant TAG_DOCENTID. */
	protected static final String TAG_DOCENTID = "docentID";

	/** The Constant TAG_ROOMID. */
	protected static final String TAG_ROOMID = "roomID";

	/** The Constant TAG_FLOOR. */
	protected static final String TAG_FLOOR = "floor";

	/** The Constant TAG_ROOMTYPEID. */
	protected static final String TAG_ROOMTYPEID = "roomtypeID";

	/** The Constant TAG_DESCRIPTION. */
	protected static final String TAG_DESCRIPTION = "description";

	/** The Constant TAG_TAGID. */
	protected static final String TAG_TAGID = "tagID";

	/** The Constant TAG_XOS. */
	protected static final String TAG_XOS = "x_pos";

	/** The Constant TAG_YPOS. */
	protected static final String TAG_YPOS = "y_pos";

	/** The Constant TAG_NAME. */
	protected static final String TAG_NAME = "name";

	/** The Constant TAG_LASTNAME. */
	protected static final String TAG_LASTNAME = "lastname";

	/** The Constant TAG_EDGESID. */
	protected static final String TAG_EDGESID = "edgesID";

	/** The Constant TAG_SOURCE. */
	protected static final String TAG_SOURCE = "source";

	/** The Constant TAG_DESTINATION. */
	protected static final String TAG_DESTINATION = "destination";

	/** The Constant TAG_COST. */
	protected static final String TAG_COST = "cost";

	/** The Constant TAG_VERSION. */
	protected static final String TAG_VERSION = "version";

	/** The Constant ROOM_TXT. */
	private static final String ROOM_TXT = "/sdcard/FMS/room.txt";

	/** The Constant TAG_TXT. */
	private static final String TAG_TXT = "/sdcard/FMS/tag.txt";

	/** The Constant ROOMTYPE_TXT. */
	private static final String ROOMTYPE_TXT = "/sdcard/FMS/roomtype.txt";

	/** The Constant DOCENT_TXT. */
	private static final String DOCENT_TXT = "/sdcard/FMS/docent.txt";

	/** The Constant EDGES_TXT. */
	private static final String EDGES_TXT = "/sdcard/FMS/edges.txt";

	/**
	 * Instantiates a new startup contoller.
	 * 
	 * @param context
	 *            the context
	 */
	public StartupContoller(Context context) {
		super(context);
	}

	/**
	 * Checks if is wifi connected.
	 * 
	 * @return true, if is wifi connected
	 */
	@SuppressWarnings("static-access")
	public boolean isWifiConnected() {

		this.connManager = (ConnectivityManager) this.getContext().getSystemService(this.getContext().CONNECTIVITY_SERVICE);
		this.mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return (mWifi.isConnected()) ? true : false;
	}

	/**
	 * Gets the external database.
	 * 
	 * @return the external database
	 */
	public void getExternalDatabase() {

		new Thread(new Runnable() {

			public void run() {

				for (String tblnames : tables) {

					// Übergabeparameter festlegen für http-Request
					params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("tablename", tblnames));
					try {
						// JSON objekte per http-Request holen
						JSONObject json = jParser.makeHttpRequest(url_getDatabase, "GET", params);

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
									JSONObject c = externalData.getJSONObject(i);

									if (tblnames.equals("room")) {

										// 1. Tabelle auf Server mit (Name |
										// hashwert)
										// 2. Lokalen hashwert mit Server
										// Hashwert vergleichen
										// 3. Bei gefundener änderung ->

										try {
											File myFile = new File(ROOM_TXT);
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
											myOutWriter.append(c.getString(TAG_ROOMID) + ";");
											myOutWriter.append(c.getString(TAG_FLOOR) + ";");
											myOutWriter.append(c.getString(TAG_ROOMTYPEID) + ";");
											myOutWriter.append(c.getString(TAG_DOCENTID) + ";");
											myOutWriter.append(c.getString(TAG_DESCRIPTION) + "\n");
											myOutWriter.close();
											fOut.close();

										} catch (Exception e) {
											e.printStackTrace();
										}

									} else if (tblnames.equals("tag")) {

										try {
											File myFile = new File(TAG_TXT);
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
											myOutWriter.append(c.getString(TAG_TAGID) + ";");
											myOutWriter.append(c.getString(TAG_ROOMID) + ";");
											myOutWriter.append(c.getString(TAG_XOS) + ";");
											myOutWriter.append(c.getString(TAG_YPOS) + ";");
											myOutWriter.append(c.getString(TAG_DESCRIPTION) + ";");
											myOutWriter.append(c.getString(TAG_FLOOR) + "\n");
											myOutWriter.close();
											fOut.close();

										} catch (Exception e) {
											e.printStackTrace();
										}

									} else if (tblnames.equals("roomtype")) {

										try {
											File myFile = new File(ROOMTYPE_TXT);
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
											myOutWriter.append(c.getString(TAG_ROOMTYPEID) + ";");
											myOutWriter.append(c.getString(TAG_DESCRIPTION) + "\n");
											myOutWriter.close();
											fOut.close();

										} catch (Exception e) {
											e.printStackTrace();
										}

									} else if (tblnames.equals("docent")) {
										try {
											File myFile = new File(DOCENT_TXT);
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
											myOutWriter.append(c.getString(TAG_DOCENTID) + ";");
											myOutWriter.append(c.getString(TAG_NAME) + ";");
											myOutWriter.append(c.getString(TAG_LASTNAME) + "\n");
											myOutWriter.close();
											fOut.close();

										} catch (Exception e) {
											e.printStackTrace();
										}

									} else if (tblnames.equals("edges")) {

										try {
											File myFile = new File(EDGES_TXT);
											myFile.createNewFile();
											FileOutputStream fOut = new FileOutputStream(myFile);
											OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
											myOutWriter.append(c.getString(TAG_EDGESID) + ";");
											myOutWriter.append(c.getString(TAG_SOURCE) + ";");
											myOutWriter.append(c.getString(TAG_DESTINATION) + ";");
											myOutWriter.append(c.getString(TAG_COST) + "\n");
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

	/**
	 * Gets the database version.
	 * 
	 * @return the database version
	 */
	public String getDatabaseVersion() {

		new Thread(new Runnable() {

			public void run() {

				params = new ArrayList<NameValuePair>();

				try {
					// JSON objekte per http-Request holen
					JSONObject json = jParser.makeHttpRequest(url_getVersion, "GET", params);

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
							Log.v("VERSIONSTEST", version);

						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}).start();

		return version;

	}

	/**
	 * Check for update.
	 * 
	 * @return the integer
	 */
	public Integer checkForUpdate() {

		this.mSharedPreferencesController = new SharedPreferencesController(this.getContext());
		String externalVersion = getDatabaseVersion();
		Integer internalVersion = this.mSharedPreferencesController.getInteger(SHARED_PREFERENCE_DATABASE_VERSION);

		Log.v("SharedPrefCHECK", internalVersion.toString());
		this.logInfo("SharedPrefCheck " + internalVersion.toString());

		if (externalVersion != "0" || internalVersion != 0) {
			if (Integer.parseInt(externalVersion) == internalVersion) {

				// No Update available
				return 1;
			} else if (Integer.parseInt(externalVersion) > internalVersion) {
				// Update
				return 2;
			} else {
				// error
				return 3;
			}
		} else {

			// error
			return 3;
		}
	}

}
