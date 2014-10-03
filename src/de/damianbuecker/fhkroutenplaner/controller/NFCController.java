package de.damianbuecker.fhkroutenplaner.controller;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import de.damianbuecker.fhkroutenplaner.activity.DisplayMapsActivity;

public class NFCController extends Controller {

	public static final String MIME_TEXT_PLAIN = "text/plain";
	public static String RESULT = "result";
	private TextView mTextView;
	private Context mContext;
	private SharedPreferences prefs;
	private Boolean running;

	public NFCController(TextView tv) {
		this.mTextView = tv;
		this.mContext = tv.getContext();
	}

	public NFCController(Context context) {
		super(context);
	}

	public Boolean handleIntent(Intent intent, Context context) {
		// TODO: handle Intent

		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)) {

				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				if (this.mTextView == null) {
					new NdefReaderTask(context).execute(tag);
				} else {
					new NdefReaderTask(this.mTextView).execute(tag);
				}

			} else {
				Log.d("TAG", "Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

			// In case we would still use the Tech Discovered Intent
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String[] techList = tag.getTechList();
			String searchedTech = Ndef.class.getName();

			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					if (this.mTextView == null) {
						new NdefReaderTask(this.mContext).execute(tag);
					} else {
						new NdefReaderTask(this.mTextView).execute(tag);
					}
					break;
				}
			}
		}
		return true;
	}

	public static void setupForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(),
				activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(
				activity.getApplicationContext(), 0, intent, 0);

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

		adapter.enableForegroundDispatch(activity, pendingIntent, filters,
				techList);
	}

	public static void stopForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	public Context getContext() {
		return this.mContext;
	}

//	public void setContext(Context context) {
//		this.mContext = context;
//	}

	public void receiveResult(String result) {
	}

	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

		private Context mContext;
		private final WeakReference<TextView> textViewReference;

		public NdefReaderTask(TextView tv) {

			this.textViewReference = new WeakReference<TextView>(tv);
			this.mContext = tv.getContext();
			prefs = mContext.getSharedPreferences(
					"de.damianbuecker.fhkroutenplaner", mContext.MODE_PRIVATE);
			running = prefs.getBoolean("RouteRunning", false);
		}

		public NdefReaderTask(Context context) {
			this.mContext = context;
			prefs = mContext.getSharedPreferences(
					"de.damianbuecker.fhkroutenplaner", mContext.MODE_PRIVATE);
			this.textViewReference = null;
			if (mContext == null) {

				NFCController.this.log("Constructor Context - null");
			} else
				NFCController.this.log("Constructor Context nicht null");

			running = prefs.getBoolean("RouteRunning", false);

			if (running == true) {
				NFCController.this.log("Constructor - yes");
			} else {
				NFCController.this.log("Constructor - no");
			}

		}

		@Override
		protected String doInBackground(Tag... params) {

			Tag tag = params[0];

			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				// NDEF is not supported by this Tag.
				return null;
			}

			NdefMessage ndefMessage = ndef.getCachedNdefMessage();

			NdefRecord[] records = ndefMessage.getRecords();
			for (NdefRecord ndefRecord : records) {
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN
						&& Arrays.equals(ndefRecord.getType(),
								NdefRecord.RTD_TEXT)) {
					try {
						return readText(ndefRecord);
					} catch (UnsupportedEncodingException e) {
						Log.e("TAG", "Unsupported Encoding", e);
					}
				}
			}

			return null;
		}

		private String readText(NdefRecord record)
				throws UnsupportedEncodingException {
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
			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8"
					: "UTF-16";

			// Get the Language Code
			int languageCodeLength = payload[0] & 0063;

			// String languageCode = new String(payload, 1, languageCodeLength,
			// "US-ASCII");
			// e.g. "en"

			// Get the Text
			return new String(payload, languageCodeLength + 1, payload.length
					- languageCodeLength - 1, textEncoding);
		}

		@SuppressWarnings("static-access")
		@Override
		protected void onPostExecute(String result) {

			NFCController.this.log("RouteRunningreP");
			if (running == true) {				
				NFCController.this.log("RouteRunning");
				//Intent intent = new Intent("android.intents.NFCGO");
				//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				

//				NFCController.this.getContext().startActivity(intent);
				Intent i = new Intent(mContext, DisplayMapsActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("Start_ID", result);
				mContext.startActivity(i);
				
				

			} else if (this.textViewReference != null && result != null) {
				final TextView tv = this.textViewReference.get();
				if (tv != null) {
					tv.setText(result);

					// NFCController.this.receiveResult(result);
				}

			}
			NFCController.this.log(result);
		}

	}
}
