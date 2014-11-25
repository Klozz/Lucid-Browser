package com.powerpoint45.lucidbrowser;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;

public abstract class Tools {
	
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
	
	public static void setActionBarColor(int c){
		ColorDrawable colorDrawable = new ColorDrawable(c);
  		MainActivity.actionBar.setBackgroundDrawable(colorDrawable);
	}


	@SuppressLint("NewApi")
	   public static boolean hasSoftNavigation(Context context)
	   {
		   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
		        return !ViewConfiguration.get(context).hasPermanentMenuKey();
		    }
		    return true;
	   }
	public static int getStatusBarHeight(Resources res) {
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        } 
        return result;
  } 
  public static int getNavBarHeight(Resources res){
	   int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
	   if (resourceId > 0) {
	       return res.getDimensionPixelSize(resourceId);
	   }
	   return 0;
  }
  
  public static float pxToDp(Context context, float px) {
      if (context == null) {
          return -1;
      }
      return px / context.getResources().getDisplayMetrics().density;
  }
  
  public static int getStatusMargine(){
	  int margine =0;
		int id = MainActivity.activity.getResources().getIdentifier("config_enableTranslucentDecor", "bool", "android");
		if (Properties.appProp.transparentNav || Properties.appProp.TransparentStatus)
			if (id != 0) {
		        if (Properties.appProp.fullscreen && Properties.appProp.transparentNav){
		        	//do nothing
		        	Log.d("LB", "1");
		        }else if (Properties.appProp.fullscreen){
		        	margine=Properties.ActionbarSize;
		        	Log.d("LB", "2");
		        }else if (Properties.appProp.transparentNav){
		        	Log.d("LB", "3");
		        	margine=Properties.ActionbarSize+Tools.getStatusBarHeight(MainActivity.activity.getResources());
		        }else{
		        	Log.d("LB", "4");
		        	margine=Properties.ActionbarSize+Tools.getStatusBarHeight(MainActivity.activity.getResources());
		        }

		        if (Properties.appProp.TransparentStatus&&Properties.appProp.fullscreen){
		        	Log.d("LB", "5");
		        	margine=Properties.ActionbarSize;
		        }
		    }
		return margine;
	}
  
  public static int getActionBarSize(){
//	  int actionBarHeight = LayoutParams.MATCH_PARENT;//fallback size
//		TypedValue tv = new TypedValue();
//		if (MainActivity.activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
//		{
//		    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,MainActivity.activity.getResources().getDisplayMetrics());
//		}
		
		return (int) MainActivity.activity.getResources().getDimension(R.dimen.actionBarSize);
  }

  
}
