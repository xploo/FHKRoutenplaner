package de.damianbuecker.fhkroutenplaner.activity;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.controller.CsvController;
import de.damianbuecker.fhkroutenplaner.controller.SharedPreferencesController;
import de.damianbuecker.fhkroutenplaner.controller.StartupContoller;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

/**
 * The Class StartUpActivity.
 */
@ContentView(R.layout.startup_activity)
public class StartUpActivity extends ModifiedViewActivityImpl {

	/** The m csv controller. */
	private CsvController mCsvController;

	/** The database helper. */
	private DatabaseHelper databaseHelper;

	/** The m shared preferences controller. */
	private SharedPreferencesController mSharedPreferencesController;

	/** The Constant INTENT_EXTRA_HISTORY. */
	private static final String INTENT_EXTRA_HISTORY = "history";

	private StartupContoller mStartupController;

	public static final int progress_bar_type = 0;

	private ProgressDialog prgDialog;

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (this.mSharedPreferencesController == null) {
			this.mSharedPreferencesController = new SharedPreferencesController(this);
		}
		this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_ROUTE_RUNNING, false);

		if (this.databaseHelper == null) {
			this.databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}

		this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_FIRST_RUN, true);

		/*
		 * // Testdaten HistoryItem h1 = new HistoryItem(); h1.setName("H1");
		 * h1.setDate(System.currentTimeMillis()); h1.setDestination("H1");
		 * h1.setStart("H1"); h1.setTimestamp(System.currentTimeMillis());
		 * HistoryItem h2 = new HistoryItem(); h2.setName("H2");
		 * h2.setDate(System.currentTimeMillis()); h2.setDestination("H2");
		 * h2.setStart("H2"); h2.setTimestamp(System.currentTimeMillis());
		 * HistoryItem h3 = new HistoryItem(); h3.setName("H3");
		 * h3.setDate(System.currentTimeMillis()); h3.setDestination("H3");
		 * h3.setStart("H3"); h3.setTimestamp(System.currentTimeMillis());
		 * 
		 * h1.writeToDatabase(this); h2.writeToDatabase(this);
		 * h3.writeToDatabase(this); // Testdaten Ende
		 */

		/**
		 * Abfrage ob Updateverfügbar und WIfi-Connected
		 */
		if (this.mStartupController == null) {

			mStartupController = new StartupContoller(this);
		}

		if(this.mStartupController.isWifiConnected() == true) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle("Datenbankupdate verfügbar!");
			alertDialogBuilder
					.setMessage(
							"Möchten Sie das Update herrunterladen und installieren?")
					.setCancelable(false)
					.setPositiveButton("Ja",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
//									new getExternalDatabase()
								}
							})
					.setNegativeButton("Nein",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}

		
		this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_FIRST_RUN, true);
		if (this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_FIRST_RUN)) {
			this.mCsvController = new CsvController(this);
			this.mCsvController.readCSV(databaseHelper);

			this.mSharedPreferencesController.putInSharedPreference(
					SHARED_PREFERENCE_DATABASE_VERSION, 1);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			prgDialog = new ProgressDialog(this);
			prgDialog.setMessage("Downloading Mp3 file. Please wait...");
			prgDialog.setIndeterminate(false);
			prgDialog.setMax(100);
			prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			prgDialog.setCancelable(false);
			prgDialog.show();
			return prgDialog;
		default:
			return null;
		}
	}
	
	
class getExternalDatabase extends AsyncTask<String,String,String>{
		
		@SuppressWarnings("deprecation")
		@Override		
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            showDialog(progress_bar_type);
        }
		
		protected String doInBackground(String... f_url) {
			
			mStartupController.getExternalDatabase();
			
			return null;
		}		
			
		
		 protected void onProgressUpdate(String... progress) {
			 
			 prgDialog.setProgress(Integer.parseInt(progress[0]));
			 
		 }
		 
		 @SuppressWarnings("deprecation")
		protected void onPostExecute(String file_url){
			 dismissDialog(progress_bar_type);
		 }
	}

	/**
	 * On click_search.
	 * 
	 * @param v
	 *            the v
	 */
	public void onClick_search(View v) {

		Intent intent = new Intent(INTENT_NAVIGATION);
		startActivity(intent);
	}

	/**
	 * On click_history.
	 * 
	 * @param v
	 *            the v
	 */
	public void onClick_history(View v) {

		if (this.databaseHelper == null) {
			this.databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		List<HistoryItem> listHistoryItems = new ArrayList<HistoryItem>();
		listHistoryItems = this.databaseHelper.getHistoryItems();
		ArrayList<String> jsonList = new ArrayList<String>();
		for (HistoryItem h : listHistoryItems) {
			jsonList.add(h.toJson(h));
		}

		Intent intent = new Intent(INTENT_HISTORY);
		intent.putStringArrayListExtra(INTENT_EXTRA_HISTORY, jsonList);
		startActivity(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (this.mSharedPreferencesController == null) {
			this.mSharedPreferencesController = new SharedPreferencesController(this);
		}
		if (this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_FIRST_RUN)) {
			this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_FIRST_RUN, false);
		}
	}

}
