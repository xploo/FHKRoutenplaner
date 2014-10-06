package de.damianbuecker.fhkroutenplaner.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.controller.CsvController;
import de.damianbuecker.fhkroutenplaner.controller.SharedPreferencesController;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

/**
 * The Class StartUpActivity.
 */
public class StartUpActivity extends ModifiedViewActivityImpl {

	private CsvController mCsvController;

	/** The database helper. */
	private DatabaseHelper databaseHelper;

	private SharedPreferencesController mSharedPreferencesController;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_activity);
	
		if(this.mSharedPreferencesController == null) {
			this.mSharedPreferencesController = new SharedPreferencesController(this);
		}
		this.mSharedPreferencesController.putInSharedPreference("RouteRunning", false);
		
		if (this.databaseHelper == null) {
			this.databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}

		this.mSharedPreferencesController.putInSharedPreference("firstrun", true);
		HistoryItem h1 = new HistoryItem();
		h1.setName("H1");h1.setDate(System.currentTimeMillis());h1.setDestination("H1");h1.setStart("H1");h1.setTimestamp(System.currentTimeMillis());
		HistoryItem h2 = new HistoryItem();
		h2.setName("H2");h2.setDate(System.currentTimeMillis());h2.setDestination("H2");h2.setStart("H2");h2.setTimestamp(System.currentTimeMillis());
		HistoryItem h3 = new HistoryItem();
		h3.setName("H3");h3.setDate(System.currentTimeMillis());h3.setDestination("H3");h3.setStart("H3");h3.setTimestamp(System.currentTimeMillis());
		
		h1.create(this);
		h2.create(this);
		h3.create(this);
		
		this.mSharedPreferencesController.putInSharedPreference("firstrun", true);
		if(this.mSharedPreferencesController.getBoolean("firstrun")) {
			this.mCsvController = new CsvController(this);
			this.mCsvController.readCSV(databaseHelper);
			
			this.mSharedPreferencesController.putInSharedPreference("databaseVersion", 1);
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
		if(this.databaseHelper == null) {
			this.databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		List<HistoryItem> listHistoryItems = new ArrayList<HistoryItem>();
		listHistoryItems = this.databaseHelper.getHistoryItems();
		ArrayList<String> jsonList = new ArrayList<String>();
		for(HistoryItem h : listHistoryItems) {
			jsonList.add(h.toJson(h));
		}
		
		Intent intent = new Intent("android.intents.History");
		intent.putStringArrayListExtra("history", jsonList);
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
		
		if(this.mSharedPreferencesController == null) {
			this.mSharedPreferencesController = new SharedPreferencesController(this);
		}
		if(this.mSharedPreferencesController.getBoolean("firstrun")) {
			this.mSharedPreferencesController.putInSharedPreference("firstrun", false);
		}
	}

}
