package com.example.pmr_tp2.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pmr_tp2.R
import com.example.pmr_tp2.data_management.DataProvider
import com.example.pmr_tp2.data_management.ToDoAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "SNOW"
    var pseudoInput = "Name"

    override fun onRestart() {
        super.onRestart()
        finish();
        startActivity(getIntent());
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val selectedPseudo = prefs.getString("listPref", "Votre login")
        Log.i("SNOW", "Selected pseudo : " + selectedPseudo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Adding the activity as listener for the button
        val pseudoOKBtnReference = findViewById<Button>(R.id.pseudo_ok_btn)
        pseudoOKBtnReference.setOnClickListener(this)

        // Checking connection state
        var connected = checkIfConnected()
        Log.i("SNOW", "Connecté ? >> $connected")

        // Greying the OK button if not connected
        pseudoOKBtnReference.setEnabled(connected) // disabled if false

        // Using Preferences to determine the written name
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val selectedPseudo = prefs.getString("listPref", "tom")

        // Last pseudo becomes the one that was written
        pseudoInput = findViewById<EditText>(R.id.pseudo_input).getText().toString()
        findViewById<EditText>(R.id.pseudo_input).setText(selectedPseudo)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.pseudo_ok_btn -> {
                Log.i(TAG, "ok_pseudo pushed")

                pseudoInput = findViewById<EditText>(R.id.pseudo_input).getText().toString()
                val passwordInput = findViewById<EditText>(R.id.password_input).getText().toString()

                // Last pseudo becomes the one that was written
                findViewById<EditText>(R.id.pseudo_input).setText(pseudoInput)


                // --------- AUTHENTICATION WITH API ---------

                val prefs = PreferenceManager.getDefaultSharedPreferences(this)
                val dataProvider = DataProvider(this)
                val context: Context = this // will be used inside the coroutine scope

                activityScope.launch {

                    try {

                        dataProvider.putHashInPreferences(pseudoInput, passwordInput)
                        var hash: String? = prefs.getString("hash", "No Hash registered yet")
                        Log.i("SNOW", "Hash : " + hash)

                        // Going to ChoixListActivity, giving pseudo
                        val b = Bundle()
                        b.putString("login", pseudoInput)
                        b.putString("password", passwordInput)
                        val toChoixListActivity = Intent(context, ChoixListActivity::class.java)
                        toChoixListActivity.putExtras(b)

                        startActivity(toChoixListActivity)
                    } catch (e: Exception) { // Expecting an HTTP exception if user is unknown or if the request is not understood
                        Log.i("SNOW", "Exception caught : " + e.toString())
                        Toast.makeText(context, "Identifiants incorrects", Toast.LENGTH_SHORT).show()
                    }
                }
                // --------------------------------------------

            }
        }
    }

    /**
     * Checks if user is connected to the internet. Returns the corresponding boolean.
     */
    fun checkIfConnected(): Boolean {
        var connected = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connected = if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .state == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .state == NetworkInfo.State.CONNECTED
        ) {
            //connected to a network
            return true
        } else return false
    }


}
