package com.powerpoint45.lucidbrowser;

import java.util.List;
import java.util.Vector;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static Activity           activity;
	public static Context            ctxt;
	public static SharedPreferences  mPrefs;
	public static SharedPreferences  mGlobalPrefs;
	static LayoutInflater            inflater;
	public static InputMethodManager imm;
	NotificationManager              mNotificationManager;
	
	public static RelativeLayout       bar;
	public static ActionBar            actionBar;
	public static SystemBarTintManager tintManager;
	static FrameLayout                 contentView;
	public static DrawerLayout         mainView; 
	
	public static LinearLayout        webLayout;
	public static ListView            browserListView;
	public static BrowserImageAdapter browserListViewAdapter;
	static Vector <CustomWebView>     webWindows;
	
	static Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity     = this;
		ctxt         = getApplicationContext();
		mPrefs       = getSharedPreferences("pref",0);
		mGlobalPrefs = PreferenceManager.getDefaultSharedPreferences(ctxt);
		inflater     = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		imm          = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		
		bar                       = new RelativeLayout(this);
		actionBar                 = getActionBar();
		mainView                  = (DrawerLayout) inflater.inflate(R.layout.main, null);
		contentView               = ((FrameLayout)mainView.findViewById(R.id.content_frame));
		
		
		webLayout                 = (LinearLayout) inflater.inflate(R.layout.page_web, null);
		browserListView           = (ListView) mainView.findViewById(R.id.right_drawer);
		browserListViewAdapter    = new BrowserImageAdapter(this);
		webWindows                = new Vector<CustomWebView>();
		
		Properties.update_preferences();
		SetupLayouts.setuplayouts();
		
		
		if (Properties.appProp.fullscreen)
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		if (Properties.appProp.systemPersistent){
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
					.setOngoing(true)
					.setPriority(2)
			        .setContentTitle(getResources().getString(R.string.app_name));
			mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(1, mBuilder.build());
		}
		
		
		contentView.addView(webLayout);
		setContentView(mainView);

		
	}
	
	
	public static boolean isDownloadManagerAvailable(Context context) {
	    try {
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
	        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
	                PackageManager.MATCH_DEFAULT_ONLY);
	        return list.size() > 0;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	public static void browserSearch(){
		if (webWindows.size()==0){
			webWindows.add(new CustomWebView(MainActivity.activity,null));
			((ViewGroup) webLayout.findViewById(R.id.webviewholder)).removeAllViews();
			((ViewGroup) webLayout.findViewById(R.id.webviewholder)).addView(webWindows.get(0));
		}
		
		CustomWebView WV = (CustomWebView) webLayout.findViewById(R.id.browser_page);
		
		WV.stopLoading();
		if (SetupLayouts.actionBarNum==2)
			((EditText)bar.findViewById(R.id.browser_searchbar)).clearFocus();
		String q = ((EditText)bar.findViewById(R.id.browser_searchbar)).getText().toString();
		if (q.contains(".") && !q.contains(" ")){
			if (q.startsWith("http://")||q.startsWith("https://"))
				WV.loadUrl(q);
			else if (q.startsWith("www."))
				WV.loadUrl("http://"+q);
			else
				WV.loadUrl("http://"+q);
		}
		else if (q.startsWith("about:")||q.startsWith("file:"))
			WV.loadUrl(q);
		else
			WV.loadUrl("http://www.google.com/#q="+q);
	}
	
	public void browserActionClicked(View v){
		Handler handler=new Handler();
 		Runnable r=new Runnable(){
 		    public void run() {
 		    	mainView.closeDrawers();
 		    }
 		}; 
 		handler.postDelayed(r, 500);	
		
		if (webWindows.size()==0){
			webWindows.add(new CustomWebView(MainActivity.this,null));
			((ViewGroup) webLayout.findViewById(R.id.webviewholder)).removeAllViews();
			((ViewGroup) webLayout.findViewById(R.id.webviewholder)).addView(webWindows.get(0));
		}
		
		Message msg = new Message();
		CustomWebView WV = (CustomWebView) webLayout.findViewById(R.id.browser_page);
		switch (v.getId()){
		case R.id.browser_home:
			WV.loadUrl(mPrefs.getString("browserhome", "http://www.google.com/"));
			WV.clearHistory();
			break;
		case R.id.browser_share:
			Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
		    sharingIntent.setType("text/plain");
		    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Link");
		    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, WV.getUrl());
		    startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share)));
			break;
		case R.id.browser_back:
			WV.goBack();
			break;
		case R.id.browser_forward:
			WV.goForward();
			break;
		case R.id.browser_refresh:
			if (WV.getProgress()!=100)
				WV.stopLoading();
			else
				WV.reload();
			break;
		case R.id.browser_bookmark:
			SharedPreferences.Editor ed = mPrefs.edit();
			ImageButton BI = (ImageButton) MainActivity.bar.findViewById(R.id.browser_bookmark);
			int numBooks=MainActivity.mPrefs.getInt("numbookmarkedpages", 0);
			boolean isBook = false;
			int markedBook = 0;
			for (int i=0;i<numBooks;i++){
				if (WV!=null)
					if (WV.getUrl()!=null)
	    				if (MainActivity.mPrefs.getString("bookmark"+i, "").compareTo(WV.getUrl())==0){
	    					BI.setImageResource(R.drawable.btn_omnibox_bookmark_selected_normal);
	    					isBook=true;
	    					markedBook = i;
	    					break;
	    				}
			}
			if (isBook){
				BI.setImageResource(R.drawable.btn_omnibox_bookmark_normal);
				if ((numBooks-1)!=markedBook){
					ed.putString("bookmark"+markedBook,mPrefs.getString("bookmark"+(numBooks-1), "null"));
				    ed.putString("bookmarktitle"+markedBook,mPrefs.getString("bookmarktitle"+(numBooks-1), "null"));
				    ed.commit();
				}
				ed.remove("bookmark"+markedBook);
			    ed.remove("bookmarktitle"+markedBook);
			    ed.putInt("numbookmarkedpages",numBooks-1);
			    ed.commit();
				
			    if (BI!=null)
	    			BI.setImageResource(R.drawable.btn_omnibox_bookmark_normal);
			}else{
				ed.putString("bookmark"+numBooks,WV.getUrl());
			    ed.putString("bookmarktitle"+numBooks,WV.getTitle());
			    ed.putInt("numbookmarkedpages",numBooks+1);
			    ed.commit();
			    if (BI!=null)
	    			BI.setImageResource(R.drawable.btn_omnibox_bookmark_selected_normal);
			}
            break;
		case R.id.browser_open_bookmarks:
     	    msg.what = 3;
            messageHandler.sendMessage(msg);
			break;
		case R.id.browser_set_home:
			mPrefs.edit().putString("browserhome", WV.getUrl()).commit();
			msg.obj=WV.getTitle()+" set";
			msg.what = 1;
            messageHandler.sendMessage(msg);
	    	 break;
	    	 
		case R.id.browser_settings:
			startActivity(new Intent(ctxt,SettingsV2.class));
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		}
	}
	
	public void closeCurrentTab(View v){
		int pos = (Integer) v.getTag();
			
		if (webLayout.findViewById(R.id.browser_page)==webWindows.get(pos)){
			if ((pos-1)>=0){
				((ViewGroup) webLayout.findViewById(R.id.webviewholder)).removeAllViews();
				((ViewGroup) webLayout.findViewById(R.id.webviewholder)).addView(webWindows.get(pos-1));
				if (((TextView) bar.findViewById(R.id.browser_searchbar))!=null)
					((TextView) bar.findViewById(R.id.browser_searchbar)).setText(webWindows.get(pos-1).getUrl().replace("http://", "").replace("https://", ""));
			}
			else{
				((ViewGroup) webLayout.findViewById(R.id.webviewholder)).removeAllViews();
				if (((TextView) bar.findViewById(R.id.browser_searchbar))!=null)
					((TextView) bar.findViewById(R.id.browser_searchbar)).setText("");
				ImageView IV = new ImageView(ctxt);
				IV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
				IV.setScaleType(ImageView.ScaleType.CENTER);
				IV.setImageResource(R.drawable.web_logo);
				((ViewGroup) webLayout.findViewById(R.id.webviewholder)).addView(IV);
			}
		}
		webWindows.get(pos).loadUrl("about:blank");
		webWindows.remove(pos);
		browserListViewAdapter.notifyDataSetChanged();
	}
	
	public void bookmarkActionClicked(View v){
		WebView WV = (WebView) ((ViewGroup) webLayout.findViewById(R.id.webviewholder)).findViewById(R.id.browser_page);
		String curURL = ((View) v.getParent()).getTag().toString();
		switch (v.getId()){
		case R.id.bookmark_title:
			WV.loadUrl(curURL);
			dismissDialog();
			break;
		case R.id.bookmark_delete:
			int curItem=0;
			int numBooks=mPrefs.getInt("numbookmarkedpages", 0);
			for (int I=0;I<numBooks;I++){
				if (curURL.compareTo(mPrefs.getString("bookmark"+I, ""))==0){
					curItem=I;
					break;
				}
			}
			SharedPreferences.Editor ed = mPrefs.edit();
			ed.putString("bookmark"+curItem,mPrefs.getString("bookmark"+(numBooks-1), "null"));
		    ed.putString("bookmarktitle"+curItem,mPrefs.getString("bookmarktitle"+(numBooks-1), "null"));
			
		    ed.remove("bookmark"+numBooks);
		    ed.remove("bookmarktitle"+numBooks);
		    ed.putInt("numbookmarkedpages",numBooks-1);
		    ed.commit();
			v.setVisibility(View.GONE);
			break;
		}
	}
	
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) !=
                Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) {
        	mainView.closeDrawers();
        }
        
        if (intent.getAction()==Intent.ACTION_WEB_SEARCH ||intent.getAction()==Intent.ACTION_VIEW){
        	if (intent.getAction()==Intent.ACTION_WEB_SEARCH ||intent.getAction()==Intent.ACTION_VIEW){
            	if (!Properties.webpageProp.disable){
    	        	if (webWindows.size()==0){
    	    			webWindows.add(new CustomWebView(MainActivity.this,null));
    	    			((ViewGroup) webLayout.findViewById(R.id.webviewholder)).removeAllViews();
    	    			((ViewGroup) webLayout.findViewById(R.id.webviewholder)).addView(webWindows.get(0));
    	    			((EditText) bar.findViewById(R.id.browser_searchbar)).setText("...");
    	    		}
    	    		CustomWebView WV = (CustomWebView) webLayout.findViewById(R.id.browser_page);
    	    		WV.stopLoading();
    	    		WV.loadUrl(intent.getDataString());
            	}
            }
        }
	}
	
	
	public static void closeVideoViewIfOpen(){
		try{
			CustomWebView WV = (CustomWebView) mainView.findViewById(R.id.web_holder).findViewById(R.id.browser_page);
			if (WV!=null)
				if (WV.isVideoPlaying())
					WV.getChromeClient().onHideCustomView();
		}catch(Exception e){};
	}
	
	
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
			if (!mainView.isDrawerOpen(browserListView))
				mainView.openDrawer(browserListView);
			else
				mainView.closeDrawer(browserListView);
			return true;
        }
	    return false;
	};
	
    static void dismissDialog(){
   	 if (dialog!=null){
   		 dialog.dismiss();
   		 dialog=null;
   	 }
    }
    
    
    
	protected static Handler messageHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 3) {//bookmark dialog
            	dialog = new Dialog(activity);
				dialog.setTitle("Bookmarks");
				ListView lv = new ListView(activity);
				lv.setAdapter(new BookmarksListAdapter());
				dialog.setContentView(lv);
				dialog.show();
            }
        }
 };
 
 


}
