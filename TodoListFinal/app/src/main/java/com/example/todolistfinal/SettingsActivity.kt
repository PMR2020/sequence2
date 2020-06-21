package com.example.todolistfinal

import android.os.Bundle
import android.preference.*
import android.widget.Toast

class SettingsActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this@SettingsActivity)
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.userpreferences)

        val pseudoPreference : EditTextPreference = findPreference("edit_text_preference_1") as EditTextPreference
        pseudoPreference.dialogMessage = preferences.getString("pseudo","Anonym").toString()
        pseudoPreference.onPreferenceChangeListener = object: Preference.OnPreferenceChangeListener {
            override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
                val newpseudo : String  = pseudoPreference.toString()
                if(newpseudo != preferences.getString("pseudo","Anon")) {
                    preferences.edit().remove("pseudo")
                    preferences.edit().putString("pseudo",newpseudo) }
                Toast.makeText(applicationContext, "Pseudo Changed", Toast.LENGTH_SHORT).show()
                return true
            }
        }
//        val url_api = preferences.getString("hash","Anonym").toString()
//        val hash_value : EditTextPreference = findPreference("hash") as EditTextPreference
        //hash_value.setTitle(url_api)

        val doyouliketheapp : CheckBoxPreference = findPreference("checkbox1") as CheckBoxPreference
        doyouliketheapp.onPreferenceChangeListener = object: Preference.OnPreferenceChangeListener {
            override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
                val boolean_happy : Boolean  = !doyouliketheapp.isChecked
                if (boolean_happy)
                    Toast.makeText(applicationContext, "Thank you !", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(applicationContext, "It's alright, we will do better next time !", Toast.LENGTH_SHORT).show()
                return true
            }
        }



    }
}