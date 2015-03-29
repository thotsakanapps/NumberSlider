package com.thotsakan.numberslider;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends PreferenceActivity {

	private class HeadersFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.settings);
			PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.settings, false);
		}

		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
			String key = preference.getKey();

			if (key.equals(getString(R.string.market_rate_app_key))) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.class.getPackage().getName()));
				startActivity(intent);
			} else if (key.equals(getString(R.string.market_other_apps_by_us_key))) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("https://play.google.com/store/search?q=pub:" + getString(R.string.market_other_apps_by_us_publisher)));
				startActivity(intent);
			}
			return super.onPreferenceTreeClick(preferenceScreen, preference);
		}
	}

	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		Fragment fragment = new HeadersFragment();
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(android.R.id.content, fragment);
		transaction.commit();
	}
}
