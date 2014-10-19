package de.damianbuecker.fhkroutenplaner.activity;

import android.util.Log;

import roboguice.activity.RoboActivity;
import de.damianbuecker.fhkroutenplaner.interfaces.LogInterface;

/**
 * The Class ModifiedViewActivityImpl.
 */
public class ModifiedViewActivityImpl extends RoboActivity implements LogInterface {

	/** The Constant SHARED_PREFERENCE_ROUTE_RUNNING. */
	protected static final String SHARED_PREFERENCE_ROUTE_RUNNING = "RouteRunning";

	/** The Constant SHARED_PREFERENCE_LAST_DESTINATION. */
	protected static final String SHARED_PREFERENCE_LAST_DESTINATION = "lastDestination";

	/** The Constant SHARED_PREFERENCE_FIRST_RUN. */
	protected static final String SHARED_PREFERENCE_FIRST_RUN = "firstrun";

	/** The Constant SHARED_PREFERENCE_DATABASE_VERSION. */
	protected static final String SHARED_PREFERENCE_DATABASE_VERSION = "databaseVersion";

	/** The Constant INTENT_HISTORY. */
	protected static final String INTENT_HISTORY = "android.intents.History";

	/** The Constant INTENT_NAVIGATION. */
	protected static final String INTENT_NAVIGATION = "android.intents.NavigationActivity";

	/** The Constant INTENT_DISPLAY_MAPS_ACTIVITY. */
	protected static final String INTENT_DISPLAY_MAPS_ACTIVITY = "android.intents.DisplayMapsActivity";
	
	/** The Constant DIRECTORY. */
	protected static final String DIRECTORY = "/FMS/";
	
	/** The Constant FILE_PREFIX. */
	protected static final String FILE_PREFIX = "file:///";
	
	/** The Constant PNG. */
	protected static final String PNG = ".png";

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
			Log.d("DEFAULT", message);
		}
	}

}
