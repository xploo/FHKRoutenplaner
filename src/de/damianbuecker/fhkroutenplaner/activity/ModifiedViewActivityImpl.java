package de.damianbuecker.fhkroutenplaner.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;

public class ModifiedViewActivityImpl extends Activity implements ModifiedViewInterface{
	
	private static final Logger logger = LoggerFactory.getLogger(ModifiedViewActivityImpl.class);
	
	/**
	 * Log.
	 *
	 * @param message the message
	 */
	public void log(String message) {
		logger.info(message);
	}
}
