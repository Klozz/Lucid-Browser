package com.powerpoint45.lucidbrowser;

import com.powerpoint45.lucidbrowserfree.R;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;


public class Properties extends MainActivity {
	public static int ActionbarSize=0; //used for getting the actual actionbar size + anny padding
	public static class appProp{
		public static int actionBarTransparency;
		static int actionBarColor;
		public static int primaryIntColor;
		static int urlBarColor;
		static boolean fullscreen = false;
		static boolean transparentNav = true;
		static boolean TransparentStatus = false;
		static boolean systemPersistent;
	}
	public static class sidebarProp{
		static int SidebarIconSize;
		static int SidebarIconPadding;
		static int SidebarSize;
		static int transparency;
		static float zoom;
		static String theme;
		static boolean showLabel;
		static boolean disable;
		static int sideBarColor;
		static int sideBarTextColor;
	}
	public static class webpageProp{
		static boolean showBackdrop;
		static boolean useDesktopView;
		static boolean clearonexit;
		static boolean enableimages;
		//static boolean enablejavascript; //uncomment if wanted by users
		static boolean enablecookies;
	}

	
	public static void update_preferences(){
		webpageProp.showBackdrop=MainActivity.mGlobalPrefs.getBoolean("showbrowserbackdrop",true);
		webpageProp.useDesktopView=MainActivity.mGlobalPrefs.getBoolean("usedesktopview",false);
		webpageProp.clearonexit=MainActivity.mGlobalPrefs.getBoolean("clearonexit",false);
		webpageProp.enableimages=MainActivity.mGlobalPrefs.getBoolean("enableimages", true);
		//webpageProp.enablejavascript=MainActivity.mGlobalPrefs.getBoolean("enablejavascript", true);
		//uncomment if wanted by users
		webpageProp.enablecookies=MainActivity.mGlobalPrefs.getBoolean("enablecookies", true);

		
		int actionBarHeight = LayoutParams.MATCH_PARENT;//fallback size
		TypedValue tv = new TypedValue();
		if (MainActivity.ctxt.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		{
		    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,MainActivity.activity.getResources().getDisplayMetrics());
		}
		
		ActionbarSize= actionBarHeight;

		appProp.actionBarTransparency=MainActivity.mGlobalPrefs.getInt("actionbartransparency",90);
		appProp.actionBarTransparency = (255*appProp.actionBarTransparency)/100;
		appProp.fullscreen=MainActivity.mGlobalPrefs.getBoolean       ("fullscreen"           ,false);
		appProp.transparentNav=MainActivity.mGlobalPrefs.getBoolean   ("transparentnav"       ,true);
		appProp.TransparentStatus=MainActivity.mGlobalPrefs.getBoolean("transparentstatus"    ,true);
		appProp.systemPersistent=MainActivity.mGlobalPrefs.getBoolean ("systempersistent"      ,false);
		appProp.primaryIntColor=MainActivity.mGlobalPrefs.getInt      ("textcolor",Color.WHITE);
		appProp.actionBarColor=MainActivity.mGlobalPrefs.getInt       ("actionbarcolor", MainActivity.activity.getResources().getColor(R.color.urlback));
		appProp.urlBarColor=MainActivity.mGlobalPrefs.getInt          ("urlbarcolor", MainActivity.activity.getResources().getColor(R.color.urlfront));
		
		sidebarProp.SidebarIconSize=numtodp(MainActivity.mGlobalPrefs.getInt    ("sidebariconsize"  ,80));
		sidebarProp.SidebarIconPadding=numtodp(MainActivity.mGlobalPrefs.getInt ("sidebariconpadding",10));
		sidebarProp.theme=MainActivity.mGlobalPrefs.getString                   ("sidebartheme", "b");
		sidebarProp.sideBarColor=MainActivity.mGlobalPrefs.getInt               ("sidebarcolor",Color.BLACK);
        sidebarProp.sideBarTextColor=MainActivity.mGlobalPrefs.getInt           ("sidebartextcolor", Color.WHITE);
		sidebarProp.showLabel=MainActivity.mGlobalPrefs.getBoolean              ("showfavoriteslabels", true);
		sidebarProp.transparency=MainActivity.mGlobalPrefs.getInt               ("sidebartransparency" ,100);
		sidebarProp.transparency= (254*sidebarProp.transparency)/100;
		if (sidebarProp.showLabel)
			sidebarProp.SidebarSize=numtodp(250);
		else
			sidebarProp.SidebarSize=sidebarProp.SidebarIconSize;
		sidebarProp.zoom=       sidebarProp.zoom/100f;
		sidebarProp.zoom=           MainActivity.mGlobalPrefs.getInt("sidebarscale" ,30);
		sidebarProp.disable=    MainActivity.mGlobalPrefs.getBoolean("hidefavorites", false);
		
	}
	
	public static int numtodp(int in){
		int out =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, in, MainActivity.activity.getResources().getDisplayMetrics());
		return out;
	}
	
	

}
