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
import androidx.core.content.ContextCompat;
import android.app.Activity;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.content.Context;
import android.content.ContentResolver;
import android.content.SharedPreferences;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;

public class Elements extends SettingsPreferenceFragment {
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.elements, container, false);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragment = new ElementsFragment();
        mFragmentTransaction.replace(R.id.fragment_container, mFragment, Constants.MAIN_VIEW);
        mFragmentTransaction.commit();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getActivity().setTitle(R.string.tielements_title);
        setHasOptionsMenu(true);
        ContentResolver resolver = getActivity().getContentResolver();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.TIELEMENTS;
    }

}
