package de.damianbuecker.fhkroutenplaner.activity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

@ContentView(R.layout.display_maps_activity)
public class DisplayMapsActivity extends ModifiedViewActivityImpl {
	
	private String[] listItems = {"Test"};
	
	@InjectView(R.id.drawer_layout)
	private DrawerLayout mDrawerLayout;

	@InjectView(R.id.left_drawer)
	private ListView mDrawerList;
	
	@InjectView(R.id.btntoggledrawer)
	private Button btnToggleDrawer;
	
	@InjectView(R.id.left_drawer)
	private ListView mListViewDrawer;
	
	@InjectView(R.id.btnleft)
	private Button btnLeft;
	
	@InjectView(R.id.btnright)
	private Button btnRight;

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
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}
	
	private void selectItem(int position) {
		Intent intent = new Intent(this, NavigationActivity.class);
		startActivity(intent);
		/**
		 * Cleanup here
		 */
	}
	
	private class ButtonDrawerToggleListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if(DisplayMapsActivity.this.mDrawerLayout.isDrawerOpen(DisplayMapsActivity.this.mListViewDrawer)) {
				DisplayMapsActivity.this.mDrawerLayout.closeDrawer(DisplayMapsActivity.this.mListViewDrawer);
			} else {
				DisplayMapsActivity.this.mDrawerLayout.openDrawer(DisplayMapsActivity.this.mListViewDrawer);
			}
		}
	}
	
	private class ButtonLeftRightListener implements View.OnClickListener {

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
