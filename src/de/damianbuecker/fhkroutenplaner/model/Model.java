package de.damianbuecker.fhkroutenplaner.model;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.interfaces.LogInterface;

/**
 * The Class Model.
 */
public class Model implements LogInterface {

	/** The database controller. */
	private DatabaseHelper databaseController;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Model.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logWarning(java
	 * .lang.String)
	 */
	@Override
	public void logWarning(String message) {
		logger.warn("@" + Model.class.getSimpleName() + " " + message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logError(java
	 * .lang.String)
	 */
	@Override
	public void logError(String message) {
		logger.error("@" + Model.class.getSimpleName() + " " + message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logInfo(java
	 * .lang.String)
	 */
	@Override
	public void logInfo(String message) {
		logger.info("@" + Model.class.getSimpleName() + " " + message);
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
				this.logError(e.getMessage());
			}
		}
	}
}
