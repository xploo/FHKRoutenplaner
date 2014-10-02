package de.damianbuecker.fhkroutenplaner.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.damianbuecker.fhkroutenplaner.model.Model;
import android.app.Activity;

public class ModifiedActivity extends Activity{
	
	private static final Logger logger = LoggerFactory.getLogger(Model.class);
	
	/**
	 * Log.
	 *
	 * @param message the message
	 */
	public void log(String message) {
		logger.info(message);
	}
	
	/*
	 * Hihi
	 */
	
}
