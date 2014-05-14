package com.powerpoint45.lucidbrowser;

import java.io.File;
import preferences.ColorPickerPreference;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;
import com.powerpoint45.lucidbrowser.R;

public class SettingsV2 extends PreferenceActivity {
	SharedPreferences globalPref;
	ColorPickerPreference sideColor;
	ColorPickerPreference sideTextColor;
	Boolean firstStart = true;

	public static class HelperMethods {
		static void DeleteRecursive(File fileOrDirectory) {
			if (fileOrDirectory.exists()) {
				if (fileOrDirectory.isDirectory())
					for (File child : fileOrDirectory.listFiles())
						DeleteRecursive(child);

				fileOrDirectory.delete();
			}
		}

		static void clearBrowsingTrace(String trace, ApplicationInfo appInfo) {
			if (trace == "cache") {
				DeleteRecursive(new File(appInfo.dataDir
						+ "/app_webview/Cache/"));
				DeleteRecursive(new File(appInfo.dataDir + "/cache/"));

			} else if (trace == "cookies") {
				DeleteRecursive(new File(appInfo.dataDir
						+ "/databases/webviewCookiesChromium.db"));
				DeleteRecursive(new File(appInfo.dataDir
						+ "/databases/webviewCookiesChromiumPrivate.db"));
				DeleteRecursive(new File(appInfo.dataDir
						+ "/app_webview/Cookies"));
				DeleteRecursive(new File(appInfo.dataDir
						+ "/app_webview/Cookies-journal"));

			} else if (trace == "history") {
				DeleteRecursive(new File(appInfo.dataDir
						+ "/databases/webview.db"));
				DeleteRecursive(new File(appInfo.dataDir
						+ "/databases/webview.db-shm"));
				DeleteRecursive(new File(appInfo.dataDir
						+ "/databases/webview.db-wal"));
				DeleteRecursive(new File(appInfo.dataDir
						+ "/app_webview/Web Data"));
				DeleteRecursive(new File(appInfo.dataDir
						+ "/app_webview/Web Data-journal"));
			} else if (trace == "all") {

				clearBrowsingTrace("cache", appInfo);
				clearBrowsingTrace("cookies", appInfo);
				clearBrowsingTrace("history", appInfo);

			} else {
				System.err
						.println("clearBrowsingTrace(String trace) did nothing. Wrong parameter was given");
			}
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		globalPref = PreferenceManager
				.getDefaultSharedPreferences(SettingsV2.this);
		MainActivity.activity = this;
		addPreferencesFromResource(R.xml.settings_v2);

		((Preference) findPreference("reset"))
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						SharedPreferences.Editor ed = globalPref.edit();
						ed.clear();
						ed.commit();
						Toast.makeText(getApplicationContext(),
								(getResources().getText(R.string.complete)),
								Toast.LENGTH_LONG).show();
						return false;
					}
				});

		((Preference) findPreference("clearbrowsercache"))
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						ApplicationInfo appInfo = getApplicationInfo();
						HelperMethods.clearBrowsingTrace("cache", appInfo);
						Toast.makeText(getApplicationContext(),
								(getResources().getText(R.string.complete)),
								Toast.LENGTH_LONG).show();
						return false;
					}
				});

		((Preference) findPreference("clearbrowserhistory"))
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						ApplicationInfo appInfo = getApplicationInfo();
						HelperMethods.clearBrowsingTrace("history", appInfo);
						Toast.makeText(getApplicationContext(),
								(getResources().getText(R.string.complete)),
								Toast.LENGTH_LONG).show();
						return false;
					}
				});

		((Preference) findPreference("clearbrowsercookies"))
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					public boolean onPreferenceClick(Preference preference) {
						ApplicationInfo appInfo = getApplicationInfo();
						HelperMethods.clearBrowsingTrace("cookies", appInfo);
						Toast.makeText(getApplicationContext(),
								(getResources().getText(R.string.complete)),
								Toast.LENGTH_LONG).show();
						return false;
					}
				});

		/*
		 * Customizable side bar
		 * 
		 * 1. Remove sidebar color settings from settings_v2.xml Save it
		 * globally, so that it can be added later
		 * 
		 * 2. Set OnPreferenceChangeListener to hide sidebarcolor and
		 * sidebartextcolor when sidebartheme = b or sidebartheme = w Add
		 * sidebar color settings when c (custom) is selected)
		 */

		if (firstStart) {
			String sidebarTheme = globalPref.getString("sidebartheme", "b");
			if (!sidebarTheme.equals("c")) {

				PreferenceScreen preferenceScreen = getPreferenceScreen();

				sideColor = (ColorPickerPreference) preferenceScreen
						.findPreference("sidebarcolor");
				sideTextColor = (ColorPickerPreference) preferenceScreen
						.findPreference("sidebartextcolor");

				((PreferenceGroup) findPreference("sideappearance"))
						.removePreference(sideColor);
				((PreferenceGroup) findPreference("sideappearance"))
						.removePreference(sideTextColor);

				firstStart = false;
			}
			;
		}
		;

		((Preference) findPreference("sidebartheme"))
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {

						String sidebarTheme = (String) newValue;
						if (!newValue.equals("c")) {
							try {
								PreferenceScreen preferenceScreen = getPreferenceScreen();

								((PreferenceGroup) findPreference("sideappearance"))
										.removePreference(sideColor);
								((PreferenceGroup) findPreference("sideappearance"))
										.removePreference(sideTextColor);

							} catch (Exception e) {
								System.out
										.println("Sidebar color preferences already removed");
							}
						} else {
							PreferenceScreen preferenceScreen = getPreferenceScreen();

							ColorPickerPreference testSideColor = (ColorPickerPreference) preferenceScreen
									.findPreference("sidebarcolor");

							if (testSideColor == null) {

								((PreferenceGroup) findPreference("sideappearance"))
										.addPreference(sideColor);
								((PreferenceGroup) findPreference("sideappearance"))
										.addPreference(sideTextColor);
							}

						}
						;
						return true;
					}
				});

		// if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) { //
		// Translucent available
		int id = getResources().getIdentifier("config_enableTranslucentDecor",
				"bool", "android");
		if (id == 0) {
			try {
				PreferenceScreen mCategory = (PreferenceScreen) findPreference("mainsettings");
				mCategory
						.removePreference(((Preference) findPreference("transparentnav")));
				mCategory
						.removePreference(((Preference) findPreference("transparentstatus")));
			} catch (Exception e) {
			}
			;
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(SettingsV2.this, MainActivity.class));
	}

	@Override
	public void onStop() {
		super.onStop();
		finish();
	}

}