package com.example.tp2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.coroutines.*


class MainActivity : GenericActivity(), View.OnClickListener {

    private var refBtnOK: Button? = null
    private var refPseudoInput: EditText? = null
    private var refPasswordInput: EditText? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        Declarations
         */
        refBtnOK = findViewById(R.id.OKBtnMain)
        refPseudoInput = findViewById(R.id.pseudoInputMain)
        refPasswordInput = findViewById(R.id.passwordInputMain)

        refBtnOK?.let { btn -> btn.setOnClickListener(this) }


        /*
        Check device connection
         */
        checkConnection()


        /*
        Auto connection
         */
        val pseudoPref : String? = prefs!!.getString("pseudo", "Pseudo")
        refPseudoInput?.setText(pseudoPref)
        val passwordPref : String? = prefs!!.getString("password", "password")
        refPasswordInput?.setText(passwordPref)

        // Auto connection
        if (pseudoPref != "" && passwordPref != "") {
            activityScope.launch {

                runCatching {
                    DataProvider.connection(pseudoPref!!, passwordPref!!)
                }.fold(
                    onSuccess = {
                        val editor : SharedPreferences.Editor = prefs!!.edit()
                        editor.putString("hash", it)
                        editor.commit()

                        val intent = Intent(this@MainActivity, ChoixListActivity::class.java)
                        startActivity(intent)
                    },
                    onFailure = {

                    }
                )
            }
        }
    }


    override fun onStart() {
        super.onStart()

        val pseudoPref : String? = prefs!!.getString("pseudo", "Pseudo")
        refPseudoInput?.setText(pseudoPref)
        val passwordPref : String? = prefs!!.getString("password", "password")
        refPasswordInput?.setText(passwordPref)
    }


    /*
    Check device connection
     */
    private fun checkConnection() {

        activityScope.launch {

            refBtnOK!!.isEnabled = false

            runCatching {
                DataProvider.testConnection()
            }.fold(
                onSuccess = {
                    refBtnOK!!.isEnabled = true
                },
                onFailure = {
                    if(it is retrofit2.HttpException){
                        refBtnOK!!.isEnabled = true
                    }
                }
            )
        }
    }


    /*
    OK Button Listener
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.OKBtnMain -> {
                val pseudo = refPseudoInput!!.text.toString()
                val password = refPasswordInput!!.text.toString()

                activityScope.launch {

                    runCatching {
                        DataProvider.connection(pseudo, password)
                    }.fold(
                        onSuccess = {
                            val editor : SharedPreferences.Editor = prefs!!.edit()
                            editor.putString("pseudo", pseudo)
                            editor.putString("password", password)
                            editor.putString("hash", it)
                            editor.commit()

                            val intent = Intent(this@MainActivity, ChoixListActivity::class.java)
                            startActivity(intent)
                        },
                        onFailure = {
                            Toast.makeText(MyApp.appContext, R.string.connectionRefused, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }





}
