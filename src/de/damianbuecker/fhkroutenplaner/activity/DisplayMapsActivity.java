package de.damianbuecker.fhkroutenplaner.activity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * The Class DisplayMapsActivity.
 */
@ContentView(R.layout.display_maps_activity)
public class DisplayMapsActivity extends ModifiedViewActivityImpl {
	
	/** The list items. */
	private String[] listItems = {"Test"};
	
	/** The m drawer layout. */
	@InjectView(R.id.drawer_layout)
	private DrawerLayout mDrawerLayout;

	/** The m drawer list. */
	@InjectView(R.id.left_drawer)
	private ListView mDrawerList;
	
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
	
	/* (non-Javadoc)
	 * @see roboguice.activity.RoboActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Import listItems here
		this.mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, listItems));
		this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		this.btnToggleDrawer.setOnClickListener(new ButtonDrawerToggleListener());
		this.btnLeft.setOnClickListener(new ButtonLeftRightListener());
		this.btnRight.setOnClickListener(new ButtonLeftRightListener());
	}
	
	/**
	 * Select item.
	 *
	 * @param position the position
	 */
	private void selectItem(int position) {
		Intent intent = new Intent(this, NavigationActivity.class);
		startActivity(intent);
		/**
		 * Cleanup here
		 */
	}
	
	/**
	 * The listener interface for receiving drawerItemClick events.
	 * The class that is interested in processing a drawerItemClick
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addDrawerItemClickListener<code> method. When
	 * the drawerItemClick event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see DrawerItemClickEvent
	 */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		/* (non-Javadoc)
		 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
		 */
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}
	
	/**
	 * The listener interface for receiving buttonDrawerToggle events.
	 * The class that is interested in processing a buttonDrawerToggle
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addButtonDrawerToggleListener<code> method. When
	 * the buttonDrawerToggle event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ButtonDrawerToggleEvent
	 */
	private class ButtonDrawerToggleListener implements View.OnClickListener {

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			if(DisplayMapsActivity.this.mDrawerLayout.isDrawerOpen(DisplayMapsActivity.this.mListViewDrawer)) {
				DisplayMapsActivity.this.mDrawerLayout.closeDrawer(DisplayMapsActivity.this.mListViewDrawer);
			} else {
				DisplayMapsActivity.this.mDrawerLayout.openDrawer(DisplayMapsActivity.this.mListViewDrawer);
			}
		}
	}
	
	/**
	 * The listener interface for receiving buttonLeftRight events.
	 * The class that is interested in processing a buttonLeftRight
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addButtonLeftRightListener<code> method. When
	 * the buttonLeftRight event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ButtonLeftRightEvent
	 */
	private class ButtonLeftRightListener implements View.OnClickListener {

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.btnleft) {
				DisplayMapsActivity.this.btnLeft.setHapticFeedbackEnabled(true);
				DisplayMapsActivity.this.btnLeft.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				DisplayMapsActivity.this.btnLeft.setVisibility(View.INVISIBLE);
				DisplayMapsActivity.this.btnRight.setVisibility(View.VISIBLE);
			} else if(v.getId() == R.id.btnright) {
				DisplayMapsActivity.this.btnRight.setHapticFeedbackEnabled(true);
				DisplayMapsActivity.this.btnRight.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				DisplayMapsActivity.this.btnRight.setVisibility(View.INVISIBLE);
				DisplayMapsActivity.this.btnLeft.setVisibility(View.VISIBLE);
			}
		}
	}
}
