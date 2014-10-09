package de.damianbuecker.fhkroutenplaner.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import de.damianbuecker.fhkroutenplaner.controller.FileController;
import de.damianbuecker.fhkroutenplaner.controller.ImageController;
import de.damianbuecker.fhkroutenplaner.controller.NfcController;

/**
 * The Class DisplayMapsActivity.
 */
public class DisplayMapsActivity extends ModifiedViewActivityImpl {

	/** The start id. */
	private Integer startID;

	/** The end id. */
	private Integer endID;

	/** The start floor. */
	private Integer startFloor;

	/** The extras. */
	private Bundle extras;

	/** The prefs. */
	private SharedPreferences prefs;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageview_activity);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		prefs = getSharedPreferences("de.damianbuecker.fhkroutenplaner",
				MODE_PRIVATE);

		if (this.mImageController == null) {
			this.mImageController = new ImageController(this);
		}
		if (this.mFileController == null) {
			this.mFileController = new FileController(this);
		}
		if (this.mNFCController == null) {
			this.mNFCController = new NfcController(this);
		}

		extras = getIntent().getExtras();
		
		if (prefs.getBoolean("RouteRunning", true)) {

			this.endID = Integer.parseInt(prefs.getString("lastDestination",
					"0"));

			this.startID = Integer.parseInt(extras.getString("Start_ID"));
			this.startFloor = mImageController.getEndFloor(startID);

		} else {
			if (!(extras == null)) {

				if (prefs.getBoolean("firstrun", true)) {
					this.endID = Integer.parseInt(extras.getString("End_ID"));
				} else {
					this.endID = Integer.parseInt(prefs.getString(
							"lastDestination", "0"));
				}
				this.startID = Integer.parseInt(extras.getString("Start_ID"));

				this.startFloor = Integer.parseInt(extras
						.getString("Start_floor"));

			}
		}

		// <---- Crash verhindern, wenn startTag gleich dem gewählten ziel ist
		if (startID == endID) {
			finish();
			Toast.makeText(this, "Ziel erreicht!", Toast.LENGTH_LONG).show();
			Intent intent = new Intent("android.intents.NFCConnector");
			startActivity(intent);
			return;

		}
		// -------------------------------------------------------------------->

		
		this.endFloor = mImageController.getEndFloor(endID);
		mImageController.testAlgorithm(this.startFloor, this.startID, this.endID,
				this.endFloor);

		WebView mWebView = (WebView) findViewById(R.id.imageview_web);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.reload();
		

		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Log.d("TAG", "No SDCARD");
		} else {

			// 1. Template auslesen
			// 2. Marker austauschen ggn Dateiname Bilder
			// 3. Angepasstes Template schreiben auf SD selber Ordner wie Bilder
			// (HTML)
			// 4. Mit WebvView öffnen
			if (startFloor == endFloor) {

				mWebView.loadUrl("file:///"
						+ Environment.getExternalStorageDirectory()
						+ "/FMS/TestIMG-" + endFloor + ".png");

			} else {

				// Stelle HTML-Seite dar, die zwei Bilder Lädt

				File file = this.mFileController.createHTMLFile(startFloor,
						endFloor,startID,endID);

				
				mWebView.loadUrl("file://" + file.getAbsolutePath());				
				
				// mWebView.loadUrl("file:///"
				// + Environment.getExternalStorageDirectory()
				// + "/FMS/template.html");

			}

		}
		
		mNFCController.handleIntent(getIntent(), this);
		prefs.edit().putBoolean("RouteRunning", true).commit();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		/**
		 * Disposes the DatabaseHelper.
		 */
		// if (this.databaseHelper != null) {
		// OpenHelperManager.releaseHelper();
		// this.databaseHelper = null;
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	protected void onNewIntent(Intent intent) {

		this.mNFCController = new NfcController(this);
		this.mNFCController.handleIntent(intent, this);
		// serv = new NFCService(this);
		// serv.HandleIntent(intent);

	}

	/*
	 * (non-Javadoc)
	 * 
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

		// Setzten von Running auf false

		this.mNFCController = new NfcController(this);
		this.mNFCController.setupForegroundDispatch(this, mNfcAdapter);
		// serv = new NFCService(this);
		// serv.setupForegroundDispatch(this, mNfcAdapter);
	}

	/*
	 * (non-Javadoc)
	 * 
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

		// serv = new NFCService(this);
		// serv.stopForegroundDispatch(this, mNfcAdapter);

	}

}
