package de.damianbuecker.fhkroutenplaner.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * The Class HistoryActivity.
 */
public class HistoryActivity extends ModifiedViewListActivityImpl {

	/** The m action mode. */
	protected Object mActionMode;

	/** The selected item. */
	public int selectedItem = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (this.getIntent().getStringArrayListExtra("history") != null) {
			if (this.getIntent().getStringArrayListExtra("history").size() == 0) {
				// Abfangen
			} else {
				ArrayList<String> mArrayListString = new ArrayList<String>();
				mArrayListString.addAll(this.getIntent().getStringArrayListExtra("history"));

				ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,
						R.layout.rowlayout, R.id.label, mArrayListString);
				setListAdapter(mArrayAdapter);

				this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
							long id) {
						if (mActionMode != null) {
							return false;
						}
						selectedItem = position;
						mActionMode = HistoryActivity.this.startActionMode(mActionModeCallback);
						view.setSelected(true);
						return true;
					}

				});
			}
		}
	}

	/** The m action mode callback. */
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
			selectedItem = -1;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater mMenuInflater = mode.getMenuInflater();
			mMenuInflater.inflate(R.menu.rowselection, menu);
			return true;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.menuitem1_show:
				show();
				mode.finish();
				return true;

			default:
				return false;
			}
		}
	};

	/**
	 * Show.
	 */
	private void show() {
		Toast.makeText(HistoryActivity.this, String.valueOf(selectedItem), Toast.LENGTH_LONG)
				.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String item = (String) getListAdapter().getItem(position);
		Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	}

}
