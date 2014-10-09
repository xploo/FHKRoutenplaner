package de.damianbuecker.fhkroutenplaner.controller;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import android.annotation.SuppressLint;
import android.content.Context;

/**
 * The Class DateTimeController.
 */
public class DateTimeController extends Controller {

	/** The Constant DATE_FORMAT. */
	private static final String DATE_FORMAT = "HH:mm:ss:SSS";

	/**
	 * Instantiates a new date time controller.
	 * 
	 * @param context
	 *            the context
	 */
	public DateTimeController(Context context) {
		super(context);
	}

	/**
	 * From date.
	 * 
	 * @param dateTime
	 *            the date time
	 * @return the long
	 */
	public static long fromDate(DateTime dateTime) {
		return dateTime.getMilliseconds(TimeZone.getDefault());
	}

	/**
	 * To date.
	 * 
	 * @param milliseconds
	 *            the milliseconds
	 * @return the date time
	 */
	public static DateTime toDate(long milliseconds) {
		return DateTime.forInstant(milliseconds, TimeZone.getDefault());
	}

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public static long getTime() {
		return System.currentTimeMillis();
	}

	/**
	 * Gets the runtime.
	 * 
	 * @param startTime
	 *            the start time
	 * @param endTime
	 *            the end time
	 * @return the runtime
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getRuntime(long startTime, long endTime) {
		long runtime = endTime - startTime;
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(runtime).toString();
	}
}
