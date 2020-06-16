package com.example.tp2.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.tp2.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}