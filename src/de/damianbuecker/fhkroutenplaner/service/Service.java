package de.damianbuecker.fhkroutenplaner.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

/**
 * The Class Service.
 */
public class Service {

	/** The m context. */
	private Context mContext;

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Service.class);

	/**
	 * Instantiates a new service.
	 * 
	 * @param context
	 *            the context
	 */
	public Service(Context context) {
		this.mContext = context;
	}

	/**
	 * Gets the context.
	 * 
	 * @return the context
	 */
	public Context getcontext() {
		return this.mContext;
	}

	/**
	 * Log warning.
	 * 
	 * @param message
	 *            the message
	 */
	public void logWarning(String message) {
		logger.warn("@" + Service.class.getSimpleName() + " " + message);
	}

	/**
	 * Log error.
	 * 
	 * @param message
	 *            the message
	 */
	public void logError(String message) {
		logger.error("@" + Service.class.getSimpleName() + " " + message);
	}

	/**
	 * Log info.
	 * 
	 * @param message
	 *            the message
	 */
	public void logInfo(String message) {
		logger.info("@" + Service.class.getSimpleName() + " " + message);
	}

}
