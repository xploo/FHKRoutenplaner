package de.damianbuecker.fhkroutenplaner.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ListActivity;

public class ModifiedViewListActivityImpl extends ListActivity implements ModifiedViewInterface{
	
	private static final Logger logger = LoggerFactory.getLogger(ModifiedViewListActivityImpl.class);

	/**
	 * Log.
	 *
	 * @param message the message
	 */
	public void log(String message) {
		logger.info(message);
	}
	
	

}
