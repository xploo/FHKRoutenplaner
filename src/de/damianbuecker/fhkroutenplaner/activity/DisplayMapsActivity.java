package de.damianbuecker.fhkroutenplaner.activity;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Point;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.damianbuecker.fhkroutenplaner.controller.FileController;
import de.damianbuecker.fhkroutenplaner.controller.ImageController;
import de.damianbuecker.fhkroutenplaner.controller.NfcController;
import de.damianbuecker.fhkroutenplaner.controller.SharedPreferencesController;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;

/**
 * The Class DisplayMapsActivity.
 */
@ContentView(R.layout.display_maps_activity)
public class DisplayMapsActivity extends ModifiedViewActivityImpl {

	/** The list items. */
	private String[] listItems = { "Neue Route", "Route beenden" };

	/** The m drawer layout. */
	@InjectView(R.id.drawer_layout)
	private DrawerLayout mDrawerLayout;

	/** The m drawer list. */
	@InjectView(R.id.left_drawer)
	private ListView mDrawerList;

	@InjectView(R.id.textViewBottomleft)
	private TextView mTextViewBottomLeft;

	@InjectView(R.id.textViewBottomright)
	private TextView mTextViewBottomRight;

	/** The btn toggle drawer. */
	@InjectView(R.id.btntoggledrawer)
	private Button btnToggleDrawer;

	/** The m list view drawer. */
	@InjectView(R.id.left_drawer)
	private ListView mListViewDrawer;

	/** The btn left. */
	@InjectView(R.id.btnleft)
	private Button btnLeft;

	/** The btn right. */
	@InjectView(R.id.btnright)
	private Button btnRight;

	/** The m web view. */
	@InjectView(R.id.webView)
	private WebView mWebView;

	/** The m nfc adapter. */
	private NfcAdapter mNfcAdapter;

	/** The m nfc controller. */
	private NfcController mNFCController;

	/** The m image controller. */
	private ImageController mImageController;
	
	private DatabaseHelper mDatabasehelper;

	/** The m file controller. */
	private FileController mFileController;

	/** The m shared preferences controller. */
	private SharedPreferencesController mSharedPreferencesController;

	/** The Constant INTENT_EXTRA_START_ID. */
	private static final String INTENT_EXTRA_START_ID = "Start_ID";

	/** The Constant INTENT_EXTRA_START_FLOOR. */
	private static final String INTENT_EXTRA_START_FLOOR = "Start_floor";
	
	private Double xPos;
	private Double yPos;

	private Integer endID;
	private Integer startID;
	private Integer startFloor;
	private Integer endFloor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Toast.makeText(this, "Route wird berechnet.", Toast.LENGTH_LONG).show();
		
