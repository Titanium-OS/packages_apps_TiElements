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

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.titanium.AwesomeAnimationHelper;

import com.titanium.support.preferences.CustomSeekBarPreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Animations extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String TAG = "Animations";
    private static final String ACTIVITY_OPEN = "activity_open";
    private static final String ACTIVITY_CLOSE = "activity_close";
    private static final String TASK_OPEN = "task_open";
    private static final String TASK_OPEN_BEHIND = "task_open_behind";
    private static final String TASK_CLOSE = "task_close";
    private static final String TASK_MOVE_TO_FRONT = "task_move_to_front";
    private static final String TASK_MOVE_TO_BACK = "task_move_to_back";
    private static final String WALLPAPER_OPEN = "wallpaper_open";
    private static final String WALLPAPER_CLOSE = "wallpaper_close";
    private static final String WALLPAPER_INTRA_OPEN = "wallpaper_intra_open";
    private static final String WALLPAPER_INTRA_CLOSE = "wallpaper_intra_close";
    private static final String KEY_TOAST_ANIMATION = "toast_animation";
    private static final String KEY_LISTVIEW_ANIMATION = "listview_animation";
    private static final String KEY_LISTVIEW_INTERPOLATOR = "listview_interpolator";
    private static final String SCROLLINGCACHE_PREF = "pref_scrollingcache";
    private static final String SCROLLINGCACHE_PERSIST_PROP = "persist.sys.scrollingcache";
    private static final String SCROLLINGCACHE_DEFAULT = "2";
    private static final String ANIMATION_DURATION = "animation_duration";
    private static final String KEY_SCREEN_OFF_ANIMATION = "screen_off_animation";

    private ListPreference mToastAnimation;
    private ListPreference mScrollingCachePref;
    private ListPreference mListViewAnimation;
    private ListPreference mListViewInterpolator;
       private ListPreference mActivityOpenPref;
    private ListPreference mActivityClosePref;
    private ListPreference mTaskOpenPref;
    private ListPreference mTaskOpenBehind;
    private ListPreference mTaskClosePref;
    private ListPreference mTaskMoveToFrontPref;
    private ListPreference mTaskMoveToBackPref;
    private ListPreference mWallpaperOpen;
    private ListPreference mWallpaperClose;
    private ListPreference mWallpaperIntraOpen;
    private ListPreference mWallpaperIntraClose;
    private CustomSeekBarPreference mAnimationDuration;
    private ListPreference mScreenOffAnimation;

    private int[] mAnimations;
    private String[] mAnimationsStrings;
    private String[] mAnimationsNum;
    private Context mContext;

