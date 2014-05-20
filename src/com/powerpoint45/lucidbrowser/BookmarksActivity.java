package com.powerpoint45.lucidbrowser;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class BookmarksActivity extends Activity{

	ListView bookmarksListView;
	public static Context activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmarks);
		activity = this;
		
		bookmarksListView = (ListView) findViewById(R.id.bookmarks_list);
		
		bookmarksListView.setAdapter(new BookmarksListAdapter());
		
		
	}

}
