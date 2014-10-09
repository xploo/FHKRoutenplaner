package de.damianbuecker.fhkroutenplaner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.interfaces.ControllerInterface;
import de.damianbuecker.fhkroutenplaner.interfaces.LogInterface;

/**
 * The Class Controller.
 */
public class Controller implements LogInterface, ControllerInterface {

	/** The Constant SHARED_PREFERENCE_ROUTE_RUNNING. */
	protected static final String SHARED_PREFERENCE_ROUTE_RUNNING = "RouteRunning";

	/** The Constant SHARED_PREFERENCE_DATABASE_VERSION. */
	protected static final String SHARED_PREFERENCE_DATABASE_VERSION = "databaseVersion";

	/** The local database helper. */
	private DatabaseHelper mDatabaseHelper;

	/** The context. */
	private Context context;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	/**
	 * Instantiates a new controller.
	 * 
	 * @param context
	 *            the context
	 */
	public Controller(Context context) {
		this.context = context;
		this.mDatabaseHelper = OpenHelperManager.getHelper(this.context, DatabaseHelper.class);
	}

	/**
	 * Gets the database helper.
	 * 
	 * @param context
	 *            the context
	 * @return the database helper
	 */
	@Override
	public DatabaseHelper getDatabaseHelper(Context context) {
		if (this.mDatabaseHelper == null) {
			this.mDatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		}
		return this.mDatabaseHelper;
	}

	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	@Override
	public Context getContext() {
		return this.context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logWarning(java
	 * .lang.String)
	 */
	@Override
	public void logWarning(String message) {
		logger.warn("@" + Controller.class.getSimpleName() + " " + message);
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
		logger.error("@" + Controller.class.getSimpleName() + " " + message);
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
		logger.info("@" + Controller.class.getSimpleName() + " " + message);
	}
}
