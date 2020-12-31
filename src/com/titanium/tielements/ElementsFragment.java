package com.titanium.tielements;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceManager;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceScreen;
import android.content.Context;
import android.content.ContentResolver;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.content.Context;
import android.content.ContentResolver;
import android.content.SharedPreferences;


import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;

import com.titanium.tielements.fragments.About;
import com.titanium.tielements.fragments.Ambient;
import com.titanium.tielements.fragments.Animations;
import com.titanium.tielements.fragments.Battery;
import com.titanium.tielements.fragments.Buttons;
import com.titanium.tielements.fragments.Lockscreen;
import com.titanium.tielements.fragments.Gestures;
import com.titanium.tielements.fragments.Notifications;
import com.titanium.tielements.fragments.QuickSettings;
import com.titanium.tielements.fragments.Screen;
import com.titanium.tielements.fragments.Statusbar;
import com.titanium.tielements.fragments.Misc;
import com.titanium.tielements.views.MenuViews;

public class ElementsFragment extends SettingsPreferenceFragment implements View.OnClickListener, Preference.OnPreferenceChangeListener {

    private static final String TAG = "TiElements";

    private MenuViews statusbar, gestures, qs, misc, lockscreen, notications, buttons, about, animations, ambient, battery, screen;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.elements_fragment, container, false);
        mFragmentManager = getActivity().getSupportFragmentManager();
        getActivity().setTitle(R.string.tielements_title);
        initViews(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void initViews(final View view) {
        statusbar = (MenuViews) view.findViewById(R.id.statusbar_menu);
        gestures = (MenuViews) view.findViewById(R.id.gestures_menu);
        qs = (MenuViews) view.findViewById(R.id.quick_settings_menu);
        notications = (MenuViews) view.findViewById(R.id.notifications_menu);
        lockscreen = (MenuViews) view.findViewById(R.id.lockscreen_menu);
        misc = (MenuViews) view.findViewById(R.id.misc_menu);
        ambient = (MenuViews) view.findViewById(R.id.ambient_menu);
        battery = (MenuViews) view.findViewById(R.id.battery_menu);
        screen = (MenuViews) view.findViewById(R.id.screen_menu);
        animations = (MenuViews) view.findViewById(R.id.animations_menu);
        buttons = (MenuViews) view.findViewById(R.id.buttons_menu);
        about = (MenuViews) view.findViewById(R.id.about_menu);

        initClick();
    }

    private void initClick() {
        statusbar.setOnClickListener(this);
        gestures.setOnClickListener(this);
        qs.setOnClickListener(this);
        notications.setOnClickListener(this);
        lockscreen.setOnClickListener(this);
        misc.setOnClickListener(this);
        ambient.setOnClickListener(this);
        battery.setOnClickListener(this);
        screen.setOnClickListener(this);
        animations.setOnClickListener(this);
        buttons.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    private void loadFragment(String tag, boolean addToStack, Bundle bundle, Fragment setFragment) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.activity_open_enter, R.anim.activity_close_exit);
        mFragment = setFragment;

        if (addToStack) {
            if (bundle != null)
                mFragment.setArguments(bundle);
            mFragmentTransaction.replace(R.id.fragment_container, mFragment, tag);
            mFragmentTransaction.addToBackStack(tag).commit();

        } else {
            if (bundle != null)
                mFragment.setArguments(bundle);
            mFragmentTransaction.replace(R.id.fragment_container, mFragment, tag);
            mFragmentTransaction.commit();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.statusbar_menu) { 
            loadFragment(Constants.STATUS_BAR_MENU_FRAGMENT,true,null,new Statusbar());
        } 
        else if (view.getId() == R.id.gestures_menu) {
            loadFragment(Constants.GESTURES_MENU_FRAGMENT,true,null,new Gestures());
        }
        else if (view.getId() == R.id.quick_settings_menu) {
            loadFragment(Constants.QUICK_SETTINGS_MENU_FRAGMENT,true,null,new QuickSettings());
        }
        else if (view.getId() == R.id.notifications_menu) {
            loadFragment(Constants.RECENT_MENU_FRAGMENT,true,null,new Notifications());
        }
        else if (view.getId() == R.id.lockscreen_menu) {
            loadFragment(Constants.LOCK_SCREEN_MENU_FRAGMENT,true,null,new Lockscreen());
        }
        else if (view.getId() == R.id.misc_menu) {
            loadFragment(Constants.MISC_MENU_FRAGMENT,true,null,new Misc());
        }
        else if (view.getId() == R.id.ambient_menu) {
            loadFragment(Constants.AMBIENT_MENU_FRAGMENT,true,null,new Ambient());
        }
        else if (view.getId() == R.id.battery_menu) {
            loadFragment(Constants.BATTERY_MENU_FRAGMENT,true,null,new Battery());
        }
        else if (view.getId() == R.id.screen_menu) {
            loadFragment(Constants.SCREEN_MENU_FRAGMENT,true,null,new Screen());
        }
        else if (view.getId() == R.id.animations_menu) {
            loadFragment(Constants.ANIMATION_MENU_FRAGMENT,true,null,new Animations());
        }
        else if (view.getId() == R.id.buttons_menu) {
            loadFragment(Constants.BUTTONS_MENU_FRAGMENT,true,null,new Buttons());
        }
        else if (view.getId() == R.id.about_menu) {
            loadFragment(Constants.ABOUT_MENU_FRAGMENT,true,null,new About());
        }
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

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return true;
    }

}
