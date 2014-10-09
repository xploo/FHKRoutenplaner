package de.damianbuecker.fhkroutenplaner.service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;

// TODO: Auto-generated Javadoc
/**
 * The Class NFCService.
 */
public class NFCService extends Service {
	// Tag auslesen
	// - StartEtage - Start ID
	// SharedPrefs -> Letztes Ziel
	// - End Etage - End ID

	// Starten der Imageberechnung anhand der Daten

	// Service muss in Imageview laufen

	/**
	 * Instantiates a new NFC service.
	 * 
	 * @param context
	 *            the context
	 */
	public NFCService(Context context) {
		super(context);
	}

	/** The Constant MIME_TEXT_PLAIN. */
	public static final String MIME_TEXT_PLAIN = "text/plain";

	// protected!
	/**
	 * Handle intent.
	 * 
	 * @param intent
	 *            the intent
	 */
	public void HandleIntent(Intent intent) {

		this.logInfo("NFCSERV - HandleIntent");

		StringBuffer action = new StringBuffer(intent.getAction());

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

			StringBuffer type = new StringBuffer(intent.getType());
			if (MIME_TEXT_PLAIN.equals(type)) {

				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				new NdefReaderTask().execute(tag);

			} else {
				this.logError("TAG - Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

			// In case we would still use the Tech Discovered Intent
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String[] techList = tag.getTechList();
			String searchedTech = Ndef.class.getName();

			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					new NdefReaderTask().execute(tag);

					break;
				}
			}
		}
	}

	/**
	 * Setup foreground dispatch.
	 * 
	 * @param activity
	 *            the activity
	 * @param adapter
	 *            the adapter
	 */
	// public static void setupForegroundDispatch(final Activity activity,
	// NfcAdapter adapter) {
	public void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		this.logInfo("NFCSERV - Foreground");
		final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][] {};

		// Notice that this is the same filter as in our manifest.
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}

		adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
	}

	/**
	 * Stop foreground dispatch.
	 * 
	 * @param activity
	 *            the activity
	 * @param adapter
	 *            the adapter
	 */
	// public static void stopForegroundDispatch(final Activity activity,
	public void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	/**
	 * The Class NdefReaderTask.
	 */
	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Tag... params) {
			NFCService.this.logInfo("NFCSERV - async");
			Tag tag = params[0];

			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				// NDEF is not supported by this Tag.
				return null;
			}
			NdefMessage ndefMessage = ndef.getCachedNdefMessage();

			NdefRecord[] records = ndefMessage.getRecords();
			for (NdefRecord ndefRecord : records) {
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
					try {
						return readText(ndefRecord);
					} catch (UnsupportedEncodingException e) {
						NFCService.this.logError("TAG - Unsupported Encoding: " + e);
					}
				}
			}

			return null;
		}

		/**
		 * Read text.
		 * 
		 * @param record
		 *            the record
		 * @return the string
		 * @throws UnsupportedEncodingException
		 *             the unsupported encoding exception
		 */
		private String readText(NdefRecord record) throws UnsupportedEncodingException {
			NFCService.this.logInfo("NFCSERV - readTest");
			/*
			 * See NFC forum specification for "Text Record Type Definition" at
			 * 3.2.1
			 * 
			 * http://www.nfc-forum.org/specs/
			 * 
			 * bit_7 defines encoding bit_6 reserved for future use, must be 0
			 * bit_5..0 length of IANA language code
			 */

			byte[] payload = record.getPayload();

			// Get the Text Encoding
			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

			// Get the Language Code
			int languageCodeLength = payload[0] & 0063;

			// String languageCode = new String(payload, 1, languageCodeLength,
			// "US-ASCII");
			// e.g. "en"

			// Get the Text
			return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {

			NFCService.this.logInfo("NFCSERV - Postexecute");
			// this.endID = Integer.parseInt(prefs.getString("lastDestination",
			// "0"));

			NFCService.this.logInfo("tagServ: " + result);
		}
	}
}
