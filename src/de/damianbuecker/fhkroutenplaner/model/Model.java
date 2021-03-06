package de.damianbuecker.fhkroutenplaner.model;

import java.sql.SQLException;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.interfaces.LogInterface;

// TODO: Auto-generated Javadoc
/**
 * The Class Model.
 */
public class Model implements LogInterface {

	/** The database controller. */
	private DatabaseHelper databaseController;

	/* (non-Javadoc)
	 * @see de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void logMessage(String tag, String message) {
		if(tag.equals("INFO")) {
			Log.i("INFO", message);
		} else if(tag.equals("WARNING")) {
			Log.w("WARNING", message);
		} else if(tag.equals("ERROR")) {
			Log.e("ERROR", message);
		} else {
			Log.d(tag, message);
		}
	}

	/**
	 * Create.
	 * 
	 * @param context
	 *            the context
	 */
	public void writeToDatabase(Context context) {
		this.create(context);
	}

	/**
	 * Creates the.
	 * 
	 * @param context
	 *            the context
	 */
	private void create(Context context) {
		if (this.databaseController == null) {
			this.databaseController = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		}

		if (this instanceof HistoryItem) {
			Dao<HistoryItem, Integer> historyItemDao;
			try {
				historyItemDao = this.databaseController.getHistoryItemDataDao();
				if (this != null) {
					historyItemDao.create((HistoryItem) this);
				}
			} catch (SQLException e) {
				this.logMessage("ERROR", e.getMessage());
			}
		}
	}
}
