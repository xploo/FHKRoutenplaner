package de.damianbuecker.fhkroutenplaner.activity;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
	
	/** The btn start search. */
	@InjectView(R.id.btn_startup_search)
	Button btnStartSearch;
	
	/** The btn open history. */
	@InjectView(R.id.btn_startup_history)
	Button btnOpenHistory;

	/** The m startup controller. */
	private StartupContoller mStartupController;

	/** The Constant progress_bar_type. */
	public static final int progress_bar_type = 0;

	/** The prg dialog. */
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
		this.btnStartSearch.setOnClickListener(new ButtonListener());
		this.btnOpenHistory.setOnClickListener(new ButtonListener());

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
//									new getExternalDatabase()
								}
							})
					.setNegativeButton("Nein",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

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

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
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
	
	
/**
 * The Class getExternalDatabase.
 */
class getExternalDatabase extends AsyncTask<String,String,String>{
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@SuppressWarnings("deprecation")
		@Override		
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            showDialog(progress_bar_type);
        }
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected String doInBackground(String... f_url) {
			
			mStartupController.getExternalDatabase();
			
			return null;
		}		
			
		
		 /* (non-Javadoc)
 		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
 		 */
 		protected void onProgressUpdate(String... progress) {
			 
			 prgDialog.setProgress(Integer.parseInt(progress[0]));
			 
		 }
		 
		 /* (non-Javadoc)
 		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
 		 */
 		@SuppressWarnings("deprecation")
		protected void onPostExecute(String file_url){
			 dismissDialog(progress_bar_type);
		 }
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

	/**
	 * The listener interface for receiving button events.
	 * The class that is interested in processing a button
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addButtonListener<code> method. When
	 * the button event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ButtonEvent
	 */
	private class ButtonListener implements View.OnClickListener{

		/** The database helper. */
		private DatabaseHelper databaseHelper;
		
		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.btn_startup_search) {
				Intent intent = new Intent(v.getContext(),NavigationActivity.class);
				startActivity(intent);
			} else if(v.getId() == R.id.btn_startup_history) {
				if (this.databaseHelper == null) {
					this.databaseHelper = OpenHelperManager.getHelper(v.getContext(), DatabaseHelper.class);
				}
				List<HistoryItem> listHistoryItems = new ArrayList<HistoryItem>();
				listHistoryItems = this.databaseHelper.getHistoryItems();
				ArrayList<String> jsonList = new ArrayList<String>();
				for (HistoryItem h : listHistoryItems) {
					jsonList.add(h.toJson(h));
				}

				Intent intent = new Intent(v.getContext(), HistoryActivity.class);
				intent.putStringArrayListExtra(INTENT_EXTRA_HISTORY, jsonList);
				startActivity(intent);
			}
		}
	}
}
