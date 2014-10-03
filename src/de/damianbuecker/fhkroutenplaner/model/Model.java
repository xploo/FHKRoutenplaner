package de.damianbuecker.fhkroutenplaner.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Model {

	private static final Logger logger = LoggerFactory.getLogger(Model.class);
	
	/**
	 * Log warning.
	 *
	 * @param message the message
	 */
	public void logWarning(String message) {
		logger.warn("WARNING", "@" + Model.class.getSimpleName() + " " + message);
	}

	/**
	 * Log error.
	 *
	 * @param message the message
	 */
	public void logError(String message) {
		logger.error("ERROR", "@" + Model.class.getSimpleName() + " " + message);
	}

	/**
	 * Log info.
	 *
	 * @param message the message
	 */
	public void logInfo(String message) {
		logger.info("INFO", "@" + Model.class.getSimpleName() + " " + message);
	}
	
}
