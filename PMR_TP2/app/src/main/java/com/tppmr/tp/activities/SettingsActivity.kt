@file:Suppress("DEPRECATION")

package com.tppmr.tp.activities

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceActivity
import android.view.KeyEvent
import com.tppmr.tp.R

class SettingsActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.preferences)
        addPreferencesFromResource(R.xml.preferences)
    }

    /**
     * Implémente le clique sur le bouton retour pour forcer le passage à l'activité MainActivity
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(Intent(this, MainActivity::class.java))
            false
        } else super.onKeyDown(keyCode, event)
    }

}
