package com.example.application_to_do_list_2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application_to_do_list_2.DataProvider.checkItemFromApi
import com.example.application_to_do_list_2.DataProvider.createItemFromApi
import com.example.application_to_do_list_2.DataProvider.getItemsFromApi
import com.example.application_to_do_list_2.affichage.ItemsAdapter
import com.example.application_to_do_list_2.data.Prefs
import com.example.application_to_do_list_2.modele.Item
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_show_list.*
import kotlinx.coroutines.*

class ShowListActivity : AppCompatActivity(), View.OnClickListener, ItemsAdapter.ActionListener {
    val gson = Gson()
    var preferences : Prefs?=null
    var pseudo : String?= null
    var connecte = false
    var hash:String?=null

    // INFORMATIONS DE LA LISTE SELECTIONNEE
    var listeId:Int?=null
    var listeLabel : String? =null

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        //RECUPERATION DES INFORMATIONS DE LA LISTE SELECTIONNEE PAR BUNDLE
        var bundle : Bundle? =this.intent.extras
        listeId = if(bundle?.getInt("listeId")!! >= 0) bundle?.getInt("listeId") else null
        listeLabel = if(bundle?.getString("listeLabel")!="") bundle?.getString("listeLabel") else null

        this.setTitle(listeLabel)

        // RECUPERER L'ECOUTE DES CLICS SUR LE BOUTON
        btnItem.setOnClickListener(this)

        // RECUPERATIONS DES PREFERENCES
        preferences= Prefs(this)
        pseudo = gson.fromJson(preferences?.pseudo,String::class.java)
        hash=gson.fromJson(preferences?.hash,String::class.java)

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
                    DataProvider.getHashFromApi(
                        "tom",
                        "web"
                    )
                connecte=true
                majItems()
            }
            catch(e:Exception) {
                Toast.makeText(this@ShowListActivity, "Erreur de connexion. Veuillez réessayer", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // FONCTION DE MISE A JOUR DU RECYCLER VIEW
    private suspend fun majItems() {
        //Création vertical layout manger
        recyclerItems.layoutManager = LinearLayoutManager(this@ShowListActivity)

        if (connecte){
            var itemsAffiches = emptyList<Item>().toMutableList()
            if (hash!=null && hash!=""){ // VERIFICATIONS
                var rqt = "" // REQUETE
                try{
                    if (listeId != null) {
                        rqt = "lists/$listeId/items"
                        var items = getItemsFromApi(rqt, hash.toString())
                        for(i in items){
                            itemsAffiches.add(i) // ON EST LA AUSSI OBLIGE DE PASSER PAR UNE LISTE INTERMEDIAIRE
                        }
                    }
                }
                catch(e:Exception) {
                    Toast.makeText(this@ShowListActivity,"Erreur lors de l'accès à la liste. Vérifiez votre connexion.", Toast.LENGTH_SHORT).show()
                }
            }
            recyclerItems.adapter =
                ItemsAdapter(
                    itemsAffiches,
                    actionListener = this
                )
        }
        // MAJ EDT
        edtItem.setHint("Nouvel item")
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

    // CLIC SUR UN ITEM DE LA LISTE
    override fun onItemClicked(item: Item) {
        if (hash!=""){
            activityScope.launch{
                try{
                    var rqt = ""
                    if (listeId!=null) {
                        rqt = "lists/"+listeId+"/items/"+item.id
                        var item = checkItemFromApi(rqt,1-item.checked, hash.toString())
                        majItems()
                    }
                }
                catch(e:Exception){
                    Toast.makeText(this@ShowListActivity, "Erreur lors de la modification. Veuillez vérifier votre connexion.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // CLIC SUR LE BOUTON DE L'ITEM POUR CREER UN NOUVEL ITEM
    override fun onClick(v: View?) {
        if(v?.id==btnItem.id){
            if (hash!=""){
                activityScope.launch{
                    try{
                        var rqt = ""

                        if (listeId!=null) {
                            // VERIFICATION DU CHAMP TEXTE
                            if(edtItem.text.toString()==""){
                                Toast.makeText(this@ShowListActivity, "Veuillez saisir l'item", Toast.LENGTH_SHORT).show()
                            } else {
                                rqt = "lists/"+listeId+"/items"
                                var item = createItemFromApi(rqt,edtItem.text.toString(),hash.toString())
                                majItems()
                            }
                        }
                    }
                    catch(e:Exception){
                        Toast.makeText(this@ShowListActivity, "Erreur lors de la création de l'item. Veuillez vérifier votre connexion", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
