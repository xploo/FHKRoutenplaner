package de.damianbuecker.fhkroutenplaner.activity;

import android.app.ListActivity;
import android.util.Log;
import de.damianbuecker.fhkroutenplaner.interfaces.LogInterface;

// TODO: Auto-generated Javadoc
/**
 * The Class ModifiedViewListActivityImpl.
 */
public class ModifiedViewListActivityImpl extends ListActivity implements LogInterface {

	/* (non-Javadoc)
	 * @see de.damianbuecker.fhkroutenplaner.interfaces.LogInterface#logMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public void logMessage(String tag, String message) {
		if(tag.equals("INFO")) {
			Log.i("INFO", message);
		} else if(tag.equals("WARNING")) {
			Log.w("WARNING", message);
		} else if(tag.equals("ERROR")) {
			Log.e("ERROR", message);
		} else {
			Log.d(tag, message);
		}	
	}

}
