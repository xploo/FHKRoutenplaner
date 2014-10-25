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
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.widget.TextView;
import de.damianbuecker.fhkroutenplaner.activity.DisplayMapsActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class NFCController.
 */
public class NfcController extends Controller {

	/** The Constant MIME_TEXT_PLAIN. */
	public static final String MIME_TEXT_PLAIN = "text/plain";

	/** The result. */
	public static String RESULT = "result";

	/** The m text view. */
	private TextView mTextView;

	/** The m context. */
	private Context mContext;

	/** The running. */
	private Boolean running;

	/** The Constant INTENT_EXTRA_START_ID. */
	private static final String INTENT_EXTRA_START_ID = "Start_ID";

	/**
	 * Instantiates a new NFC controller.
	 * 
	 * @param tv
	 *            hidden textView
	 */
	public NfcController(TextView tv) {
		super(tv.getContext());
		this.mTextView = tv;
	}

	/**
	 * Instantiates a new NFC controller.
	 * 
	 * @param context
	 *            the context
	 */
	public NfcController(Context context) {
		super(context);
	}

	/**
	 * this method handles the incoming intent
	 * after a Tag was scanned
	 * 
	 * @param intent
	 *            intent
	 * @param context
	 *            the context
	 * @return result if operation was successful 
	 */
	public Boolean handleIntent(Intent intent, Context context) {

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
				this.logMessage("ERROR", "Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

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

	/**
	 * The foreground dispatch system  
	 * allows an activity to intercept an intent and
	 * claim priority over other activities that handle the same intent.
	 * 
	 * @param activity
	 *            the activity
	 * @param adapter
	 *            the adapter
	 */
	public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][] {};
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
	public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.damianbuecker.fhkroutenplaner.controller.Controller#getContext()
	 */
	public Context getContext() {
		return this.mContext;
	}

	/**
	 * This class get the payload from an scanned ndef formated Tag
	 */
	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

		/** The m context. */
		private Context mContext;

		/** The text view reference. */
		private final WeakReference<TextView> textViewReference;

		/** The m shared preferences controller. */
		private SharedPreferencesController mSharedPreferencesController;

		/**
		 * Instantiates a new ndef reader task.
		 * 
		 * @param tv
		 *            the tv
		 */
		public NdefReaderTask(TextView tv) {

			this.textViewReference = new WeakReference<TextView>(tv);
			this.mContext = tv.getContext();
			this.mSharedPreferencesController = new SharedPreferencesController(this.mContext);

			running = this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_ROUTE_RUNNING);
		}

		/**
		 * Instantiates a new ndef reader task.
		 * 
		 * @param context
		 *            context
		 */
		public NdefReaderTask(Context context) {
			this.mContext = context;
			this.mSharedPreferencesController = new SharedPreferencesController(this.mContext);
			this.textViewReference = null;

			running = this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_ROUTE_RUNNING);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(Tag... params) {

			Tag tag = params[0];

			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				return null;
			}

			NdefMessage ndefMessage = ndef.getCachedNdefMessage();

			NdefRecord[] records = ndefMessage.getRecords();
			for (NdefRecord ndefRecord : records) {
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
					try {
						return readText(ndefRecord);
					} catch (UnsupportedEncodingException e) {
						NfcController.this.logMessage("ERROR", e.getMessage());
					}
				}
			}
			return null;
		}

		/**
		 * gets the content from an NDEF-record
		 * 
		 * @param record
		 *            NDEF-record
		 * @return  content of the scanned Tag
		 * @throws UnsupportedEncodingException
		 *             the unsupported encoding exception
		 */
		private String readText(NdefRecord record) throws UnsupportedEncodingException {

			byte[] payload = record.getPayload();

			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

			int languageCodeLength = payload[0] & 0063;

			return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String result) {

			if (running == false) {
				NfcController.this.logMessage("RUNNING ", "False");
			}

			if (running == true) {
				Intent i = new Intent(mContext, DisplayMapsActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra(INTENT_EXTRA_START_ID, result);
				mContext.startActivity(i);
				NfcController.this.logMessage("INFO", result);

			} else if (this.textViewReference != null && result != null) {
				final TextView tv = this.textViewReference.get();
				NfcController.this.logMessage("INFO", result);
				if (tv != null) {
					tv.setText(result);
				}
			}
		}
	}
}