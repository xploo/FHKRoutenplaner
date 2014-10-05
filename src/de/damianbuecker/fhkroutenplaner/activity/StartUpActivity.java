package de.damianbuecker.fhkroutenplaner.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.controller.CsvController;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;

/**
 * The Class StartUpActivity.
 */
public class StartUpActivity extends ModifiedViewActivityImpl {

	/** The obj. */
	private CsvController obj;

	/** The database helper. */
	private DatabaseHelper databaseHelper;

	/** The prefs. */
	private SharedPreferences prefs;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_activity);

		prefs = getSharedPreferences("de.damianbuecker.fhkroutenplaner", MODE_PRIVATE);
		prefs.edit().putBoolean("RouteRunning", false).commit();
		// Dateien kopieren(assets -> Intern)
		/*
		 * this.srtcon = new StartupContoller(this); if (this.wifistate =
		 * srtcon.isWifiConnected() && srtcon.checkForUpdate() == 2) {
		 * Log.v("WIFI CONNECTED", "YES"); AlertDialog.Builder
		 * alertDialogBuilder = new AlertDialog.Builder( this);
		 * alertDialogBuilder.setTitle("Database update is available!");
		 * 
		 * alertDialogBuilder .setMessage("Install update?")
		 * .setCancelable(false) .setPositiveButton("Yes", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { // Update Database
		 * 
		 * srtcon.getExternalDatabase();
		 * 
		 * } }) .setNegativeButton("No", new DialogInterface.OnClickListener() {
		 * public void onClick(DialogInterface dialog, int id) {
		 * dialog.cancel(); } }); AlertDialog alertDialog =
		 * alertDialogBuilder.create(); // show it alertDialog.show(); }
		 */
		if (this.databaseHelper == null) {
			this.databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		// if(!FirstStart)
		if (prefs.getBoolean("firstrun", true)) {
			this.obj = new CsvController(this);
			this.obj.readCSV(databaseHelper);

			prefs.edit().putInt("databaseVersion", 1).commit();
		}
	}

	/**
	 * On click_search.
	 * 
	 * @param v
	 *            the v
	 */
	public void onClick_search(View v) {

		Intent intent = new Intent("android.intents.NFCConnector");
		startActivity(intent);
	}

	/**
	 * On click_history.
	 * 
	 * @param v
	 *            the v
	 */
	public void onClick_history(View v) {

		/*
		 * Aus DB holen
		 */
		ArrayList<String> test = new ArrayList<String>();
		test.add("BoobA");
		test.add("BoobB");
		test.add("BoobC");

		Intent intent = new Intent("android.intents.History");
		intent.putExtra("history", test);
		startActivity(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (prefs.getBoolean("firstrun", true)) {
			// Do first run stuff here then set 'firstrun' as false
			// using the following line to edit/commit prefs
			prefs.edit().putBoolean("firstrun", false).commit();
		}
	}

}
