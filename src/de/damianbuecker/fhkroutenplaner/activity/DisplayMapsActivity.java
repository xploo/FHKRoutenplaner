package de.damianbuecker.fhkroutenplaner.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.Point;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebView.PictureListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.adapter.NavigationDrawerListAdapter;
import de.damianbuecker.fhkroutenplaner.controller.FileController;
import de.damianbuecker.fhkroutenplaner.controller.ImageController;
import de.damianbuecker.fhkroutenplaner.controller.NfcController;
import de.damianbuecker.fhkroutenplaner.controller.SharedPreferencesController;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;
import de.damianbuecker.fhkroutenplaner.model.NavigationDrawerItem;

/**
 * The Class DisplayMapsActivity.
 */
@SuppressWarnings("deprecation")
@ContentView(R.layout.display_maps_activity)
public class DisplayMapsActivity extends ModifiedViewActivityImpl {

	/** The list items. */
	private String[] listItems;/** = {NEW_ROUTE, QUIT_ROUTE};*/
	
	private TypedArray mNavigationMenuIcons;
	private ArrayList<NavigationDrawerItem> mNavigationDrawerItems;
	private NavigationDrawerListAdapter mNavigationDrawerListAdapter;

	/** The local drawer layout. */
	@InjectView(R.id.drawer_layout)
	private DrawerLayout mDrawerLayout;

	/** The local drawer list. */
	@InjectView(R.id.left_drawer)
	private ListView mDrawerList;

	/** The local text view bottom left. */
	@InjectView(R.id.textViewBottomleft)
	private TextView mTextViewBottomLeft;

	/** The local text view bottom right. */
	@InjectView(R.id.textViewBottomright)
	private TextView mTextViewBottomRight;

	/** The button toggle drawer. */
	@InjectView(R.id.btntoggledrawer)
	private Button btnToggleDrawer;

	/** The local list view drawer. */
	@InjectView(R.id.left_drawer)
	private ListView mListViewDrawer;

	/** The button left. */
	@InjectView(R.id.btnleft)
	private Button btnLeft;

	/** The button right. */
	@InjectView(R.id.btnright)
	private Button btnRight;

	/** The local web view. */
	@InjectView(R.id.webView)
	private WebView mWebView;

	/** The local nfc adapter. */
	private NfcAdapter mNfcAdapter;

	/** The local nfc controller. */
	private NfcController mNFCController;

	/** The local image controller. */
	private ImageController mImageController;
	
	/** The local databasehelper. */
	private DatabaseHelper mDatabasehelper;

	/** The local file controller. */
	private FileController mFileController;

	/** The local shared preferences controller. */
	private SharedPreferencesController mSharedPreferencesController;

	/** The progress dialog. */
	private ProgressDialog mProgressDialog;
	
	/** The Constant progress_bar_type. */
	public static final int progress_bar_type = 0;
	
	/** The x pos_start. */
	private Double xPos_start;
	
	/** The y pos_start. */
	private Double yPos_start;

	/** The x pos. */
	private Double xPos;
	
	/** The y pos. */
	private Double yPos;

	/** The end id. */
	private Integer endID;
	
	/** The start id. */
	private Integer startID;
	
	/** The start floor. */
	private Integer startFloor;
	
	/** The end floor. */
	private Integer endFloor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** Creating the navigation drawer design. */
		this.listItems = getResources().getStringArray(R.array.nav_drawer_items);
		this.mNavigationMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		this.mNavigationDrawerItems = new ArrayList<NavigationDrawerItem>();
		this.mNavigationDrawerItems.add(new NavigationDrawerItem(this.listItems[0], this.mNavigationMenuIcons.getResourceId(0, -1)));
		this.mNavigationDrawerItems.add(new NavigationDrawerItem(this.listItems[1], this.mNavigationMenuIcons.getResourceId(1, -1)));
		this.mNavigationMenuIcons.recycle();
		
		this.mNavigationDrawerListAdapter = new NavigationDrawerListAdapter(this, this.mNavigationDrawerItems);
		this.mDrawerList.setAdapter(this.mNavigationDrawerListAdapter);
		
