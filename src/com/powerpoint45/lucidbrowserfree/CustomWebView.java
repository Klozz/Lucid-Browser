package com.powerpoint45.lucidbrowserfree;

import java.net.URISyntaxException;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.powerpoint45.lucidbrowserfree.R;

public class CustomWebView extends WebView {

	private ProgressBar PB;
	private boolean videoPlaying;
	VideoEnabledWebChromeClient chromeClient;

	public CustomWebView(Context context, AttributeSet set, String url) {
		super(context, set);
		this.setId(R.id.browser_page);
		if (url == null)
			this.loadUrl(MainActivity.mPrefs.getString("browserhome",
					"file:///android_asset/home.html"));
		else
			this.loadUrl(url);

		if (Properties.webpageProp.useDesktopView) {
			this.getSettings().setUserAgentString(
					createUserAgentString(context, "desktop"));
			this.getSettings().setLoadWithOverviewMode(true);
		} else {
			this.getSettings().setUserAgentString(
					createUserAgentString(context, "mobile"));
			this.getSettings().setLoadWithOverviewMode(false);
		}

		// Enable / Disable cookies
		if (!Properties.webpageProp.enablecookies) {
			CookieSyncManager.createInstance(context);
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(false);
		} else {
			CookieSyncManager.createInstance(context);
			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
		}

		// Uncomment if wanted by users
		//
		// // Enable / Disable JavaScript
		// if (!Properties.webpageProp.enablejavascript) {
		// this.getSettings().setJavaScriptEnabled(false);
		// } else {
		this.getSettings().setJavaScriptEnabled(true);
		// }

		// Enable / Disable Images
		if (!Properties.webpageProp.enableimages) {
			this.getSettings().setLoadsImagesAutomatically(false);
		} else {
			this.getSettings().setLoadsImagesAutomatically(true);
		}

		this.getSettings().setPluginState(PluginState.ON);
		this.getSettings().setDomStorageEnabled(true);
		this.getSettings().setBuiltInZoomControls(true);
		this.getSettings().setDisplayZoomControls(false);
		this.getSettings().setUseWideViewPort(true);
		this.getSettings().setSaveFormData(true);
		this.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		((Activity) MainActivity.activity).registerForContextMenu(this);
		this.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (url.startsWith("https://play.google.com/store/")
						|| url.startsWith("market://")) {

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					intent.putExtra("tabNumber", MainActivity.getTabNumber());
					MainActivity.activity.startActivity(intent);
					System.out.println("Play Store!!");
					return true;
				}

				else if (url.startsWith("https://maps.google.")
						|| url.startsWith("intent://maps.google.")) {

					// Convert maps intent to normal http link
					if (url.contains("intent://")) {
						url = url.replace("intent://", "https://");
						url = url.substring(0, url.indexOf("#Intent;"));

					}
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					intent.putExtra("tabNumber", MainActivity.getTabNumber());
					MainActivity.activity.startActivity(intent);
					return true;
				}

				else if (url.contains("youtube.com/")) {
					// Might be a bit too generic but saves a lot of comparisons

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					intent.putExtra("tabNumber", MainActivity.getTabNumber());
					MainActivity.activity.startActivity(intent);
					return true;
				} else if (url.startsWith("intent://")) {

					Intent intent;
					try {
						intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
					} catch (URISyntaxException e) {
						e.printStackTrace();
						System.out.println("INVALID INTENT URI");
						return false;
					}
					intent.putExtra("tabNumber", MainActivity.getTabNumber());
					MainActivity.activity.startActivity(intent);
					return true;
				} else if (url.startsWith("mailto:")) {

					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(url));
					intent.putExtra("tabNumber", MainActivity.getTabNumber());
					MainActivity.activity.startActivity(intent);
					return true;
				}
				System.out.println(url);

				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (PB == null)
					try {
						PB = (ProgressBar) MainActivity.webLayout
								.findViewById(R.id.webpgbar);
					} catch (Exception e) {
					}
				;
				if (view.getVisibility() == View.VISIBLE)
					if (PB != null && PB.getVisibility() != View.VISIBLE
							&& url.compareTo("about:blank") != 0)
						PB.setVisibility(ProgressBar.VISIBLE);
				ImageButton IB = (ImageButton) MainActivity.bar
						.findViewById(R.id.browser_refresh);
				if (IB != null) {
					IB.setImageResource(R.drawable.btn_toolbar_stop_loading_normal);
				}
			}

			public void onPageFinished(WebView view, String url) {

				// do your stuff here
				if (PB == null)
					PB = (ProgressBar) MainActivity.webLayout
							.findViewById(R.id.webpgbar);
				if (MainActivity.browserListViewAdapter != null)
					MainActivity.browserListViewAdapter.notifyDataSetChanged();

				CustomWebView WV = (CustomWebView) MainActivity.webLayout
						.findViewById(R.id.browser_page);

				if (WV == CustomWebView.this) {// check if this webview is being
												// currently shown/used
					if (((EditText) ((Activity) MainActivity.activity)
							.findViewById(R.id.browser_searchbar)) != null)
						if (!((EditText) ((Activity) MainActivity.activity)
								.findViewById(R.id.browser_searchbar))
								.isFocused())
							if (view != null)
								if (view.getUrl() != null
										&& view.getUrl().compareTo(
												"about:blank") != 0) {
									if (view.getUrl().compareTo(
											"file:///android_asset/home.html") == 0) {
										((EditText) ((Activity) MainActivity.activity)
												.findViewById(R.id.browser_searchbar))
												.setText(MainActivity.activity
														.getResources()
														.getString(
																R.string.urlbardefault));
										CustomWebView.this
												.loadUrl("javascript:(function() { "
														+ "document.getElementById('searchbtn').value = "
														+ "'"
														+ MainActivity.activity
																.getResources()
																.getString(
																		R.string.search)
														+ "';"
														+ "document.title = '"
														+ MainActivity.activity
																.getResources()
																.getString(
																		R.string.home)
														+ "';" + "})()");
										Handler handler = new Handler();
										Runnable r = new Runnable() {
											public void run() {
												MainActivity.browserListViewAdapter
														.notifyDataSetChanged();
											}
										};
										handler.postDelayed(r, 500);// allows to
																	// wait for
																	// js to
																	// take
																	// effect
									} else
										((EditText) ((Activity) MainActivity.activity)
												.findViewById(R.id.browser_searchbar))
												.setText(view
														.getUrl()
														.replace("http://", "")
														.replace("https://", ""));
								}
					PB.setVisibility(ProgressBar.INVISIBLE);

					ImageButton IB = (ImageButton) MainActivity.bar
							.findViewById(R.id.browser_refresh);
					if (IB != null) {
						IB.setImageResource(R.drawable.btn_toolbar_reload_normal);
					}

					ImageButton BI = (ImageButton) MainActivity.bar
							.findViewById(R.id.browser_bookmark);
					if (BI != null) {
						int numBooks = MainActivity.mPrefs.getInt(
								"numbookmarkedpages", 0);
						boolean isBook = false;
						for (int i = 0; i < numBooks; i++) {
							if (CustomWebView.this != null)
								if (CustomWebView.this.getUrl() != null)
									if (MainActivity.mPrefs.getString(
											"bookmark" + i, "").compareTo(
											CustomWebView.this.getUrl()) == 0) {
										BI.setImageResource(R.drawable.btn_omnibox_bookmark_selected_normal);
										isBook = true;
										break;
									}
						}
						if (!isBook)
							BI.setImageResource(R.drawable.btn_omnibox_bookmark_normal);
					}
				}

			}
		});

		chromeClient = new VideoEnabledWebChromeClient(this);
		this.setWebChromeClient(chromeClient);

		this.setDownloadListener(new DownloadListener() {
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {

				if (MainActivity.isDownloadManagerAvailable(MainActivity.ctxt)) {
					DownloadManager.Request request = new DownloadManager.Request(
							Uri.parse(url));

					// TODO Check if necessary
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
						request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
					else
						request.setShowRunningNotification(true);

					if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR2)
						request.setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
					else
						request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);

					request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
					request.allowScanningByMediaScanner();
					request.setDestinationInExternalPublicDir(
							Environment.DIRECTORY_DOWNLOADS,
							url.substring(url.lastIndexOf('/') + 1,
									url.length()));
					DownloadManager manager = (DownloadManager) MainActivity.ctxt
							.getSystemService(Context.DOWNLOAD_SERVICE);
					manager.enqueue(request);
				}
			}
		});

	}

	public CustomWebView(Context context) {
		super(context);

		// TODO Auto-generated constructor stub
	}

	public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public boolean isVideoPlaying() {
		return videoPlaying;
	}

	private String createUserAgentString(Context application, String mode) {
		String ua = "";

		// TODO Test with different user agents
		// For now copied Chrome user agents and adapt them to the user's device
		if (mode.equals("mobile")) {

			ua = "Mozilla/5.0 (" + System.getProperty("os.name", "Linux")
					+ "; Android " + Build.VERSION.RELEASE + "; " + Build.MODEL
					+ "; Build/" + Build.ID
					+ ") AppleWebKit/537.36 (KHTML, like Gecko) "
					+ "Chrome/34.0.1847.114 Mobile Safari/537.36";

		} else if (mode.equals("desktop")) {
			ua = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.114 Mobile Safari/537.36";

		}
		return ua;
	}

	public void setVideoPlaying(boolean b) {
		videoPlaying = b;
	}

	public VideoEnabledWebChromeClient getChromeClient() {
		return chromeClient;
	}

}
