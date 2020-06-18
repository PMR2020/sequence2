package com.example.application_to_do_list_2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application_to_do_list_2.affichage.ListesAdapter
import com.example.application_to_do_list_2.data.Prefs
import com.example.application_to_do_list_2.modele.Liste
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_choix_list.*
import kotlinx.coroutines.*


class ChoixListActivity : AppCompatActivity(), View.OnClickListener, ListesAdapter.ActionListener { // action listener pour le recyclerview et onclick pour le bouton normal
    val gson = Gson()
    var preferences : Prefs?=null
    var pseudo : String?= null
    var connecte = false


    var hash:String?=null

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        // RECUPERATIONS DES PREFERENCES ET DU HASH CORRESPONDANT A LA CONNEXION
        preferences= Prefs(this)
        pseudo = gson.fromJson(preferences?.pseudo,String::class.java)
        hash=gson.fromJson(preferences?.hash,String::class.java)
        this.setTitle("Listes enregistrées")


        //RECUPERER L'ECOUTE DES CLICS SUR LE BOUTON
        btnListe.setOnClickListener(this)

        // AJOUT DE LISTE NON EFFECTUE POUR SEQUENCE 2
        btnListe.visibility=View.INVISIBLE
        edtListe.visibility=View.INVISIBLE

        // ESSAI DE CONNEXION A L'API
        connexionAPI()
    }

    // REECRITURE DES FONCTIONS ONDESTROY ONRESUME
    override fun onDestroy() {
        activityScope.cancel()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        connexionAPI()
    }

    // CONNEXION A L'API
    private fun connexionAPI(){
        activityScope.launch{
            // ON SE CONNECTE AU RESEAU
            try{
                var hashtext =DataProvider.getHashFromApi("tom","web")
                connecte=true
                majListe()
            }
            catch(e:Exception) {
                Toast.makeText(this@ChoixListActivity, "Erreur de connexion. Veuillez réessayer", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // FONCTION DE MISE A JOUR DU RECYCLER VIEW
    private suspend fun majListe() {
        //Création vertical layout manager
        recyclerListe.layoutManager = LinearLayoutManager(this@ChoixListActivity)

        if (connecte){
            var listesAffichees = emptyList<Liste>().toMutableList()

            if (hash!=null && hash!=""){ // VERIFICATIONS
                try{
                    var lists =DataProvider.getListsFromApi(hash!!) // RECUPERATIONS DES LISTES
                    for(l in lists){
                        listesAffichees.add(l) // JE SUIS OBLIGE DE PASSER PAR UNE LISTE INTERMEDIAIRE POUR TRAITER L'EXCEPTION ET ACCEDER AU RECYCLERVIEW
                    }
                }
                catch(e:Exception) {
                    Toast.makeText(this@ChoixListActivity,"Erreur lors de l'accès aux listes. Vérifiez votre connexion.", Toast.LENGTH_SHORT).show()
                }

            }

            recyclerListe.adapter =ListesAdapter(listesAffichees,actionListener = this)
        }
        // MAJ EDT
        edtListe.setHint("Nouvelle liste")
    }

    // CREATION DU MENU ANDROID
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item.toString()=="Préférences"){
            startActivity(Intent(this,
                SettingsActivity::class.java))
        }
        return true
    }


    // CLIC SUR UNE LISTE
    override fun onListClicked(liste: Liste) {
        var ListActivity : Intent = Intent(this, ShowListActivity::class.java)
        ListActivity.putExtra("listeId",liste.id)
        ListActivity.putExtra("listeLabel",liste.label)
        startActivity(ListActivity) // ON CHANGE D'ACTIVITE EN TRANSMETTANT L'ID ET LE LABEL DE LA LISTE
    }

    // CLIC BOUTON --> NOUVELLE  LISTE NON TRAITE DANS LA SEQUENCE 2
    override fun onClick(v: View?) {
        if (v?.id == R.id.btnListe) {
        }
    }



}
