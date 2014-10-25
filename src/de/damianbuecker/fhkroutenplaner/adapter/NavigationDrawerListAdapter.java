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

public class NavigationDrawerListAdapter extends BaseAdapter{

	private Context mContext;
	private ArrayList<NavigationDrawerItem> mNavigationDrawerItems;
	
	public NavigationDrawerListAdapter(Context mContext, ArrayList<NavigationDrawerItem> mNavigationDrawerItems) {
		this.mContext = mContext;
		this.mNavigationDrawerItems = mNavigationDrawerItems;
	}

	@Override
	public int getCount() {
		return mNavigationDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mNavigationDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

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
