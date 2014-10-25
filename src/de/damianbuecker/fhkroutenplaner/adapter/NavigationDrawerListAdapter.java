package de.damianbuecker.fhkroutenplaner.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.damianbuecker.fhkroutenplaner.activity.R;
import de.damianbuecker.fhkroutenplaner.model.NavigationDrawerItem;

// TODO: Auto-generated Javadoc
/**
 * The Class NavigationDrawerListAdapter.
 */
public class NavigationDrawerListAdapter extends BaseAdapter{

	/** The m context. */
	private Context mContext;
	
	/** The m navigation drawer items. */
	private ArrayList<NavigationDrawerItem> mNavigationDrawerItems;
	
	/**
	 * Instantiates a new navigation drawer list adapter.
	 *
	 * @param mContext the m context
	 * @param mNavigationDrawerItems the m navigation drawer items
	 */
	public NavigationDrawerListAdapter(Context mContext, ArrayList<NavigationDrawerItem> mNavigationDrawerItems) {
		this.mContext = mContext;
		this.mNavigationDrawerItems = mNavigationDrawerItems;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return mNavigationDrawerItems.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return mNavigationDrawerItems.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) this.mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}
		
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
		
		imgIcon.setImageResource(this.mNavigationDrawerItems.get(position).getIcon());
		txtTitle.setText(this.mNavigationDrawerItems.get(position).getTitle());
		
		return convertView;
	}

}
