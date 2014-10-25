package de.damianbuecker.fhkroutenplaner.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

// TODO: Auto-generated Javadoc
/**
 * The Class HistoryActivity.
 */
public class HistoryActivity extends ModifiedViewListActivityImpl {

	/** The m action mode. */
	protected Object mActionMode;

	/** The selected item. */
	public int selectedItem = -1;

	/** The Constant INTENT_EXTRA_HISTORY. */
	private static final String INTENT_EXTRA_HISTORY = "history";
	
	/** The Constant INTENT_EXTRA_SELECTED_ITEM. */
	private static final String INTENT_EXTRA_SELECTED_ITEM = "selectedItem";
	
	/** The database helper. */
	private DatabaseHelper databaseHelper;
	
	/** The m array adapter. */
	HistoryItemArrayAdapter mArrayAdapter;
	


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (this.getIntent().getStringArrayListExtra(INTENT_EXTRA_HISTORY) != null) {
			if (this.getIntent().getStringArrayListExtra(INTENT_EXTRA_HISTORY).size() == 0) {				
			} else {
				ArrayList<String> jsonArray = this.getIntent().getStringArrayListExtra(INTENT_EXTRA_HISTORY);
				ArrayList<HistoryItem> historyItemsList = new ArrayList<HistoryItem>();
				for (String s : jsonArray) {
					HistoryItem h = new HistoryItem();
					h = h.fromJson(s);
					historyItemsList.add(h);
				}
				if (this.databaseHelper == null) {
					this.databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
				}

				this.mArrayAdapter = new HistoryItemArrayAdapter(this, R.layout.rowlayout, historyItemsList);
				setListAdapter(mArrayAdapter);
				
				this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
	
	/**
	 * Delete item from list.
	 * Delete a item from SQLite Database
	 * @param position position in arrayAdapter
	 */
	protected void deleteItemFromList(int position) {
		if(this.mArrayAdapter != null) {
			HistoryItem mHistoryItem = this.mArrayAdapter.getItem(position);
			this.mArrayAdapter.remove(this.mArrayAdapter.getItem(position));
			try {
				HistoryActivity.this.databaseHelper.deleteHistoryItemById(mHistoryItem.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
				HistoryActivity.this.deleteItemFromList(selectedItem);
				mode.finish();
				return true;

			default:
				return false;
			}
		}
	};	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		HistoryItem item = (HistoryItem) getListAdapter().getItem(position);

		Intent intent = new Intent(this, HistoryItemDetailActivity.class);
		intent.putExtra(INTENT_EXTRA_SELECTED_ITEM, item.toJson(item));
		startActivity(intent);
	}

	/**
	 * The Class HistoryItemArrayAdapter.
	 */
	public class HistoryItemArrayAdapter extends ArrayAdapter<HistoryItem> {

		/** The resource. */
		int resource;

		/**
		 * Instantiates a new history item array adapter.
		 *
		 * @param context  context
		 * @param resource the resource
		 * @param items HistoryItem-objects
		 */
		public HistoryItemArrayAdapter(Context context, int resource, List<HistoryItem> items) {
			super(context, resource, items);
			this.resource = resource;
		}

		/* (non-Javadoc)
		 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LinearLayout linearLayout;
			HistoryItem historyItem = getItem(position);

			if (convertView == null) {
				linearLayout = new LinearLayout(getContext());
				LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				layoutInflater.inflate(resource, linearLayout, true);
			} else {
				linearLayout = (LinearLayout) convertView;
			}
			TextView text = (TextView) linearLayout.findViewById(R.id.label);
			text.setText(historyItem.getName());


			return linearLayout;
		}

	}

}
