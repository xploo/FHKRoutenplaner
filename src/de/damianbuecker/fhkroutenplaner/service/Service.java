package de.damianbuecker.fhkroutenplaner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Service.
 */
public class Service {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Service.class);
	
	/**
	 * Log warning.
	 *
	 * @param message the message
	 */
	public void logWarning(String message) {
		logger.warn("WARNING", "@" + Service.class.getSimpleName() + " " + message);
	}

	/**
	 * Log error.
	 *
	 * @param message the message
	 */
	public void logError(String message) {
		logger.error("ERROR", "@" + Service.class.getSimpleName() + " " + message);
	}

	/**
	 * Log info.
	 *
	 * @param message the message
	 */
	public void logInfo(String message) {
		logger.info("INFO", "@" + Service.class.getSimpleName() + " " + message);
	}
	
}
