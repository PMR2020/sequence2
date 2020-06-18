package com.example.myapplicationtp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.TextView
import com.example.myapplicationtp1.MainActivity.Companion.pseudo

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        Log.v("PSEUDO2 = ", pseudo)
        setContentView(R.layout.activity_settings)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val textPseudo: TextView = findViewById<TextView>(R.id.pseudoText)
        textPseudo.text = prefs.getString("pseudo","")!!




    }
}
