package com.example.application_to_do_list_2.data

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson

@Suppress("DEPRECATION")
class Prefs (context: Context) {
    var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    var pseudo = prefs?.getString("pseudo","Pseudo").toString()
    var hash = prefs?.getString("hash","").toString()

    // ON ENREGISTERA PAR CETTE METHODE LE PSEUDO ET LE HASH
    fun enregistrerPref(key : String, value : Any){
        val gsonset = Gson()
        val jsonset = gsonset.toJson(value)
        var editor= prefs?.edit()
        editor?.putString(key,jsonset)
        editor?.commit()
   }
}
