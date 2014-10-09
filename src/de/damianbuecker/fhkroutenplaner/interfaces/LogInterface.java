package de.damianbuecker.fhkroutenplaner.interfaces;

/**
 * The Interface ModifiedViewInterface.
 */
public interface LogInterface {

	/**
	 * Log warning.
	 * 
	 * @param message
	 *            the message
	 */
	public void logWarning(String message);

	/**
	 * Log error.
	 * 
	 * @param message
	 *            the message
	 */
	public void logError(String message);

	/**
	 * Log info.
	 * 
	 * @param message
	 *            the message
	 */
	public void logInfo(String message);

}
