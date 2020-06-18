package com.example.tp1_todolist.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceScreen
import com.example.tp1_todolist.R
import com.example.tp1_todolist.fragment.MySettingsFragment

class SettingsActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("SettingsActivity","onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_activity)
        this.title="Setting"
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, MySettingsFragment())
            .commit()

       // var pseudo by MyPreference(this, "pseudo", "pseudoDefault")
//        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this )
//        val pseudoInSetting = sharedPreferences.getString("pseudo", pseudo)

        val sharedPreference =  getSharedPreferences("Setting",Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
            editor.putString("URL_BASE","http://tomnab.fr/todo-api/")
            editor.commit()

        var URL_BASE=sharedPreference.getString("username",getString(R.string.DEFAULT_URL_BASE))



        fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference?) {
            val t = Toast.makeText(this, "You have been logout", Toast.LENGTH_SHORT)
            t.show()
        }


    }





}




