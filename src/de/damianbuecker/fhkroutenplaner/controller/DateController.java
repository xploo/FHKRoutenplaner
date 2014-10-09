package de.damianbuecker.fhkroutenplaner.controller;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import android.content.Context;

public class DateController extends Controller {

	public DateController(Context context) {
		super(context);
	}
	
	public static long fromDate(DateTime dateTime) {
		return dateTime.getMilliseconds(TimeZone.getDefault());
	}
	
	public static DateTime toDate(long milliseconds) {
		return DateTime.forInstant(milliseconds, TimeZone.getDefault());
	}
}
