package de.damianbuecker.fhkroutenplaner.controller;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.SuppressLint;
import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;

/**
 * The Class Controller.
 */
public class Controller {

	/** The local database helper. */
	private DatabaseHelper mDatabaseHelper;

	/** The context. */
	private Context context;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);

	/**
	 * Instantiates a new controller.
	 */
	protected Controller() {
	}

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
	public DatabaseHelper getDatabaseHelper(Context context) {
		if (this.mDatabaseHelper == null) {
			this.mDatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		}
		return this.mDatabaseHelper;
	}

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public long getTime() {

		long time = System.currentTimeMillis();
		return time;
	}

	/**
	 * Gets the runtime.
	 * 
	 * @param startTime
	 *            the start time
	 * @param endTime
	 *            the end time
	 * @return the runtime
	 */
	@SuppressLint("SimpleDateFormat")
	public String getRuntime(long startTime, long endTime) {

		long runtime = endTime - startTime;
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		// System.out.println(sdf.format(runtime));
		return sdf.format(runtime).toString();
	}

	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	public Context getContext() {
		return this.context;
	}

	/**
	 * Log.
	 * 
	 * @param message
	 *            the message
	 */
	public void log(String message) {
		logger.info(message);
	}
}
