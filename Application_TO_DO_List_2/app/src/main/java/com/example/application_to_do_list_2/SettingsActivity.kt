package com.example.application_to_do_list_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.application_to_do_list_2.data.Prefs
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {
    val gson = Gson()
    var preferences: Prefs? = null
    var pseudo : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        this.setTitle("Préférences")

        // RECUPERATIONS DES PREFERENCES ENREGISTREES DANS LA CLASSE PREFS
        preferences = Prefs(this)
        pseudo = gson.fromJson(preferences?.pseudo,String::class.java)

        if (pseudo==null || pseudo==""){
            txtPseudo.text = "Aucun utilisateur"
        }
        else {
            txtPseudo.text = pseudo
        }

        // RECUPERATION DE L'URL
        edtURL.setText(DataProvider.URL)

        // ACTIVATION DU CLIC LISTENER
        btn_sauvegarder.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        if(v?.id==btn_sauvegarder.id){ // ENREGISTREMENTS DES PARAMETRES ET REDEMARRAGE DU SERVICEAPI
                DataProvider.URL= edtURL.text.toString()
                DataProvider.reStartService()
                Toast.makeText(this, "Url modifiée.", Toast.LENGTH_SHORT).show()
                onBackPressed() // RETOUR A MAIN ACTIVITY
            }

    }

}
