package com.example.nss.vocolrecorder.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.example.nss.vocolrecorder.Activity.TeacherSellectActivity;
import com.example.nss.vocolrecorder.Activity.VocalSellectActivity;
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

                Intent intent = new Intent(getActivity(),VocalSellectActivity.class);

                startActivity(intent);

                return true;
            }
        });

        Preference pref_teacher = findPreference(getString(R.string.pref_teacher_key));

        pref_teacher.setSummary(MySharedPreference.getPrefTeacherNick(getActivity()));

        pref_teacher.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(getActivity(),TeacherSellectActivity.class);

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
