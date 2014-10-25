package de.damianbuecker.fhkroutenplaner.activity;

import android.util.Log;

import roboguice.activity.RoboActivity;
import de.damianbuecker.fhkroutenplaner.interfaces.LogInterface;

/**
 * The Class ModifiedViewActivityImpl.
 */
public class ModifiedViewActivityImpl extends RoboActivity implements LogInterface {

	/** The Constant SHARED_PREFERENCE_ROUTE_RUNNING. */
	protected static final String SHARED_PREFERENCE_ROUTE_RUNNING = "RouteRunning";

	/** The Constant SHARED_PREFERENCE_LAST_DESTINATION. */
	protected static final String SHARED_PREFERENCE_LAST_DESTINATION = "lastDestination";

	/** The Constant SHARED_PREFERENCE_FIRST_RUN. */
	protected static final String SHARED_PREFERENCE_FIRST_RUN = "firstrun";

	/** The Constant SHARED_PREFERENCE_DATABASE_VERSION. */
	protected static final String SHARED_PREFERENCE_DATABASE_VERSION = "databaseVersion";

	/** The Constant INTENT_HISTORY. */
	protected static final String INTENT_HISTORY = "android.intents.History";

	/** The Constant INTENT_NAVIGATION. */
	protected static final String INTENT_NAVIGATION = "android.intents.NavigationActivity";

	/** The Constant INTENT_DISPLAY_MAPS_ACTIVITY. */
	protected static final String INTENT_DISPLAY_MAPS_ACTIVITY = "android.intents.DisplayMapsActivity";
	
	/** The Constant DIRECTORY. */
	protected static final String DIRECTORY = "/FMS/";
	
	/** The Constant FILE_PREFIX. */
	protected static final String FILE_PREFIX = "file:///";
	
	/** The Constant PNG. */
	protected static final String PNG = ".png";
	
	/** The Constant DATABASE_UPDATE_AVAILABLE. */
	protected static final String DATABASE_UPDATE_AVAILABLE = "Datenbankupdate verfügbar!";
	
	/** The Constant DOWNLOAD_INSTALL_UPDATE_QUESTION. */
	protected static final String DOWNLOAD_INSTALL_UPDATE_QUESTION = "Möchten Sie das Update herunterladen und installieren?";
	
	/** The Constant DOWNLOADING_DATABASE_ENTRIES. */
	protected static final String DOWNLOADING_DATABASE_ENTRIES = "Lade Datenbankeinträge herunter. Bitte warten...";
	
	/** The Constant ALERT_DIALOG_TITLE. */
	protected static final String ALERT_DIALOG_TITLE = "Startpunkt benötigt.";

	/** The Constant ALERT_DIALOG_MESSAGE. */
	protected static final String ALERT_DIALOG_MESSAGE = "Bitte halte dein Smartphone an ein RFID-Tag";

	/** The Constant ERROR_MESSAGE_NFC_UNSUPPORTED. */
	protected static final String ERROR_MESSAGE_NFC_UNSUPPORTED = "This device doesn't support NFC.";

	/** The Constant ERROR_MESSAGE_NFC_DISABLED. */
	protected static final String ERROR_MESSAGE_NFC_DISABLED = "NFC is disabled.";
	
	/** The Constant ERROR. */
	protected static final String ERROR = "ERROR";
	
	/** The Constant INFO. */
	protected static final String INFO = "INFO";
	
	/** The Constant WARNING. */
	protected static final String WARNING = "WARNING";
	
	/** The Constant DEFAULT. */
	protected static final String DEFAULT = "DEFAULT";
	
	/** The Constant SELECT_ROOM_TYPE_DESTINATION. */
	protected static final String SELECT_ROOM_TYPE_DESTINATION = "Bitte den Raumtyp des Zielortes wählen!";
	
	/** The Constant SELECT_DESTINATION_ROOM. */
	protected static final String SELECT_DESTINATION_ROOM = "Bitte Ziel Raum wählen!";
	
	/** The Constant WHITESPACE. */
	protected static final String WHITESPACE = " ";
	
	/** The Constant END_ID. */
	protected static final String END_ID = "End_ID";
	
	/** The Constant AFTER. */
	protected static final String AFTER = "nach";
	
	/** The Constant INVALID_SELECTION. */
	protected static final String INVALID_SELECTION = "Bitte eine gültige Auswahl treffen!";
	
	/** The Constant NEW_ROUTE. */
	protected static final String NEW_ROUTE = "Neue Route";
	
	/** The Constant QUIT_ROUTE. */
	protected static final String QUIT_ROUTE = "Route beenden";
	
	/** The Constant INTENT_EXTRA_START_ID. */
	protected static final String INTENT_EXTRA_START_ID = "Start_ID";
	
	/** The Constant INTENT_EXTRA_START_FLOOR. */
	protected static final String INTENT_EXTRA_START_FLOOR = "Start_floor";
	
	/** The Constant ROUTE_IS_BEING_CALCULATED. */
	protected static final String ROUTE_IS_BEING_CALCULATED = "Route wird berechnet.";
	
	/** The Constant TO_STARTING_FLOOR. */
	protected static final String TO_STARTING_FLOOR = "Zur Startetage:";
	
	/** The Constant TO_DESTINATION_FLOOR. */
	protected static final String TO_DESTINATION_FLOOR = "Zur Zieletage:";
	
	/** The Constant FILENAME. */
	protected static final String FILENAME = "TestIMG-";
	
	/** The Constant SELECTED_ITEM. */
	protected static final String SELECTED_ITEM = "selectedItem";
	
	/** The Constant DATE_FORMAT_DD_MM_YYYY. */
	protected static final String DATE_FORMAT_DD_MM_YYYY = "DD.MM.YYYY";
	
	/** The Constant TIME_FORMAT_HH_MM_SS. */
	protected static final String TIME_FORMAT_HH_MM_SS =  "hh:mm:ss";

	/* (non-Javadoc)
	 * @see de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void logMessage(String tag, String message) {
		if(tag.equals(INFO)) {
			Log.i(INFO, message);
		} else if(tag.equals(WARNING)) {
			Log.w(WARNING, message);
		} else if(tag.equals(ERROR)) {
			Log.e(ERROR, message);
		} else {
			Log.d(DEFAULT, message);
		}
	}

}
