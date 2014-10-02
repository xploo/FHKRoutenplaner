package de.damianbuecker.fhkroutenplaner.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.controller.StartupContoller;
import de.damianbuecker.fhkroutenplaner.databaseaccess.CSVReader;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;

public class StartUpActivity extends ModifiedActivity {

	private boolean wifistate;
	private CSVReader obj;
	private StartupContoller srtcon;
	private DatabaseHelper databaseHelper;
	private SharedPreferences prefs;
	private Integer UpdateResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_activity);

		prefs = getSharedPreferences("de.damianbuecker.fhkroutenplaner",
				MODE_PRIVATE);
		prefs.edit().putBoolean("RouteRunning", false).commit();
		// Dateien kopieren(assets -> Intern)
/*
		this.srtcon = new StartupContoller(this);
		if (this.wifistate = srtcon.isWifiConnected()
				&& srtcon.checkForUpdate() == 2) {
			Log.v("WIFI CONNECTED", "YES");
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Database update is available!");

			alertDialogBuilder
					.setMessage("Install update?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// Update Database

									srtcon.getExternalDatabase();

								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
		}
*/
		if (this.databaseHelper == null) {
			this.databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);
		}
		// if(!FirstStart)
		if (prefs.getBoolean("firstrun", true)) {
			this.obj = new CSVReader();
			this.obj.readCSV(databaseHelper, this);

			prefs.edit().putInt("databaseVersion", 1).commit();
		}
	}

	public void onClick_search(View v) {

		Intent intent = new Intent("android.intents.NFCConnector");
		startActivity(intent);
	}

	public void onClick_history(View v) {

		Intent intent = new Intent("android.intents.History");
		startActivity(intent);
	}

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
