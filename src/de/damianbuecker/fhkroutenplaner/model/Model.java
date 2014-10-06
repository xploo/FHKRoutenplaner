package de.damianbuecker.fhkroutenplaner.model;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;

/**
 * The Class Model.
 */
abstract public class Model {

	/** The database controller. */
	private DatabaseHelper databaseController;
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Model.class);
	
	/**
	 * Log warning.
	 *
	 * @param message the message
	 */
	public void logWarning(String message) {
		logger.warn("@" + Model.class.getSimpleName() + " " + message);
	}

	/**
	 * Log error.
	 *
	 * @param message the message
	 */
	public void logError(String message) {
		logger.error("@" + Model.class.getSimpleName() + " " + message);
	}

	/**
	 * Log info.
	 *
	 * @param message the message
	 */
	public void logInfo(String message) {
		logger.info("@" + Model.class.getSimpleName() + " " + message);
	}
	
	/**
	 * Create.
	 *
	 * @param context the context
	 */
	public void create(Context context) {
		if (this.databaseController == null) {
			this.databaseController = OpenHelperManager.getHelper(context,
					DatabaseHelper.class);
		}

		if (this instanceof HistoryItem) {
			Dao<HistoryItem, Integer> historyItemDao;
			try {
				historyItemDao = this.databaseController
						.getHistoryItemDataDao();
				if (this != null) {
					historyItemDao.create((HistoryItem) this);
				}
			} catch (SQLException e) {
				this.logError(e.getMessage());
			}
		}
	}
}
