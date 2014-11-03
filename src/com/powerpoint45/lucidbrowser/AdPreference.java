package com.powerpoint45.lucidbrowser;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amazon.device.ads.Ad;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.InterstitialAd;

public class AdPreference extends Object {
	
	//your ad id goes here
	//ca-app-pub-XXXXXXXXXXXXXXXXXX/XXXXXXXXXX
	final String AD_UNIT_ID = "ca-app-pub-5849487494074701/2903707073";
	InterstitialAd interstitial;
	SharedPreferences globalPref;
	Context context;
	
	public AdPreference(SharedPreferences p, Context ctxt){
		globalPref = p;
		context = ctxt;
	}
    
    public void setUpAd() {
    	Calendar c = Calendar.getInstance(); 
		int day = c.get(Calendar.DATE);
		int lastTimeShownAd = globalPref.getInt("adDisplayDate", -1);
		
		Log.d("Ads", "today is "+ day);
		Log.d("Ads", "and last time I showed an ad was on "+ globalPref.getInt("adDisplayDate", -1));
		if (lastTimeShownAd!=day)
			Log.d("Ads", "so I will start loading up the ad");
		else
			Log.d("Ads", "so I will not load the ad");
			
		if (lastTimeShownAd!=day){
			try{
			AdRegistration.setAppKey("yourkey");
//			AdRegistration.enableTesting(true);
//			AdRegistration.enableLogging(true);
			}catch(Exception e){}
			interstitial = new InterstitialAd((Activity) context);
			interstitial.setListener(new AdListener() {
				
				@Override
				public void onAdLoaded(Ad arg0, AdProperties arg1) {
					// TODO Auto-generated method stub
					displayInterstitial();
					Calendar c = Calendar.getInstance(); 
					int day = c.get(Calendar.DATE);
					globalPref.edit().putInt("adDisplayDate", day).commit();
				}
				
				@Override
				public void onAdFailedToLoad(Ad arg0, AdError arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAdExpanded(Ad arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAdDismissed(Ad arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAdCollapsed(Ad arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			interstitial.loadAd();
		}
		
 
    }
    
    public void displayInterstitial() {
    	interstitial.showAd();
    }
} 