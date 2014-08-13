package com.powerpoint45.lucidbrowser;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;

public class Tools {

	public Matrix getResizedMatrix(Bitmap bm, int newHeight, int newWidth)
	{
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    return matrix;
	}
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
	{
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	    return resizedBitmap;
	}
	
	
	public float getScaleFactor(Resources res,String string){
		float scaleFactor=1.0f;
		XmlResourceParser xrp = null;
		XmlPullParser xpp=null;
			try{
			int n;
	        if ((n = res.getIdentifier("appfilter", "xml", string)) != 0) {
	        	xrp = res.getXml(n);
	            System.out.println(n);
	        } else {
	            //InputStream inputStream = res.getAssets().open("appfilter.xml");
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            factory.setValidating(false);
	            xpp = factory.newPullParser();
	            InputStream raw = res.getAssets().open("appfilter.xml");
	            xpp.setInput(raw, null);
	        }
	     
	        if (n!=0){
		        while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT && scaleFactor==1.0f) {    
		                if (xrp.getEventType()==2){
		                	try{
		                	String s = xrp.getName();
			                if (s.equals("scale")) {
			                	String scale = "1.0";
			                	//System.out.println("scale"+xrp.getAttributeValue(0));
			                	scaleFactor = Float.parseFloat(xrp.getAttributeValue(0));
			                }
		                	}catch(Exception e){}
		                }
		            xrp.next();
		        }
	        }
	        else{
	        	while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && scaleFactor==1.0f) {    
	                if (xpp.getEventType()==2){
	                	try{
	                	String s = xpp.getName();
	                	System.out.println(s+"SCTRING");
		                if (s.equals("scale")) {
		                	String scale = "1.0";
		                	System.out.println("scale"+xpp.getAttributeValue(0));
		                	scaleFactor = Float.parseFloat(xpp.getAttributeValue(0));
		                }
	                	}catch(Exception e){}
	                }
	            xpp.next();
	        }
	        }
		}catch(Exception e){
			System.out.println(e);
		}
		return scaleFactor;
	}
	
	
	public String getResourceName(Resources res,String string,String componentInfo){
		String resource = null;
		XmlResourceParser xrp = null;
		XmlPullParser xpp=null;
			try{
			int n;
	        if ((n = res.getIdentifier("appfilter", "xml", string)) != 0) {
	        	xrp = res.getXml(n);
	        } else {
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            factory.setValidating(false);
	            xpp = factory.newPullParser();
	            InputStream raw = res.getAssets().open("appfilter.xml");
	            xpp.setInput(raw, null);
	        }
	     
	        if (n!=0){
		        while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT && resource==null) {    
		                if (xrp.getEventType()==2){
		                	try{
		                	String s = xrp.getName();
			                if (s.equals("item")) {
			                	if (xrp.getAttributeValue(0).compareTo(componentInfo)==0){
				                	resource = xrp.getAttributeValue(1);
			                	}
			                }
		                	}catch(Exception e){}
		                }
		            xrp.next();
		        }
	        }
	        else{
	        	while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && resource==null) {    
	                if (xpp.getEventType()==2){
	                	try{
	                	String s = xpp.getName();
	                	if (s.equals("item")) {
		                	if (xpp.getAttributeValue(0).compareTo(componentInfo)==0){
			                	resource = xpp.getAttributeValue(1);
		                	}
		                }
	                	}catch(Exception e){}
	                }
	            xpp.next();
	        }
	        }
		}catch(Exception e){
			System.out.println(e);
		}
		return resource;
	}
	
	
	public String [] getIconBackAndMaskResourceName(Resources res,String string){
		String[] resource = new String[2];
		XmlResourceParser xrp = null;
		XmlPullParser xpp=null;
			try{
			int n;
	        if ((n = res.getIdentifier("appfilter", "xml", string)) != 0) {
	        	xrp = res.getXml(n);
	        } else {
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            factory.setValidating(false);
	            xpp = factory.newPullParser();
	            InputStream raw = res.getAssets().open("appfilter.xml");
	            xpp.setInput(raw, null);
	        }
	     
	        if (n!=0){
		        while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT && (resource[0]==null || resource[1]==null)) {    
		                if (xrp.getEventType()==2){
		                	try{
		                	String s = xrp.getName();
			                if (s.equals("iconback")) {
				                resource[0] = xrp.getAttributeValue(0);
			                }
			                if (s.equals("iconmask")) {
			                	resource[1] = xrp.getAttributeValue(0);
			                }
		                	}catch(Exception e){}
		                }
		            xrp.next();
		        }
	        }
	        else{
	        	while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && (resource[0]==null || resource[1]==null)) {    
	                if (xpp.getEventType()==2){
	                	try{
	                	String s = xpp.getName();
	                	if (s.equals("iconback")) {
			                resource[0] = xrp.getAttributeValue(0);
		                }
	                	if (s.equals("iconmask")) {
		                	resource[1] = xrp.getAttributeValue(0);
	                	}
	                	}catch(Exception e){}
	                }
	            xpp.next();
	        }
	        }
		}catch(Exception e){
			System.out.println(e);
		}
		return resource;
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
	  int actionBarHeight = LayoutParams.MATCH_PARENT;//fallback size
		TypedValue tv = new TypedValue();
		if (MainActivity.activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		{
		    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,MainActivity.activity.getResources().getDisplayMetrics());
		}
		
		return actionBarHeight;
  }

  
}
