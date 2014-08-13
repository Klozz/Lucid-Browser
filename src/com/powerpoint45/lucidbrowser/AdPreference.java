package com.powerpoint45.lucidbrowser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AdPreference extends LinearLayout {
 

 
    public AdPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		SharedPreferences globalPref = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		if (!globalPref.getBoolean("disableads", false))
			addView(getAdView());
	}

	public AdPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		SharedPreferences globalPref = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		if (!globalPref.getBoolean("disableads", false))
			addView(getAdView());
	}

	public AdPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		SharedPreferences globalPref = PreferenceManager
				.getDefaultSharedPreferences(getContext());
		if (!globalPref.getBoolean("disableads", false))
			addView(getAdView());
	}

	//your ad id goes here
	final String AD_UNIT_ID = "ca-app-pub-XXXXXXXXXX/XXXXXXXXXX";
    
    public AdView getAdView() {
        // the context is a PreferenceActivity 
        Activity activity = (Activity)getContext();
 
        // Create the adView 
        AdView adView = new AdView(activity);
 
        // Initiate a generic request to load it with an ad 
        
        
		adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId(AD_UNIT_ID);		

		
		AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .addTestDevice("4C7737FB5E1CF1C791654654891A4803")//MOTEROLA
        .addTestDevice("A90A1B37090BFFE40F3EC77212CC8E45")//KINDLE
        .addTestDevice("B2E17AC6E84F2EC84F8FF602FAC67470")//SAMSUNG
        .build(); 

	    adView.loadAd(adRequest);
 
        return adView;    
    } 
} 