@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.animations);
        setRetainInstance(true);

        ContentResolver resolver = getActivity().getContentResolver();
        PreferenceScreen prefs = getPreferenceScreen();
        mContext = getActivity();

        mToastAnimation = (ListPreference) findPreference(KEY_TOAST_ANIMATION);
        mToastAnimation.setSummary(mToastAnimation.getEntry());
        int CurrentToastAnimation = Settings.Global.getInt(getContentResolver(), Settings.Global.TOAST_ANIMATION, 1);
        mToastAnimation.setValueIndex(CurrentToastAnimation); //set to index of default value
        mToastAnimation.setSummary(mToastAnimation.getEntries()[CurrentToastAnimation]);
        mToastAnimation.setOnPreferenceChangeListener(this);

        mListViewAnimation = (ListPreference) findPreference(KEY_LISTVIEW_ANIMATION);
        int listviewanimation = Settings.Global.getInt(getContentResolver(),
                Settings.Global.LISTVIEW_ANIMATION, 0);
        mListViewAnimation.setValue(String.valueOf(listviewanimation));
        mListViewAnimation.setSummary(mListViewAnimation.getEntry());
        mListViewAnimation.setOnPreferenceChangeListener(this);

        mListViewInterpolator = (ListPreference) findPreference(KEY_LISTVIEW_INTERPOLATOR);
        int listviewinterpolator = Settings.Global.getInt(getContentResolver(),
                Settings.Global.LISTVIEW_INTERPOLATOR, 0);
        mListViewInterpolator.setValue(String.valueOf(listviewinterpolator));
        mListViewInterpolator.setSummary(mListViewInterpolator.getEntry());
        mListViewInterpolator.setOnPreferenceChangeListener(this);
        mListViewInterpolator.setEnabled(listviewanimation > 0);

        mScrollingCachePref = (ListPreference) findPreference(SCROLLINGCACHE_PREF);
        mScrollingCachePref.setValue(SystemProperties.get(SCROLLINGCACHE_PERSIST_PROP,
                SystemProperties.get(SCROLLINGCACHE_PERSIST_PROP, SCROLLINGCACHE_DEFAULT)));
        mScrollingCachePref.setOnPreferenceChangeListener(this);


        mAnimations = AwesomeAnimationHelper.getAnimationsList();
        int animqty = mAnimations.length;
        mAnimationsStrings = new String[animqty];
        mAnimationsNum = new String[animqty];
        for (int i = 0; i < animqty; i++) {
            mAnimationsStrings[i] = AwesomeAnimationHelper.getProperName(mContext, mAnimations[i]);
            mAnimationsNum[i] = String.valueOf(mAnimations[i]);

        mScreenOffAnimation = (ListPreference) findPreference(KEY_SCREEN_OFF_ANIMATION);
        int screenOffAnimation = Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_OFF_ANIMATION, 0);
        mScreenOffAnimation.setValue(Integer.toString(screenOffAnimation));
        mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntry());
        mScreenOffAnimation.setOnPreferenceChangeListener(this);
        }

        mAnimationDuration = (CustomSeekBarPreference) findPreference(ANIMATION_DURATION);
        mAnimationDuration.setValue(Settings.Global.getInt(resolver, Settings.Global.ANIMATION_CONTROLS_DURATION, 0));
        mAnimationDuration.setOnPreferenceChangeListener(this);

        mActivityOpenPref = (ListPreference) findPreference(ACTIVITY_OPEN);
        mActivityOpenPref.setOnPreferenceChangeListener(this);
        mActivityOpenPref.setSummary(getProperSummary(mActivityOpenPref));
        mActivityOpenPref.setEntries(mAnimationsStrings);
        mActivityOpenPref.setEntryValues(mAnimationsNum);

        mActivityClosePref = (ListPreference) findPreference(ACTIVITY_CLOSE);
        mActivityClosePref.setOnPreferenceChangeListener(this);
        mActivityClosePref.setSummary(getProperSummary(mActivityClosePref));
        mActivityClosePref.setEntries(mAnimationsStrings);
        mActivityClosePref.setEntryValues(mAnimationsNum);

        mTaskOpenPref = (ListPreference) findPreference(TASK_OPEN);
        mTaskOpenPref.setOnPreferenceChangeListener(this);
        mTaskOpenPref.setSummary(getProperSummary(mTaskOpenPref));
        mTaskOpenPref.setEntries(mAnimationsStrings);
        mTaskOpenPref.setEntryValues(mAnimationsNum);

        mTaskOpenBehind = (ListPreference) findPreference(TASK_OPEN_BEHIND);
        mTaskOpenBehind.setOnPreferenceChangeListener(this);
        mTaskOpenBehind.setSummary(getProperSummary(mTaskOpenBehind));
        mTaskOpenBehind.setEntries(mAnimationsStrings);
        mTaskOpenBehind.setEntryValues(mAnimationsNum);

        mTaskClosePref = (ListPreference) findPreference(TASK_CLOSE);
        mTaskClosePref.setOnPreferenceChangeListener(this);
        mTaskClosePref.setSummary(getProperSummary(mTaskClosePref));
        mTaskClosePref.setEntries(mAnimationsStrings);
        mTaskClosePref.setEntryValues(mAnimationsNum);

        mTaskMoveToFrontPref = (ListPreference) findPreference(TASK_MOVE_TO_FRONT);
        mTaskMoveToFrontPref.setOnPreferenceChangeListener(this);
        mTaskMoveToFrontPref.setSummary(getProperSummary(mTaskMoveToFrontPref));
        mTaskMoveToFrontPref.setEntries(mAnimationsStrings);
        mTaskMoveToFrontPref.setEntryValues(mAnimationsNum);

        mTaskMoveToBackPref = (ListPreference) findPreference(TASK_MOVE_TO_BACK);
        mTaskMoveToBackPref.setOnPreferenceChangeListener(this);
        mTaskMoveToBackPref.setSummary(getProperSummary(mTaskMoveToBackPref));
        mTaskMoveToBackPref.setEntries(mAnimationsStrings);
        mTaskMoveToBackPref.setEntryValues(mAnimationsNum);

        mWallpaperOpen = (ListPreference) findPreference(WALLPAPER_OPEN);
        mWallpaperOpen.setOnPreferenceChangeListener(this);
        mWallpaperOpen.setSummary(getProperSummary(mWallpaperOpen));
        mWallpaperOpen.setEntries(mAnimationsStrings);
        mWallpaperOpen.setEntryValues(mAnimationsNum);

        mWallpaperClose = (ListPreference) findPreference(WALLPAPER_CLOSE);
        mWallpaperClose.setOnPreferenceChangeListener(this);
        mWallpaperClose.setSummary(getProperSummary(mWallpaperClose));
        mWallpaperClose.setEntries(mAnimationsStrings);
        mWallpaperClose.setEntryValues(mAnimationsNum);

        mWallpaperIntraOpen = (ListPreference) findPreference(WALLPAPER_INTRA_OPEN);
        mWallpaperIntraOpen.setOnPreferenceChangeListener(this);
        mWallpaperIntraOpen.setSummary(getProperSummary(mWallpaperIntraOpen));
        mWallpaperIntraOpen.setEntries(mAnimationsStrings);
        mWallpaperIntraOpen.setEntryValues(mAnimationsNum);

        mWallpaperIntraClose = (ListPreference) findPreference(WALLPAPER_INTRA_CLOSE);
        mWallpaperIntraClose.setOnPreferenceChangeListener(this);
        mWallpaperIntraClose.setSummary(getProperSummary(mWallpaperIntraClose));
        mWallpaperIntraClose.setEntries(mAnimationsStrings);
        mWallpaperIntraClose.setEntryValues(mAnimationsNum);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TIELEMENTS;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        boolean result = false;

        if (preference == mToastAnimation) {
            int index = mToastAnimation.findIndexOfValue((String) newValue);
            Settings.Global.putString(getContentResolver(), Settings.Global.TOAST_ANIMATION, (String) newValue);
            mToastAnimation.setSummary(mToastAnimation.getEntries()[index]);
            Toast.makeText(mContext, "Toast Test", Toast.LENGTH_SHORT).show();
            return true;
        } else if (preference == mListViewAnimation) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewAnimation.findIndexOfValue((String) newValue);
            Settings.Global.putInt(getContentResolver(),
                    Settings.Global.LISTVIEW_ANIMATION, value);
            mListViewAnimation.setSummary(mListViewAnimation.getEntries()[index]);
            mListViewInterpolator.setEnabled(value > 0);
            return true;
        } else if (preference == mListViewInterpolator) {
            int value = Integer.parseInt((String) newValue);
            int index = mListViewInterpolator.findIndexOfValue((String) newValue);
            Settings.Global.putInt(getContentResolver(),
                    Settings.Global.LISTVIEW_INTERPOLATOR, value);
            mListViewInterpolator.setSummary(mListViewInterpolator.getEntries()[index]);
            return true;
        } else if (preference == mScrollingCachePref) {
            if (newValue != null) {
                SystemProperties.set(SCROLLINGCACHE_PERSIST_PROP, (String) newValue);
            }
            return true;
        } else if (preference == mActivityOpenPref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[0], val);
        } else if (preference == mActivityClosePref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[1], val);
        } else if (preference == mTaskOpenPref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[2], val);
        } else if (preference == mTaskClosePref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[3], val);
        } else if (preference == mTaskMoveToFrontPref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[4], val);
        } else if (preference == mTaskMoveToBackPref) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[5], val);
        } else if (preference == mWallpaperOpen) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[6], val);
        } else if (preference == mWallpaperClose) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[7], val);
        } else if (preference == mWallpaperIntraOpen) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[8], val);
        } else if (preference == mWallpaperIntraClose) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[9], val);
        } else if (preference == mTaskOpenBehind) {
            int val = Integer.parseInt((String) newValue);
            result = Settings.Global.putInt(mContext.getContentResolver(),
                    Settings.Global.ACTIVITY_ANIMATION_CONTROLS[10], val);
        } else if (preference == mAnimationDuration) {
            int val = (Integer) newValue;
            Settings.Global.putInt(getContentResolver(), Settings.Global.ANIMATION_CONTROLS_DURATION, val);
            return true;
        } else if (preference == mScreenOffAnimation) {
            int value = Integer.valueOf((String) newValue);
            int index = mScreenOffAnimation.findIndexOfValue((String) newValue);
            mScreenOffAnimation.setSummary(mScreenOffAnimation.getEntries()[index]);
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_ANIMATION, value);
            return true;
        }
        preference.setSummary(getProperSummary(preference));
        return result;
    }

    private String getProperSummary(Preference preference) {
        String mString = "";
        if (preference == mActivityOpenPref) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[0];
        } else if (preference == mActivityClosePref) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[1];
        } else if (preference == mTaskOpenPref) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[2];
        } else if (preference == mTaskClosePref) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[3];
        } else if (preference == mTaskMoveToFrontPref) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[4];
        } else if (preference == mTaskMoveToBackPref) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[5];
        } else if (preference == mWallpaperOpen) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[6];
        } else if (preference == mWallpaperClose) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[7];
        } else if (preference == mWallpaperIntraOpen) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[8];
        } else if (preference == mWallpaperIntraClose) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[9];
        } else if (preference == mTaskOpenBehind) {
            mString = Settings.Global.ACTIVITY_ANIMATION_CONTROLS[10];
        }
        int mNum = Settings.Global.getInt(mContext.getContentResolver(), mString, 0);
        return mAnimationsStrings[mNum];
    }

}
