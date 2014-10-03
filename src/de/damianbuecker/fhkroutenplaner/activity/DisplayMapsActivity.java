package de.damianbuecker.fhkroutenplaner.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import de.damianbuecker.fhkroutenplaner.controller.FileController;
import de.damianbuecker.fhkroutenplaner.controller.ImageController;
import de.damianbuecker.fhkroutenplaner.controller.NFCController;
import de.damianbuecker.fhkroutenplaner.service.NFCService;

public class DisplayMapsActivity extends ModifiedViewActivityImpl {

	private Integer startID;
	private Integer endID;
	private Integer startFloor;
	private Bundle extras;
	private SharedPreferences prefs;
	private Integer endFloor;

	private NFCController mNFCController;
	private ImageController mImageController; 
	private FileController mFileController;
	private NfcAdapter mNfcAdapter;
	private NFCController nfccon;

	private NFCService serv;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageview_activity);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		prefs = getSharedPreferences("de.damianbuecker.fhkroutenplaner",
				MODE_PRIVATE);

		// ABfrage Running??
		
		this.mNFCController = new NFCController(this);
//		this.mNFCController.setContext(this);		
		mNFCController.handleIntent(getIntent(),this);			
		
		
		extras = getIntent().getExtras();
		if (prefs.getBoolean("RouteRunning", true)) {

			this.endID = Integer.parseInt(prefs.getString("End_ID", "0"));
			ImageController mImgCont = new ImageController(this);
			this.startID = Integer.parseInt(extras.getString("Start_ID"));
			this.startFloor = mImgCont.getEndFloor(startID);

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

		ImageController mImgCont = new ImageController(this);
		mImgCont.testAlgorithm(this.startFloor, this.startID, this.endID);
		this.endFloor = mImgCont.getEndFloor(endID);

		WebView mWebView = (WebView) findViewById(R.id.imageview_web);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setBuiltInZoomControls(true);

		if (this.mImageController == null) {
			this.mImageController = new ImageController(this);
		}
		if (this.mFileController == null) {
			this.mFileController = new FileController(this);
		}
		if (this.mNFCController == null) {

			this.mNFCController = new NFCController(this);
		}

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
						endFloor);

				mWebView.loadUrl("file://" + file.getAbsolutePath());

				// mWebView.loadUrl("file:///"
				// + Environment.getExternalStorageDirectory()
				// + "/FMS/template.html");

			}

		}

	}

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

	protected void onNewIntent(Intent intent) {

		nfccon = new NFCController(this);
		nfccon.handleIntent(intent,this);
		// serv = new NFCService(this);
		// serv.HandleIntent(intent);

	}

	@SuppressWarnings("static-access")
	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */

		// Setzten von Running auf false

		nfccon = new NFCController(this);
		nfccon.setupForegroundDispatch(this, mNfcAdapter);
		// serv = new NFCService(this);
		// serv.setupForegroundDispatch(this, mNfcAdapter);
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onPause() {
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */

		nfccon = new NFCController(this);
		nfccon.stopForegroundDispatch(this, mNfcAdapter);
		// serv = new NFCService(this);
		// serv.stopForegroundDispatch(this, mNfcAdapter);

		super.onPause();
	}

}
