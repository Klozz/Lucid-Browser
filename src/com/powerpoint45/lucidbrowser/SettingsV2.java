package com.powerpoint45.lucidbrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsV2 extends PreferenceActivity {
	SharedPreferences globalPref;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalPref = PreferenceManager.getDefaultSharedPreferences(SettingsV2.this);
        MainActivity.activity =this;
        addPreferencesFromResource(R.xml.settings_v2);
        
        ((Preference) findPreference("reset")).setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
	            	SharedPreferences.Editor ed = globalPref.edit();
					ed.clear();
					ed.commit();
					Toast.makeText(getApplicationContext(),
							(getResources().getText(R.string.complete)), Toast.LENGTH_LONG).show();
					return false;
            }
      });
       
        ((Preference) findPreference("clearbrowsercache")).setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	DeleteRecursive(new File(getApplicationInfo().dataDir+"/app_webview/Cache/"));
            	DeleteRecursive(new File(getApplicationInfo().dataDir+"/cache/"));
            	Toast.makeText(getApplicationContext(),
			               (getResources().getText(R.string.complete)), Toast.LENGTH_LONG).show();
				return false;
            }
      });
        
      ((Preference) findPreference("clearbrowserhistory")).setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	DeleteRecursive(new File(getApplicationInfo().dataDir+"/databases/webview.db"));
            	DeleteRecursive(new File(getApplicationInfo().dataDir+"/databases/webview.db-shm"));
            	DeleteRecursive(new File(getApplicationInfo().dataDir+"/databases/webview.db-wal"));
            	DeleteRecursive(new File(getApplicationInfo().dataDir+"/app_webview/Web Data"));
            	DeleteRecursive(new File(getApplicationInfo().dataDir+"/app_webview/Web Data-journal"));
            	Toast.makeText(getApplicationContext(),
			               (getResources().getText(R.string.complete)), Toast.LENGTH_LONG).show();
				return false;
            }
      });
        
      ((Preference) findPreference("clearbrowsercookies")).setOnPreferenceClickListener(new OnPreferenceClickListener() {
          public boolean onPreferenceClick(Preference preference) {
          	DeleteRecursive(new File(getApplicationInfo().dataDir+"/databases/webviewCookiesChromium.db"));
          	DeleteRecursive(new File(getApplicationInfo().dataDir+"/databases/webviewCookiesChromiumPrivate.db"));
          	DeleteRecursive(new File(getApplicationInfo().dataDir+"/app_webview/Cookies"));
          	DeleteRecursive(new File(getApplicationInfo().dataDir+"/app_webview/Cookies-journal"));
          	Toast.makeText(getApplicationContext(),
		               (getResources().getText(R.string.complete)), Toast.LENGTH_LONG).show();
				return false;
          }
    });
      
      
     
      
        
        //if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { // Translucent available
        int id = getResources().getIdentifier("config_enableTranslucentDecor", "bool", "android");
        if (id == 0) {
        	try{
            	PreferenceScreen mCategory = (PreferenceScreen) findPreference("mainsettings");
            	mCategory.removePreference(((Preference) findPreference("transparentnav")));
            	mCategory.removePreference(((Preference) findPreference("transparentstatus")));
            	}catch(Exception e){};
        } 

    }
    
 
    
    void DeleteRecursive(File fileOrDirectory) {
    	if (fileOrDirectory.exists()){
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);

        fileOrDirectory.delete();
    	}
    }
    
    
    @Override
	public void onBackPressed(){
    	super.onBackPressed();
    	startActivity(new Intent(SettingsV2.this,MainActivity.class));
    }
	
    @Override
    public void onStop(){
    	super.onStop();
    	finish();
    }
    

}