package de.damianbuecker.fhkroutenplaner.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Model {

	private static final Logger logger = LoggerFactory.getLogger(Model.class);
	
	/**
	 * Log.
	 *
	 * @param message the message
	 */
	public void log(String message) {
		logger.info(message);
	}
}
