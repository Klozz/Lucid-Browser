package com.powerpoint45.lucidbrowser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class BookmarksActivity extends Activity{

	ListView bookmarksListView;
	public static Context activity; 
	Dialog dialog;
	Dialog editDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmarks);
		activity = this;
		
		bookmarksListView = (ListView) findViewById(R.id.bookmarks_list);
		
		bookmarksListView.setAdapter(new BookmarksListAdapter());
		
		OnItemClickListener clickItemListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				
				String url = MainActivity.mPrefs.getString("bookmark"+pos, "null");
				WebView WV = (WebView) ((ViewGroup) MainActivity.webLayout.findViewById(R.id.webviewholder)).findViewById(R.id.browser_page);
				
				if (WV!=null && url!=null)
					WV.loadUrl(url);
				
				finish();
			}
			
		};
		
		OnItemLongClickListener longClickItemListener = new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					final int pos, long arg3) {
				
				AlertDialog.Builder builder = new AlertDialog.Builder(BookmarksActivity.this);
				
	        	builder.setTitle(MainActivity.mPrefs.getString("bookmarktitle"+pos, "null"));
				
				ListView modeList = new ListView(BookmarksActivity.this);
	        	  String[] stringArray = new String[] {  getResources().getString(R.string.openinnewtab), 
							 getResources().getString(R.string.edit),
							 getResources().getString(R.string.remove)};
	        	  ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(BookmarksActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
	        	  modeList.setAdapter(modeAdapter);
	        	  modeList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int dialogPos, long arg3) {
						
						switch (dialogPos){
						case 0:   //new tab
							MainActivity.openURLInNewTab(MainActivity.mPrefs.getString("bookmark"+pos, "null"));
							finish();
							break;
						case 1:   //edit
							LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
							final LinearLayout editLayout = (LinearLayout) inflater.inflate(R.layout.edit_bookmark_dialog, null);
							((EditText) editLayout.findViewById(R.id.edit_bookmark_title)).setText(MainActivity.mPrefs.getString("bookmarktitle"+pos, "null"));
							
							if (MainActivity.mPrefs.getString("bookmark"+pos, "null").compareTo(MainActivity.assetHomePage)==0)
								((EditText) editLayout.findViewById(R.id.edit_bookmark_url)).setText("about:home");
							else
								((EditText) editLayout.findViewById(R.id.edit_bookmark_url)).setText(MainActivity.mPrefs.getString("bookmark"+pos, "null"));
							
							AlertDialog.Builder builder = new AlertDialog.Builder(activity);
							builder.setTitle(R.string.edit_bookmark)
							.setView(editLayout)
							.setPositiveButton(android.R.string.ok, new OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									String url =((EditText) editLayout.findViewById(R.id.edit_bookmark_url)).getText().toString();
									
									if (url.compareTo("about:home")==0)
										url = MainActivity.assetHomePage;
									
									MainActivity.mPrefs.edit().putString("bookmarktitle"+pos, ((EditText) editLayout.findViewById(R.id.edit_bookmark_title)).getText().toString()).
									putString("bookmark"+pos, url)
									.commit();
									
									
									((BaseAdapter) bookmarksListView.getAdapter()).notifyDataSetChanged();
								}
							})
							.setNegativeButton(android.R.string.cancel, new OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								}
							});

							AlertDialog editDialog = builder.create();
							dialog.dismiss();
							
							
							editDialog.show();
							
							break;
						case 2:   //remove
							removeBookmark(pos);
							dialog.dismiss();
							break;
						
						}
					}
	        	  });
	        	  
	        	  builder.setView(modeList);
	        	  dialog = builder.create();
	        	  dialog.show();	
	        	  System.out.println("LONG PRESSED");
				
				
				return true;
			}
		};
		
		bookmarksListView.setOnItemLongClickListener(longClickItemListener);
		bookmarksListView.setOnItemClickListener(clickItemListener);
		
		Vector<URL> urlsToDownload = new Vector<URL>();
	    
		
		int numURLsToDownload = 0;
		File imageFile = null;
		URL curURL = null;
		int numBookmarks = MainActivity.mPrefs.getInt("numbookmarkedpages", 0);
		
		for (int i = 0; i < numBookmarks ; i++){
			try {
				curURL = new URL(MainActivity.mPrefs.getString("bookmark"+i, "www.google.com"));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (curURL!=null && curURL.getHost().compareTo("")!=0 && curURL.getPath().compareTo(MainActivity.assetHomePage)!=0){
				imageFile = new File(getApplicationInfo().dataDir+"/icons/"+ curURL.getHost());
				if (!imageFile.exists()){
					numURLsToDownload++;
					try {
						urlsToDownload.add(new URL(curURL.getProtocol() + "://" + curURL.getHost() + "/favicon.ico"));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		
		if (numURLsToDownload>0){
			URL arrayURLsToDownload[] = new URL[urlsToDownload.size()];
			
			for (int i = 0; i<urlsToDownload.size(); i++){
				arrayURLsToDownload[i] = urlsToDownload.get(i);
			}
			
			new DownloadFilesTask().execute(arrayURLsToDownload);
		}
		
		
	}
	
	public void removeBookmark(int pos){
		int numBooks=MainActivity.mPrefs.getInt("numbookmarkedpages", 0);
		SharedPreferences.Editor ed = MainActivity.mPrefs.edit();
		ed.putString("bookmark"+pos,MainActivity.mPrefs.getString("bookmark"+(numBooks-1), "null"));
	    ed.putString("bookmarktitle"+pos,MainActivity.mPrefs.getString("bookmarktitle"+(numBooks-1), "null"));
		
	    ed.remove("bookmark"+numBooks);
	    ed.remove("bookmarktitle"+numBooks);
	    ed.putInt("numbookmarkedpages",numBooks-1);
	    ed.commit();
	    ((BaseAdapter) bookmarksListView.getAdapter()).notifyDataSetChanged();
		if (numBooks-1 == 0){
			((Activity) BookmarksActivity.activity).finish();
		}
	}
	
	private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
	    protected Long doInBackground(URL... urls) {
	        int count = urls.length;
	        long totalSize = 0;
	        for (int i = 0; i < count; i++) {
	        	System.out.println("DOWNLOADING INDEX "+i +"AND URL " + urls[i].getHost()+urls[i].getPath());
	        	downloadImage(urls[i]);
	        	publishProgress(0);
	            if (isCancelled()) break;
	        }
	        return totalSize;
	    }

	    protected void onProgressUpdate(Integer... progress) {
	        ((BaseAdapter) bookmarksListView.getAdapter()).notifyDataSetChanged();
	    }

	    protected void onPostExecute(Long result) {
	    	((BaseAdapter) bookmarksListView.getAdapter()).notifyDataSetChanged();
	    }
	}
	
	public void downloadImage(URL url){
		try{
			InputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;
			while (-1!=(n=in.read(buf)))
			{
			   out.write(buf, 0, n);
			}
			out.close();
			in.close();
			byte[] response = out.toByteArray();
			new File(getApplicationInfo().dataDir+"/icons/").mkdirs();
			FileOutputStream fos = new FileOutputStream(getApplicationInfo().dataDir+"/icons/"+ url.getHost());
			fos.write(response);
			fos.close();
		}catch(Exception e){};
	}

}


