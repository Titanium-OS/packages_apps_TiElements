/*
 * Copyright (C) 2019 TitaniumOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.titanium.tielements.fragments;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.UserHandle;
import androidx.preference.ListPreference;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.Utils;

public class Misc extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
    
    private static final String TAG = "Misc";

    private static final String RINGTONE_FOCUS_MODE = "ringtone_focus_mode";

    private ListPreference mHeadsetRingtoneFocus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.misc);
        setRetainInstance(true);

        final ContentResolver resolver = getActivity().getContentResolver();

        mHeadsetRingtoneFocus = (ListPreference) findPreference(RINGTONE_FOCUS_MODE);
        int mHeadsetRingtoneFocusValue = Settings.Global.getInt(resolver,
                Settings.Global.RINGTONE_FOCUS_MODE, 0);
        mHeadsetRingtoneFocus.setValue(Integer.toString(mHeadsetRingtoneFocusValue));
        mHeadsetRingtoneFocus.setSummary(mHeadsetRingtoneFocus.getEntry());
        mHeadsetRingtoneFocus.setOnPreferenceChangeListener(this);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TIELEMENTS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver(); 
        Settings.System.putIntForUser(resolver,
                Settings.System.SCREENSHOT_SOUND, 1, UserHandle.USER_CURRENT);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        if (preference == mHeadsetRingtoneFocus) {
            int mHeadsetRingtoneFocusValue = Integer.valueOf((String) objValue);
            int index = mHeadsetRingtoneFocus.findIndexOfValue((String) objValue);
            mHeadsetRingtoneFocus.setSummary(
                    mHeadsetRingtoneFocus.getEntries()[index]);
            Settings.Global.putInt(getContentResolver(), Settings.Global.RINGTONE_FOCUS_MODE,
                    mHeadsetRingtoneFocusValue);
            return true;
        }
        return true;
    }

}
