package com.example.android.udacitycapstoneproject.ui.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.android.udacitycapstoneproject.MyApp;
import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.utils.InjectorUtil;
import com.example.android.udacitycapstoneproject.worker.SyncNewsWorker;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import timber.log.Timber;

import static com.example.android.udacitycapstoneproject.utils.AppConstants.TAG_PERIODIC_WORK_REQUEST;

public class SettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        //registerNewsFavChangeListener();
    }


    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        private PeriodicWorkRequest periodicWorkRequest;
        private SharedPreferences prefs;
        private SharedPreferences.OnSharedPreferenceChangeListener listener;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("select fav channel"));
            registerNewsFavChangeListener();

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        /**
         * listener to change in fav channel & sync data using work-manager
         * cancel existing work manager instance
         */
        private void registerNewsFavChangeListener() {
            prefs = PreferenceManager.getDefaultSharedPreferences(MyApp.getInstance());
            listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if(key.equals(getString(R.string.key_fav_channel))) {
                        Timber.d("fav channel changed");
                        AppNewsRepository newsRepository = InjectorUtil.provideRepository(MyApp.getInstance());
                        newsRepository.setDefaultOrFavChannel(sharedPreferences.getString(key,
                                getString(R.string.channel_source_bbc_sports)));
                        if (periodicWorkRequest != null) {
                            UUID compressionWorkId = periodicWorkRequest.getId();
                            WorkManager.getInstance().cancelWorkById(compressionWorkId);
                        }
                        startWorkManager();
                    }
                }
            };
            prefs.registerOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            prefs.unregisterOnSharedPreferenceChangeListener(listener);
        }

        /**
         * starts work manager using periodic work request
         */
        private void startWorkManager() {
            final WorkManager workManager = WorkManager.getInstance();

            periodicWorkRequest =
                    new PeriodicWorkRequest.Builder(SyncNewsWorker.class,
                            2, TimeUnit.SECONDS)
                            .addTag(TAG_PERIODIC_WORK_REQUEST)
                            .build();

            // Queue the work
            Timber.d("work manager enqueue");
            workManager.enqueue(periodicWorkRequest);
        }

    }

}
