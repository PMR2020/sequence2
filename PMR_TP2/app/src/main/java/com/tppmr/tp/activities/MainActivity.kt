package com.tppmr.tp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.preference.PreferenceManager
import com.tppmr.tp.DataProvider
import com.tppmr.tp.R
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var prefs: SharedPreferences? = null
    private val defaultValueOfBaseUrl =
        "http://10.0.2.2:8888/todo-api/api/" // 10.0.2.2 in order to be connected in the application emulator to the device server instead of localhost or 127.0.0
    private val activityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )
    private var btnOk: Button? = null
    private var newOk: Button? = null
    private var isOnline: Boolean = false
    private var pseudoAutoCompleteTextView: AutoCompleteTextView? = null
    private var passwordEditText: EditText? = null
    private var baseUrl: String = defaultValueOfBaseUrl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = ""
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        with(prefs!!.edit()) {
            putString(getString(R.string.urlBaseAPIPrefs), defaultValueOfBaseUrl)
            apply()
        }
        btnOk = findViewById(R.id.okBtn)
        btnOk!!.setOnClickListener(this)
        newOk = findViewById(R.id.newBtn)
        newOk!!.setOnClickListener(this)
        isOnline = checkNetworkConnectivity()
        btnOk!!.isClickable = checkNetworkConnectivity()
        pseudoAutoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        passwordEditText = findViewById(R.id.editText)
        if (!isOnline) {
            Toast.makeText(
                this,
                getString(R.string.notOnlineMessage),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        baseUrl = prefs!!.getString(getString(R.string.urlBaseAPIPrefs), defaultValueOfBaseUrl)!!
        var pseudos: MutableList<String> = mutableListOf("")
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line, pseudos
        )
        pseudoAutoCompleteTextView!!.setAdapter(adapter)
        pseudoAutoCompleteTextView!!.setText(prefs!!.getString(getString(R.string.pseudoPrefs), ""))
        if(isOnline){
            activityScope.launch {
                val adminHash = DataProvider.getHashFromApi("admin", "admin", baseUrl!!)
                pseudos = DataProvider.getAllUsersPseudosFromApi(baseUrl, adminHash!!)!!
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }


    /**
     * Vérifie la disponibilité d'une interface réseau
     */
    fun checkNetworkConnectivity(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    /**
     * Inflate le menu_activity_main de l'activité avec le layout menu_activity_main
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    /**
     * Implémente le clique sur l'item du menu_activity_main pour passer à l'activité SettingsActivity
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_go_to_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    /**
     * Implémente le clique sur le bouton Ok pour enregistrer le pseudo dans les préférences et pour
     * passer à l'activité ChoixListActivity
     */
    override fun onClick(v: View?) {
        val pseudo = pseudoAutoCompleteTextView!!.text.toString()
        val passwordEntered = passwordEditText!!.text.toString()
        when (v?.id) {
            R.id.okBtn -> {
                val intent = Intent(this, ChoixListActivity::class.java)
                val toast = Toast.makeText(
                    this,
                    getString(R.string.falsePasswordOrIdentifierUnknown),
                    Toast.LENGTH_LONG
                )
                if (isOnline) {
                    activityScope.launch {
                        val response =
                            DataProvider.getHashFromApi(pseudo, passwordEntered, baseUrl!!)
                        if (response == null) {
                            toast.show()
                        } else {
                            with(prefs!!.edit()) {
                                putString(getString(R.string.pseudoPrefs), pseudo)
                                putString(getString(R.string.passwordPrefs), passwordEntered)
                                putString(getString(R.string.hashPrefs), response)
                                apply()
                            }
                            startActivity(intent)
                        }
                    }
                }
            }
            R.id.newBtn -> {
                if (pseudo.isNotEmpty() || passwordEntered.isNotEmpty()) {
                    val intent = Intent(this, ChoixListActivity::class.java)
                    val toast =
                        Toast.makeText(
                            this,
                            getString(R.string.impossibleToCreateUser),
                            Toast.LENGTH_LONG
                        )
                    if(isOnline) {
                        activityScope.launch {
                            val adminHash = DataProvider.getHashFromApi("admin", "admin", baseUrl!!)
                            val response = DataProvider.createUserInApi(
                                pseudo,
                                passwordEntered,
                                baseUrl!!,
                                adminHash!!
                            )
                            if (response != null) {
                                Log.i("test", "$response")
                                with(prefs!!.edit()) {
                                    putString(getString(R.string.pseudoPrefs), pseudo)
                                    putString(getString(R.string.passwordPrefs), passwordEntered)
                                    putString(getString(R.string.hashPrefs), response)
                                    apply()
                                }
                                startActivity(intent)
                            } else {
                                toast.show()
                            }

                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.noPseudoOfPasswordEntered),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    /**
     * Implémente le clique sur le bouton retour pour empêcher le retour sur l'activité précédente
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            false
        } else super.onKeyDown(keyCode, event)
    }

}

