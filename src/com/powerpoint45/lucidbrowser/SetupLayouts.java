package com.powerpoint45.lucidbrowser;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetupLayouts extends MainActivity{
	static int actionBarNum;
	
	public static void setuplayouts(){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Properties.ActionbarSize, Properties.ActionbarSize);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		bar.setClickable(true);
		bar.setFocusable(true); 
		bar.setFocusableInTouchMode(true);
		bar.setBackgroundColor(Properties.appProp.actionBarColor);
		bar.getBackground().setAlpha(Properties.appProp.actionBarTransparency);
		setUpActionBar();
		actionBar.setCustomView(bar);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		
		LinearLayout LL = (LinearLayout) inflater.inflate(R.layout.web_sidebar_footer, null);
		if (Properties.sidebarProp.theme.compareTo("w")==0){
			((TextView) LL.findViewById(R.id.browser_open_bookmarks)).setTextColor(Color.BLACK);
			((TextView) LL.findViewById(R.id.browser_home)).setTextColor(Color.BLACK);
			((TextView) LL.findViewById(R.id.browser_share)).setTextColor(Color.BLACK);
			((TextView) LL.findViewById(R.id.browser_set_home)).setTextColor(Color.BLACK);
			((TextView) LL.findViewById(R.id.browser_settings)).setTextColor(Color.BLACK);
		}
		browserListView.addFooterView(LL);
		MainActivity.browserListView.setAdapter(MainActivity.browserListViewAdapter);
		
		if (Properties.sidebarProp.theme.compareTo("b")==0){
			browserListView.setBackgroundColor(Color.argb(Properties.sidebarProp.transparency,17, 17, 17));
		}
		else{
			browserListView.setBackgroundColor(Color.argb(Properties.sidebarProp.transparency,255, 255, 255));
		}

		
		browserListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				// TODOet Auto-generated method stub
				if (pos==webWindows.size()){
					mainView.closeDrawer(browserListView);
					webWindows.add(new CustomWebView(MainActivity.activity,null,null));
					if (webLayout!=null)
						if (((ViewGroup) webLayout.findViewById(R.id.webviewholder))!=null){
							((ViewGroup) webLayout.findViewById(R.id.webviewholder)).removeAllViews();
							((ViewGroup) webLayout.findViewById(R.id.webviewholder)).addView(webWindows.get(pos));
						}
					if (((EditText) bar.findViewById(R.id.browser_searchbar))!=null)
						((EditText) bar.findViewById(R.id.browser_searchbar)).setText("...");
					
				}
				else{
					mainView.closeDrawer(browserListView);
					((ViewGroup) webLayout.findViewById(R.id.webviewholder)).removeAllViews();
					((ViewGroup) webLayout.findViewById(R.id.webviewholder)).addView(webWindows.get(pos));
					if (MainActivity.webLayout.findViewById(R.id.webpgbar)!=null)
						if (webWindows.get(pos).getProgress()!=100)
							MainActivity.webLayout.findViewById(R.id.webpgbar).setVisibility(View.VISIBLE);
						else
							MainActivity.webLayout.findViewById(R.id.webpgbar).setVisibility(View.INVISIBLE);
						
					if (webWindows.get(pos).getUrl()!=null)
						((EditText) bar.findViewById(R.id.browser_searchbar)).setText(webWindows.get(pos).getUrl().replace("http://", "").replace("https://", ""));
					else
						((EditText) bar.findViewById(R.id.browser_searchbar)).setText("...");
				}
				MainActivity.browserListViewAdapter.notifyDataSetChanged();
			}
		   });
		//browserListView.setPadding(0, 0, 0, NavMargine+StatusMargine);
		//browserListView.setY(StatusMargine);
		
		mainView.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int state) {
				// TODO Auto-generated method stub

					final Handler handler = new Handler();
					handler.post(new Runnable() {
					    @Override
					    public void run() {
					    	closeVideoViewIfOpen();
					    }
					});			
			}
			
			@Override
			public void onDrawerSlide(View v, float arg1) {
			}
			
			@Override
			public void onDrawerOpened(View arg0) {
				
			}
			
			@Override
			public void onDrawerClosed(View arg0) {
		}});
		
	}
	            	      
	static public void setUpActionBar(){
			View browserBar = (RelativeLayout) inflater.inflate(R.layout.browser_bar_with_bg, null);
			bar.removeAllViews();
			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Properties.numtodp(3));
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			if (Properties.webpageProp.showBackdrop){
				browserBar = (RelativeLayout) inflater.inflate(R.layout.browser_bar_with_bg, null);
				((ImageButton)browserBar.findViewById(R.id.browser_back)).setColorFilter(Color.DKGRAY, Mode.MULTIPLY);
				((ImageButton)browserBar.findViewById(R.id.browser_forward)).setColorFilter(Color.DKGRAY, Mode.MULTIPLY);
				((ImageButton)browserBar.findViewById(R.id.browser_refresh)).setColorFilter(Color.DKGRAY, Mode.MULTIPLY);
				((ImageButton)browserBar.findViewById(R.id.browser_bookmark)).setColorFilter(Color.DKGRAY, Mode.MULTIPLY);
			}
			else{
				browserBar = (LinearLayout) inflater.inflate(R.layout.browser_bar, null);
				((ImageButton)browserBar.findViewById(R.id.browser_back)).setColorFilter(Properties.appProp.primaryIntColor, Mode.MULTIPLY);
				((ImageButton)browserBar.findViewById(R.id.browser_forward)).setColorFilter(Properties.appProp.primaryIntColor, Mode.MULTIPLY);
				((ImageButton)browserBar.findViewById(R.id.browser_refresh)).setColorFilter(Properties.appProp.primaryIntColor, Mode.MULTIPLY);
				((ImageButton)browserBar.findViewById(R.id.browser_bookmark)).setColorFilter(Properties.appProp.primaryIntColor, Mode.MULTIPLY);
				((EditText)browserBar.findViewById(R.id.browser_searchbar)).setTextColor(Properties.appProp.primaryIntColor);
			}
			
			final EditText ET = ((EditText)browserBar.findViewById(R.id.browser_searchbar));
			ET.setScrollContainer(true);
			
			ET.setOnKeyListener(new OnKeyListener() {
			    public boolean onKey(View v, int keyCode, KeyEvent event) {
			        // If the event is a key-down event on the "enter" button
			        if ((event.getAction() == KeyEvent.ACTION_DOWN)
			                && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			        	new AsyncTask<Void, Void, Void>() {
						    @Override
						    protected void onPostExecute(Void result) {
						        super.onPostExecute(result);
						        imm.hideSoftInputFromWindow(ET.getWindowToken(), 0);
					        	browserSearch();
						    }
							@Override
							protected Void doInBackground(Void... params) {
								return null;
							}
						}.execute();
			            return true;
			        }
			        return false;
			        }
			    });
			
			bar.addView(browserBar);
	}
}