		this.mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listItems));
		this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		this.btnToggleDrawer.setOnClickListener(new ButtonDrawerToggleListener());
		this.btnLeft.setOnClickListener(new ButtonLeftRightListener());
		this.btnRight.setOnClickListener(new ButtonLeftRightListener());
		
		this.mDrawerLayout.setScrimColor(Color.TRANSPARENT);
		

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
		if(this.mDatabasehelper == null){
			this.mDatabasehelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}

		if (this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_ROUTE_RUNNING)) {

			this.endID = Integer.parseInt(this.mSharedPreferencesController.getString(SHARED_PREFERENCE_LAST_DESTINATION));
			ImageController mImgCont = new ImageController(this);
			this.startID = Integer.parseInt(this.getIntent().getExtras().getString(INTENT_EXTRA_START_ID));
			this.startFloor = mImgCont.getEndFloor(startID);

		} else {
			if (!(this.getIntent().getExtras() == null)) {

				if (this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_FIRST_RUN)) {
					this.endID = Integer.parseInt(this.getIntent().getExtras().getString("End_ID"));
				} else {
					this.endID = Integer.parseInt(this.mSharedPreferencesController.getString(SHARED_PREFERENCE_LAST_DESTINATION));
				}
				this.startID = Integer.parseInt(this.getIntent().getExtras().getString(INTENT_EXTRA_START_ID));

				this.startFloor = Integer.parseInt(this.getIntent().getExtras().getString(INTENT_EXTRA_START_FLOOR));
			}
		}

		this.endFloor = mImageController.getEndFloor(endID);
		mImageController.testAlgorithm(this.startFloor, this.startID, this.endID, this.endFloor);

		try {
			List<Tag> listTag = this.mDatabasehelper.getTagById(String.valueOf(startID));
			
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			 this.xPos = (listTag.get(0).getX_pos()*2) - (width/2);
			 this.yPos = (listTag.get(0).getY_pos()*2) - (height/2);
			 this.logMessage("INFO XPOS", xPos.toString());
			 this.logMessage("INFO YPOS", yPos.toString());
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		

		if (startFloor == endFloor) {
			this.btnLeft.setVisibility(View.INVISIBLE);
			this.btnRight.setVisibility(View.INVISIBLE);
			this.mTextViewBottomLeft.setVisibility(View.INVISIBLE);
			this.mTextViewBottomRight.setVisibility(View.INVISIBLE);
		}

		btnLeft.setVisibility(View.INVISIBLE);
		mTextViewBottomLeft.setVisibility(View.INVISIBLE);

		mTextViewBottomLeft.setText("Zur Startetage: " + startFloor);
		mTextViewBottomRight.setText("Zur Zieletage: " + endFloor);
		
		
		this.mWebView.loadUrl(FILE_PREFIX + Environment.getExternalStorageDirectory() + DIRECTORY + "TestIMG-" + startFloor + startID + PNG);

		mWebView.setPictureListener(new PictureListener() {			

			@Override			
			public void onNewPicture(WebView view, Picture picture) {				
				//mWebView.setInitialScale(85);	
				mWebView.getSettings().setBuiltInZoomControls(true);
				mWebView.getSettings().setDisplayZoomControls(false);
				mWebView.getSettings().setLoadWithOverviewMode(false);
				mWebView.getSettings().setUseWideViewPort(true);
				mWebView.setScrollbarFadingEnabled(true);				
				mWebView.scrollBy(xPos.intValue(), yPos.intValue());
				
				

			}
		});
	}

	/**
	 * Select item.
	 * 
	 * @param position
	 *            the position
	 */
	private void selectItem(int position) {
		
		if (position == 0) {			
			Intent intent = new Intent(this, NavigationActivity.class);
			startActivity(intent);
			finish();
			
		} else if (position == 1) {
			Intent intent = new Intent(this, StartUpActivity.class);
			startActivity(intent);
			finish();
		}

		/**
		 * Cleanup here
		 */
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
		this.mNFCController = new NfcController(this);
		this.mNFCController.setupForegroundDispatch(this, mNfcAdapter);
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onNewIntent(android.content.Intent)
	 */
	protected void onNewIntent(Intent intent) {
		this.mNFCController = new NfcController(this);
		this.mNFCController.handleIntent(intent, this);
	}

	/**
	 * The listener interface for receiving drawerItemClick events. The class
	 * that is interested in processing a drawerItemClick event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addDrawerItemClickListener<code> method. When
	 * the drawerItemClick event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see DrawerItemClickEvent
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}

	/**
	 * The listener interface for receiving buttonDrawerToggle events. The class
	 * that is interested in processing a buttonDrawerToggle event implements
	 * this interface, and the object created with that class is registered with
	 * a component using the component's
	 * <code>addButtonDrawerToggleListener<code> method. When
	 * the buttonDrawerToggle event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see ButtonDrawerToggleEvent
	 */
	private class ButtonDrawerToggleListener implements View.OnClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			if (DisplayMapsActivity.this.mDrawerLayout.isDrawerOpen(DisplayMapsActivity.this.mListViewDrawer)) {
				DisplayMapsActivity.this.mDrawerLayout.closeDrawer(DisplayMapsActivity.this.mListViewDrawer);
			} else {
				DisplayMapsActivity.this.mDrawerLayout.openDrawer(DisplayMapsActivity.this.mListViewDrawer);
			}
		}
	}

	/**
	 * The listener interface for receiving buttonLeftRight events. The class
	 * that is interested in processing a buttonLeftRight event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addButtonLeftRightListener<code> method. When
	 * the buttonLeftRight event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see ButtonLeftRightEvent
	 */
	private class ButtonLeftRightListener implements View.OnClickListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.btnleft) {
				DisplayMapsActivity.this.btnLeft.setHapticFeedbackEnabled(true);
				DisplayMapsActivity.this.btnLeft.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				DisplayMapsActivity.this.btnLeft.setVisibility(View.INVISIBLE);
				DisplayMapsActivity.this.btnRight.setVisibility(View.VISIBLE);
				DisplayMapsActivity.this.mTextViewBottomLeft.setVisibility(View.INVISIBLE);
				DisplayMapsActivity.this.mTextViewBottomRight.setVisibility(View.VISIBLE);

				mWebView.loadUrl(FILE_PREFIX + Environment.getExternalStorageDirectory() + DIRECTORY + "TestIMG-" + startFloor + startID
						+ PNG);
			} else if (v.getId() == R.id.btnright) {
				DisplayMapsActivity.this.btnRight.setHapticFeedbackEnabled(true);
				DisplayMapsActivity.this.btnRight.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				DisplayMapsActivity.this.btnRight.setVisibility(View.INVISIBLE);
				DisplayMapsActivity.this.btnLeft.setVisibility(View.VISIBLE);
				DisplayMapsActivity.this.mTextViewBottomLeft.setVisibility(View.VISIBLE);
				DisplayMapsActivity.this.mTextViewBottomRight.setVisibility(View.INVISIBLE);

				mWebView.loadUrl(FILE_PREFIX + Environment.getExternalStorageDirectory() + DIRECTORY + "TestIMG-" + endFloor + endID + PNG);
			}
		}

	}
}
