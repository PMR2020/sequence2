package com.pmr.todolist

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.pmr.todolist.choixlist.ChoixListActivity
import com.pmr.todolist.data.DataProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
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

        okButton.setOnClickListener {
            val pseudo = pseudoEdit.text.toString()
            val password = mdpEdit.text.toString()

            activityScope.launch {
                val hash = withContext(Dispatchers.IO) {
                    DataProvider.getHash(pseudo, password)
                }

                val prefs = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                val editor = prefs.edit()
                editor.putString("hash", hash)
                editor.apply()

                val intent = Intent(this@MainActivity, ChoixListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        DataProvider.updateApiUrl(getApiUrl())

        okButton.alpha = if (hasInternet()) 1f else 0.2f
        okButton.isClickable = hasInternet()
    }

    override fun onDestroy() {
        activityScope.cancel()
        super.onDestroy()
    }

    /* Taken from
     * https://developer.android.com/training/monitoring-device-state/connectivity-status-type#DetermineConnection
     */
    private fun hasInternet(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    private fun getApiUrl(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        return prefs.getString("api_url", "localhost/todo-api/api/")!!
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }
}
