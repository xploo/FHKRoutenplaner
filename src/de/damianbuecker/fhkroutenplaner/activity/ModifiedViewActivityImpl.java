package de.damianbuecker.fhkroutenplaner.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.activity.RoboActivity;

/**
 * The Class ModifiedViewActivityImpl.
 */
public class ModifiedViewActivityImpl extends RoboActivity implements ModifiedViewInterface {

	protected static final String SHARED_PREFERENCE_ROUTE_RUNNING = "RouteRunning";
	
	protected static final String SHARED_PREFERENCE_LAST_DESTINATION = "lastDestination";
	
	protected static final String SHARED_PREFERENCE_FIRST_RUN = "firstrun";
	
	protected static final String SHARED_PREFERENCE_DATABASE_VERSION = "databaseVersion";
	
	/** The Constant INTENT_HISTORY. */
	protected static final String INTENT_HISTORY = "android.intents.History";
	
	/** The Constant INTENT_NAVIGATION. */
	protected static final String INTENT_NAVIGATION = "android.intents.NavigationActivity";
	
	protected static final String INTENT_DISPLAY_MAPS_ACTIVITY = "android.intents.DisplayMapsActivity";
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ModifiedViewActivityImpl.class);

	/* (non-Javadoc)
	 * @see de.damianbuecker.fhkroutenplaner.activity.ModifiedViewInterface#logWarning(java.lang.String)
	 */
	@Override
	public void logWarning(String message) {
		logger.warn(message);
	}

	/* (non-Javadoc)
	 * @see de.damianbuecker.fhkroutenplaner.activity.ModifiedViewInterface#logError(java.lang.String)
	 */
	@Override
	public void logError(String message) {
		logger.error(message);
	}

	/* (non-Javadoc)
	 * @see de.damianbuecker.fhkroutenplaner.activity.ModifiedViewInterface#logInfo(java.lang.String)
	 */
	@Override
	public void logInfo(String message) {
		logger.info(message);
	}
}
