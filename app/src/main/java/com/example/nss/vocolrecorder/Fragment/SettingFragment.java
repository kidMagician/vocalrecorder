package com.example.nss.vocolrecorder.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nss.vocolrecorder.Activity.SettingActivity;
import com.example.nss.vocolrecorder.Activity.VocalSettingActivity;
import com.example.nss.vocolrecorder.Listener.MySharedPreference;
import com.example.nss.vocolrecorder.R;


public class SettingFragment extends PreferenceFragment {



    public SettingFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        Preference pref_vocal = findPreference(getString(R.string.pref_vocal_key));

        pref_vocal.setSummary(MySharedPreference.getPrefVocalName(getActivity()));

        pref_vocal.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(getActivity(),VocalSettingActivity.class);

                startActivity(intent);

                return true;
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();

        Preference pref_vocal = findPreference(getString(R.string.pref_vocal_key));
        pref_vocal.setSummary(MySharedPreference.getPrefVocalName(getActivity()));

    }
}
