package de.damianbuecker.fhkroutenplaner.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;

/**
 * The Class ModifiedViewActivityImpl.
 */
public class ModifiedViewActivityImpl extends Activity implements ModifiedViewInterface {

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
