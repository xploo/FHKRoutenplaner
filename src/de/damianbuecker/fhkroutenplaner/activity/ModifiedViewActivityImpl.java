package de.damianbuecker.fhkroutenplaner.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.damianbuecker.fhkroutenplaner.interfaces.LogInterface;

import roboguice.activity.RoboActivity;

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

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ModifiedViewActivityImpl.class);

	/* (non-Javadoc)
	 * @see de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logWarning(java.lang.String)
	 */
	@Override
	public void logWarning(String message) {
		logger.warn(message);
	}

	/* (non-Javadoc)
	 * @see de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logError(java.lang.String)
	 */
	@Override
	public void logError(String message) {
		logger.error(message);
	}

	/* (non-Javadoc)
	 * @see de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logInfo(java.lang.String)
	 */
	@Override
	public void logInfo(String message) {
		logger.info(message);
	}
}
