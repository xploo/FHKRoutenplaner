package de.damianbuecker.fhkroutenplaner.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.ListActivity;

public class ModifiedViewListActivityImpl extends ListActivity implements ModifiedViewInterface{
	
	private static final Logger logger = LoggerFactory.getLogger(ModifiedViewListActivityImpl.class);

	@Override
	public void logWarning(String message) {
		logger.warn("WARNING", message);
	}

	@Override
	public void logError(String message) {
		logger.error("ERROR", message);
	}

	@Override
	public void logInfo(String message) {
		logger.info("INFO", message);
	}

}
