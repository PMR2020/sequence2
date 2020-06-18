package com.example.myapplicationtp1

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.EditText
import com.example.myapplicationtp1.MainActivity.Companion.pseudo
import com.example.myapplicationtp1.data.DataProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : ToolbarActivity() {
    companion object {
        var pseudo = ""
    }
    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("MainActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonOk.setOnClickListener {
            var pseudo = findViewById<EditText>(R.id.pseudo).text.toString()
            Log.v("Pseudo = ",pseudo)
            var mdp = findViewById<EditText>(R.id.password).text.toString()
            Log.v("mdp = ",mdp)

            activityScope.launch {
                val hash = withContext(Dispatchers.IO) {
                    DataProvider.getHash(pseudo, mdp)
                }

                val prefs = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                val editor = prefs.edit()
                editor.putString("hash", hash)
                editor.putString("pseudo", pseudo)
                editor.apply()

                val intent = Intent(this@MainActivity, ChoixListActivity::class.java)
                startActivity(intent)
            }
        }
    }
}