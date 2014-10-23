package de.damianbuecker.fhkroutenplaner.activity;

import java.util.ArrayList;
import java.util.List;

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
import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

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

				HistoryItemArrayAdapter mArrayAdapter = new HistoryItemArrayAdapter(this, R.layout.rowlayout, historyItemsList);
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
		Toast.makeText(HistoryActivity.this, String.valueOf(selectedItem), Toast.LENGTH_LONG).show();
	}

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
		 * @param context the context
		 * @param resource the resource
		 * @param items the items
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
