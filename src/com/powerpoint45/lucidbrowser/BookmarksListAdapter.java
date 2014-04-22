package com.powerpoint45.lucidbrowser;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookmarksListAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return MainActivity.mPrefs.getInt("numbookmarkedpages", 0);
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		RelativeLayout RL = (RelativeLayout) MainActivity.inflater.inflate(R.layout.bookmark_item, null);
		((TextView) RL.findViewById(R.id.bookmark_title)).setText(MainActivity.mPrefs.getString("bookmarktitle"+arg0, "null"));
		RL.setTag(MainActivity.mPrefs.getString("bookmark"+arg0, "www.google.com"));
		return RL;
	}

}
