package de.damianbuecker.fhkroutenplaner.activity;

import java.io.File;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebView;
import de.damianbuecker.fhkroutenplaner.controller.FileController;
import de.damianbuecker.fhkroutenplaner.controller.ImageController;
import de.damianbuecker.fhkroutenplaner.controller.NfcController;
import de.damianbuecker.fhkroutenplaner.controller.SharedPreferencesController;

/**
 * The Class DisplayMapsActivity.
 */
@ContentView(R.layout.imageview_activity)
public class DisplayMapsActivity extends ModifiedViewActivityImpl {

	/** The m web view. */
	@InjectView(R.id.imageview_web)			WebView mWebView;
	
	/** The start id. */
	private Integer startID;
	
	/** The end id. */
	private Integer endID;
	
	/** The start floor. */
	private Integer startFloor;
	
	/** The m shared preferences controller. */
	private SharedPreferencesController mSharedPreferencesController;
	
	/** The end floor. */
	private Integer endFloor;

	/** The m nfc controller. */
	private NfcController mNFCController;
	
	/** The m image controller. */
	private ImageController mImageController;
	
	/** The m file controller. */
	private FileController mFileController;
	
	/** The m nfc adapter. */
	private NfcAdapter mNfcAdapter;
	
	/** The Constant INTENT_EXTRA_START_ID. */
	private static final String INTENT_EXTRA_START_ID = "Start_ID";
	
	/** The Constant INTENT_EXTRA_START_FLOOR. */
	private static final String INTENT_EXTRA_START_FLOOR =  "Start_floor";
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageview_activity);

		this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		this.mSharedPreferencesController = new SharedPreferencesController(this);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (this.mImageController == null) {
			this.mImageController = new ImageController(this);
		}
		if (this.mFileController == null) {
			this.mFileController = new FileController(this);
		}
		if (this.mNFCController == null) {
		this.mNFCController = new NfcController(this);
		this.mNFCController.handleIntent(getIntent(), this);
		}

		if (this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_ROUTE_RUNNING)) {

			this.endID = Integer.parseInt(this.mSharedPreferencesController.getString(SHARED_PREFERENCE_LAST_DESTINATION));
			ImageController mImgCont = new ImageController(this);
			this.startID = Integer.parseInt(this.getIntent().getExtras().getString(INTENT_EXTRA_START_ID));
			this.startFloor = mImgCont.getEndFloor(startID);

		}else{
			if (!(this.getIntent().getExtras() == null)) {

				if (this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_FIRST_RUN)) {
					this.endID = Integer.parseInt(this.getIntent().getExtras().getString("End_ID"));
				} else {
					this.endID = Integer.parseInt(this.mSharedPreferencesController.getString(SHARED_PREFERENCE_LAST_DESTINATION));
				}
				this.startID = Integer.parseInt(this.getIntent().getExtras().getString(INTENT_EXTRA_START_ID));

				this.startFloor = Integer.parseInt(this.getIntent().getExtras().getString(INTENT_EXTRA_START_FLOOR));
				
				this.logInfo("Was steht im ENDID "+endID.toString());

			}
		}

		this.endFloor = mImageController.getEndFloor(endID);
		mImageController.testAlgorithm(this.startFloor, this.startID, this.endID,
				this.endFloor);

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.reload();

		if (this.mImageController == null) {
			this.mImageController = new ImageController(this);
		}
		if (this.mFileController == null) {
			this.mFileController = new FileController(this);
		}
		if (this.mNFCController == null) {

			this.mNFCController = new NfcController(this);
		}

		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			this.logWarning("No SD Card!");
		} else {

			// 1. Template auslesen
			// 2. Marker austauschen ggn Dateiname Bilder
			// 3. Angepasstes Template schreiben auf SD selber Ordner wie Bilder
			// (HTML)
			// 4. Mit WebvView öffnen
			if (startFloor == endFloor) {

				mWebView.loadUrl("file:///" + Environment.getExternalStorageDirectory()
						+ "/FMS/TestIMG-" + endFloor + ".png");

			} else {

				// Stelle HTML-Seite dar, die zwei Bilder Lädt
				File file = this.mFileController.createHTMLFile(startFloor,
						endFloor,startID,endID);

				mWebView.loadUrl("file://" + file.getAbsolutePath());
			}

		}
		this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_ROUTE_RUNNING, true);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	protected void onNewIntent(Intent intent) {

		this.mNFCController = new NfcController(this);
		this.mNFCController.handleIntent(intent, this);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */

	@SuppressWarnings("static-access")
	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */
		this.mNFCController = new NfcController(this);
		this.mNFCController.setupForegroundDispatch(this, mNfcAdapter);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@SuppressWarnings("static-access")
	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */

		this.mNFCController = new NfcController(this);
		this.mNFCController.stopForegroundDispatch(this, mNfcAdapter);

}

}
