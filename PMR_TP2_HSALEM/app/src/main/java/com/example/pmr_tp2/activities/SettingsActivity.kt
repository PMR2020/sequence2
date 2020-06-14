package com.example.pmr_tp2.activities

import android.os.Bundle
import android.preference.*
import android.util.Log
import com.example.pmr_tp2.R
import com.example.pmr_tp2.data_management.ToDoAPIService.Companion.DEFAULT_BASE_URL


class SettingsActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val screen = preferenceManager.createPreferenceScreen(this)


        addPreferencesFromResource(R.layout.activity_settings)

        val category = PreferenceCategory(this)
        category.title = "Préférences de l'application"

        screen.addPreference(category)


        // Preference to choose base url

        val customBaseURL : EditTextPreference = EditTextPreference(this)

        customBaseURL.setDefaultValue(DEFAULT_BASE_URL)
        customBaseURL.setTitle("Changer l'URL de base de l'API")
        customBaseURL.key = "urlPref"

        // ------------------------------

        category.addPreference(customBaseURL)
        preferenceScreen = screen

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val selectedPseudo= prefs.getString("listPref","none")

        Log.i("SNOW", "Selected pseudo : " + selectedPseudo)

    }
}