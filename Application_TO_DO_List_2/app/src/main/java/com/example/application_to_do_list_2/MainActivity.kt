package com.example.application_to_do_list_2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.application_to_do_list_2.data.Prefs
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    val gson = Gson()
    var preferences: Prefs? = null
    var pseudo : String?=null
    var mdp: String?=null

    var hash:String?=null

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.setTitle("Saisie de l'utilisateur")

        // RECUPERATIONS DES PREFERENCES ENREGISTREES DANS LA CLASSE PREFS
        preferences = Prefs(this)
        pseudo = gson.fromJson(preferences?.pseudo,String::class.java)
        edtPseudo.hint=pseudo

        btnPseudo.setOnClickListener(this)
        btnPseudo.isEnabled = false

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
                var hashtext =
                    DataProvider.getHashFromApi("tom","web")
                btnPseudo.isEnabled = true
            }
            catch(e:Exception) {
                Toast.makeText(this@MainActivity, "Erreur de connexion. Veuillez réessayer", Toast.LENGTH_SHORT).show()
                btnPseudo.isEnabled = false
            }
        }
    }

    // CREATION DU MENU ANDROID
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item.toString()=="Préférences"){
            // Enregister pseudo
            if (hash != null) {
                preferences?.enregistrerPref("hash", hash!!)
                preferences?.enregistrerPref("pseudo",pseudo!!)
            }
            newActivity(Intent(this,
                SettingsActivity::class.java))
        }
        return true
    }

    // FONCTION PERMETTANT D'ACCEDER A LA PROCHAINE ACTIVITE
    fun newActivity(intent : Intent) {
        // Récupération de l'user
        if(edtPseudo.text.toString()!=""){
            //ouverture Activité ChoixListActivity
            intent.putExtra("pseudo",edtPseudo.text.toString())
        }else{
            intent.putExtra("pseudo",edtPseudo.hint.toString())
        }
        //ouverture Activité ChoixListActivity
        startActivity(intent)
    }

    // REECRITURE DE LA FONCTION ONCLICK
    override fun onClick(v: View) {
        if(v?.id==btnPseudo.id){
            activityScope.launch{
                try {
                    // CONNEXION A L'API
                    pseudo = edtPseudo.text.toString()
                    mdp = edtMDP.text.toString()
                    hash =DataProvider.getHashFromApi( pseudo.toString(),mdp.toString()) //RECUPERATION DU NOUVEL HASH CORRESPONDANT AUX IDENTIFIANTS

                    if (hash != null) {
                        preferences?.enregistrerPref("hash", hash!!)
                        preferences?.enregistrerPref("pseudo",pseudo!!)
                        newActivity((Intent(this@MainActivity, ChoixListActivity::class.java)))
                    }
                }
                catch(e:Exception){
                    Toast.makeText(this@MainActivity, "Erreur dans le pseudo ou le mot de passe, veuillez réessayer", Toast.LENGTH_SHORT).show() //UX : on informe l'utilisateur que la requête avec les pseudos et mdp fournis ne fonctionne pas
                }
            }
        }
        else if (v?.id==edtPseudo.id) { //RECUPERATION DU PSEUDO LORS DU CLIC SUR LE CHAMP TEXT POUR PREDEFINIR LE PSEUDO ENREGISTRER
            pseudo = gson.fromJson(preferences?.pseudo,String::class.java)
            if (pseudo != "") edtPseudo?.setText(pseudo)
            edtPseudo?.selectAll()
        }
    }
}