		Toast.makeText(this, ROUTE_IS_BEING_CALCULATED, Toast.LENGTH_LONG).show();
		
//		this.mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listItems));
		
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
					this.endID = Integer.parseInt(this.getIntent().getExtras().getString(END_ID));
				} else {
					this.endID = Integer.parseInt(this.mSharedPreferencesController.getString(SHARED_PREFERENCE_LAST_DESTINATION));
				}
				this.startID = Integer.parseInt(this.getIntent().getExtras().getString(INTENT_EXTRA_START_ID));

				this.startFloor = Integer.parseInt(this.getIntent().getExtras().getString(INTENT_EXTRA_START_FLOOR));
			}
		}

		this.endFloor = mImageController.getEndFloor(endID);
		new calculateRoute().execute((String)null);

		try {			
			List<Tag> listTag = this.mDatabasehelper.getTagById(String.valueOf(startID));			
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			this.xPos = this.xPos_start = (listTag.get(0).getX_pos()*2) - (width/2);
			this.yPos = this.yPos_start = (listTag.get(0).getY_pos()*2) - (height/2);
		
			 
		} catch (SQLException e) {
			this.logMessage(ERROR, e.getLocalizedMessage());
		}		
		

		if (startFloor == endFloor) {
			this.btnLeft.setVisibility(View.INVISIBLE);
			this.btnRight.setVisibility(View.INVISIBLE);
			this.mTextViewBottomLeft.setVisibility(View.INVISIBLE);
			this.mTextViewBottomRight.setVisibility(View.INVISIBLE);
		}

		btnLeft.setVisibility(View.INVISIBLE);
		mTextViewBottomLeft.setVisibility(View.INVISIBLE);

		mTextViewBottomLeft.setText(TO_STARTING_FLOOR + WHITESPACE + startFloor);
		mTextViewBottomRight.setText(TO_DESTINATION_FLOOR + WHITESPACE + endFloor);
		
		
		//this.mWebView.loadUrl(FILE_PREFIX + Environment.getExternalStorageDirectory() + DIRECTORY + "TestIMG-" + startFloor + startID + PNG);

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
		
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(ROUTE_IS_BEING_CALCULATED);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}
	
	
	/**
	 * The Class calculateRoute.
	 */
	private class calculateRoute extends AsyncTask<String,String,String>{
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		protected void onPreExecute() {
            super.onPreExecute();            
            showDialog(progress_bar_type);
        }

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected String doInBackground(String... params) {
			mImageController.testAlgorithm(startFloor, startID, endID, endFloor);
			// TODO Auto-generated method stub
			return null;
		}
		
		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(String values){
			
			dismissDialog(progress_bar_type);
			mWebView.loadUrl(FILE_PREFIX + Environment.getExternalStorageDirectory() + DIRECTORY + FILENAME + startFloor + startID + PNG);
		}
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
				DisplayMapsActivity.this.xPos = DisplayMapsActivity.this.xPos_start;
				DisplayMapsActivity.this.yPos = DisplayMapsActivity.this.yPos_start;				
				DisplayMapsActivity.this.btnLeft.setHapticFeedbackEnabled(true);
				DisplayMapsActivity.this.btnLeft.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				DisplayMapsActivity.this.btnLeft.setVisibility(View.INVISIBLE);
				DisplayMapsActivity.this.btnRight.setVisibility(View.VISIBLE);
				DisplayMapsActivity.this.mTextViewBottomLeft.setVisibility(View.INVISIBLE);
				DisplayMapsActivity.this.mTextViewBottomRight.setVisibility(View.VISIBLE);

				mWebView.loadUrl(FILE_PREFIX + Environment.getExternalStorageDirectory() + DIRECTORY + FILENAME + startFloor + startID
						+ PNG);
			} else if (v.getId() == R.id.btnright) {				
				DisplayMapsActivity.this.xPos = (double) mWebView.getScrollX();
				DisplayMapsActivity.this.yPos = (double) mWebView.getScrollY();	
				DisplayMapsActivity.this.btnRight.setHapticFeedbackEnabled(true);
				DisplayMapsActivity.this.btnRight.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				DisplayMapsActivity.this.btnRight.setVisibility(View.INVISIBLE);
				DisplayMapsActivity.this.btnLeft.setVisibility(View.VISIBLE);
				DisplayMapsActivity.this.mTextViewBottomLeft.setVisibility(View.VISIBLE);
				DisplayMapsActivity.this.mTextViewBottomRight.setVisibility(View.INVISIBLE);

				mWebView.loadUrl(FILE_PREFIX + Environment.getExternalStorageDirectory() + DIRECTORY + FILENAME + endFloor + endID + PNG);
			}
		}

	}
}
