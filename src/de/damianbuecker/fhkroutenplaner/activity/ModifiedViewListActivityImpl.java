package de.damianbuecker.fhkroutenplaner.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.damianbuecker.fhkroutenplaner.interfaces.LogInterface;

import android.app.ListActivity;

/**
 * The Class ModifiedViewListActivityImpl.
 */
public class ModifiedViewListActivityImpl extends ListActivity implements LogInterface {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ModifiedViewListActivityImpl.class);

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
