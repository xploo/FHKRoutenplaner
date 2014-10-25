package de.damianbuecker.fhkroutenplaner.activity;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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

// TODO: Auto-generated Javadoc
/**
 * The Class StartUpActivity.
 */
@ContentView(R.layout.startup_activity)
public class StartUpActivity extends ModifiedViewActivityImpl {

	/** The local csv controller. */
	private CsvController mCsvController;

	/** The database helper. */
	private DatabaseHelper databaseHelper;

	/** The local shared preferences controller. */
	private SharedPreferencesController mSharedPreferencesController;

	/** The Constant INTENT_EXTRA_HISTORY. */
	private static final String INTENT_EXTRA_HISTORY = "history";

	/** The button start search. */
	@InjectView(R.id.btn_startup_search)
	private Button btnStartSearch;

	/** The button open history. */
	@InjectView(R.id.btn_startup_history)
	private Button btnOpenHistory;

	/** The local startup controller. */
	private StartupContoller mStartupController;

	/** The Constant progress_bar_type. */
	public static final int PROGRESS_BAR_TYPE = 0;

	/** The progress dialog. */
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

		/**
		 * Abfrage ob Updateverfügbar und WIfi-Connected
		 */
		if (this.mStartupController == null) {

			this.mStartupController = new StartupContoller(this);
		}
		this.mStartupController.deleteOldImages();
		
		this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_FIRST_RUN, true);
		if (this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_FIRST_RUN)) {
			this.mCsvController = new CsvController(this);
			this.mCsvController.readCSV(databaseHelper);

			this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_DATABASE_VERSION, 1);
		}

		// new CheckForUpdate(this).execute("");
	}

	/**
	 * The Class CheckForUpdate gets the MySQL databaseversion
	 * 
	 */
	private class CheckForUpdate extends AsyncTask<String, String, String> {

		/** The context. */
		private Context context;

		/**
		 * Instantiates a new check for update.
		 *
		 * @param context the context
		 */
		public CheckForUpdate(Context context) {
			this.context = context;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(String... args) {
			mStartupController.getDatabaseVersion();
			return null;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String values) {

			if (mStartupController.isWifiConnected() == true && mStartupController.checkForUpdate() == 2) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setTitle(DATABASE_UPDATE_AVAILABLE);
				alertDialogBuilder.setMessage(DOWNLOAD_INSTALL_UPDATE_QUESTION).setCancelable(false)
						.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								new getExternalDatabase().execute((String) null);
							}
						}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								dialog.cancel();
							}
						});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PROGRESS_BAR_TYPE:
			prgDialog = new ProgressDialog(this);
			prgDialog.setMessage(DOWNLOADING_DATABASE_ENTRIES);
			prgDialog.setIndeterminate(true);
			prgDialog.setCancelable(false);
			prgDialog.show();
			return prgDialog;
		default:
			return null;
		}
	}

	/**
	 * The Class getExternalDatabase gets the external database items.
	 * 
	 */
	@SuppressWarnings("deprecation")
	class getExternalDatabase extends AsyncTask<String, String, String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(PROGRESS_BAR_TYPE);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		protected String doInBackground(String... f_url) {

			mStartupController.getExternalDatabase();

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		protected void onProgressUpdate(String... progress) {

			// Evtl überflüssig
			prgDialog.setProgress(Integer.parseInt(progress[0]));

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(String file_url) {
			dismissDialog(PROGRESS_BAR_TYPE);
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
	 * The listener interface for receiving button events. The class that is
	 * interested in processing a button event implements this interface, and
	 * the object created with that class is registered with a component using
	 * the component's <code>addButtonListener<code> method. When
	 * the button event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see ButtonEvent
	 */
	private class ButtonListener implements View.OnClickListener {

		/** The database helper. */
		private DatabaseHelper databaseHelper;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_startup_search) {
				Intent intent = new Intent(v.getContext(), NavigationActivity.class);
				startActivity(intent);
			} else if (v.getId() == R.id.btn_startup_history) {
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
