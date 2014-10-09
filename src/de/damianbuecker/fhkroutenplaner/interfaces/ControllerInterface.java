package de.damianbuecker.fhkroutenplaner.interfaces;

import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import android.content.Context;

/**
 * The Interface ControllerInterface.
 */
public interface ControllerInterface {

	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	public Context getContext();

	/**
	 * Gets the database helper.
	 * 
	 * @param context
	 *            the context
	 * @return the database helper
	 */
	public DatabaseHelper getDatabaseHelper(Context context);
}
