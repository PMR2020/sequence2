package com.example.todolistfinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.example.todolistfinal.choixlist.ChoixListActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : BasicActivity() {
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
//            val binding = ActivityMainBinding.inflate(layoutInflater)
            val pseudo = pseudoEdit.text.toString()
            val pwd = passwordEdit.text.toString()
            activityScope.launch {
                // main
                val hash = withContext(Dispatchers.IO){ DataProvider.getHashFromApi(pseudo,pwd)}

                val prefs = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                val editor: SharedPreferences.Editor = prefs.edit()
                editor.putString("pseudo",pseudo)
                editor.apply()
                editor.putString("hash",hash)
                editor.apply()
                val lists = DataProvider.getAllListsFromApi(hash)
                if (pseudo.toString() != "" && pwd.toString() !="") {
                    Toast.makeText(this@MainActivity, "Completed!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@MainActivity, ChoixListActivity::class.java)
                    startActivity(intent)
            }

            }

        }
        buttonClear.setOnClickListener {
            pseudoEdit.text.clear()
            passwordEdit.text.clear()
            Toast.makeText(this, "Cleared!", Toast.LENGTH_SHORT).show()
        }


    }
    override fun onStart() {
        super.onStart()
        buttonOk.isClickable = internet_access()
    }
    private fun internet_access(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting as Boolean
    }
    override fun onDestroy() {
        activityScope.cancel()
        super.onDestroy()
    }
    private fun getLocalHash(): String {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("hash", "").toString()
    }
}