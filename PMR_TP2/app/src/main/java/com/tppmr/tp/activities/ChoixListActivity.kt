package com.tppmr.tp.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tppmr.tp.DataProvider
import com.tppmr.tp.R
import com.tppmr.tp.adapters.ListeAdapter
import com.tppmr.tp.model.Liste
import kotlinx.coroutines.*

class ChoixListActivity : AppCompatActivity(), View.OnClickListener,
    ListeAdapter.ActionListener {

    private var prefs: SharedPreferences? = null
    private var urlBaseAPI: String? = null
    private var pseudo: String? = null
    private var hash: String? = null
    private var btnOk: Button? = null
    private var recyclerView: RecyclerView? = null
    private var adapter = newAdapter()
    private val activityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        urlBaseAPI = prefs!!.getString(getString(R.string.urlBaseAPIPrefs), "")
        pseudo = prefs!!.getString(getString(R.string.pseudoPrefs), "")
        hash = prefs!!.getString(getString(R.string.hashPrefs), "")

        title = "${getString(R.string.listes)} $pseudo"

        btnOk = findViewById(R.id.okBtn)
        btnOk!!.setOnClickListener(this)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onResume() {
        super.onResume()
        Log.i("ChoixListActivity", "$urlBaseAPI & $hash")
        activityScope.launch {
            updateListesDataSet()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }


    private fun newAdapter(): ListeAdapter {

        val adapter = ListeAdapter(
            actionListener = this
        )
        return adapter
    }

    /**
     * Met à jour le dataset de l'adapter
     */
    private suspend fun updateListesDataSet() {
        val listes = DataProvider.getListsFromApi(urlBaseAPI!!, hash!!)
        if (listes != null) {
            adapter.showData(listes)
        }
    }

    /**
     * Inflate le menu_activity_main de l'activité avec le layout menu_activity_main
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_activity_choix_list, menu)
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
        R.id.action_delete_all_lists -> {
            val toastDeleted =
                Toast.makeText(this, getString(R.string.listsDeleted), Toast.LENGTH_SHORT)
            val toastNotDeleted =
                Toast.makeText(this, getString(R.string.listsNotDeleted), Toast.LENGTH_LONG)
            activityScope.launch {
                val response = DataProvider.deleteAllListsInApi(urlBaseAPI!!,hash!!)
                if(response){
                    updateListesDataSet()
                    toastDeleted.show()
                }
                else{
                    toastNotDeleted.show()
                }
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    /**
     * Implémente le clique sur le bouton Ok pour enregistrer le nom de la nouvelle liste
     * et demander le nom du premier item de la liste
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.okBtn -> {
                val editText: EditText = findViewById(R.id.editText)
                val newLabel = editText.text.toString()
                val toastCreated =
                    Toast.makeText(this, getString(R.string.listCreated), Toast.LENGTH_SHORT)
                val toastNotCreated =
                    Toast.makeText(this, getString(R.string.listNotCreated), Toast.LENGTH_LONG)
                if (newLabel.isNotEmpty()) {
                    editText.setText("")
                    activityScope.launch {
                        val response =
                            DataProvider.createListInApi(urlBaseAPI!!, hash!!, newLabel)
                        if (response != null) {
                            updateListesDataSet()
                            toastCreated.show()
                        } else {
                            toastNotCreated.show()
                        }
                    }
                }
            }
        }
    }

    /**
     * Implémente le clique simple sur une liste pour passer à l'activité ShowListActivity qui
     * affiche tous les items de la liste
     */
    override fun onListeClicked(liste: Liste) {
        val intent = Intent(this, ShowListActivity::class.java)
        intent.putExtra(getString(R.string.listeIdIntent), liste.id)
        intent.putExtra(getString(R.string.listeLabelIntent), liste.label)
        startActivity(intent)
    }

    /**
     * Implémente le clique long sur une liste pour soit modifier le nom de la liste soit la supprimer
     */
    override fun onListeLongClicked(liste: Liste) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle(getString(R.string.modifierOuSupprimerListeToDo))
            .setPositiveButton(R.string.modifier, DialogInterface.OnClickListener { dialog1, id ->
                val builder2 = AlertDialog.Builder(this)
                val inflater = this.layoutInflater
                val layoutDialog: View = inflater.inflate(R.layout. dialog_new, null)
                val textView: TextView = layoutDialog.findViewById(R.id.textView)
                textView.text = getString(R.string.enterNameofTodoList)
                val newLabelEditText: EditText = layoutDialog.findViewById(R.id.editText2)
                newLabelEditText.setText(liste.label)
                builder2.setView(layoutDialog)
                    .setPositiveButton(R.string.ok,
                        DialogInterface.OnClickListener { dialog2, id ->
                            val newLabel = newLabelEditText.text.toString()
                            val toastModified = Toast.makeText(
                                this,
                                getString(R.string.listModified),
                                Toast.LENGTH_SHORT
                            )
                            val toastNotModified = Toast.makeText(
                                this,
                                getString(R.string.listeNotModified),
                                Toast.LENGTH_LONG
                            )
                            activityScope.launch {
                                val response = DataProvider.modifyListInApi(
                                    urlBaseAPI!!,
                                    hash!!,
                                    liste.id,
                                    newLabel
                                )
                                if (response != null) {
                                    updateListesDataSet()
                                    toastModified.show()
                                } else {
                                    toastNotModified.show()
                                }
                            }
                            dialog1.cancel()
                            dialog2.cancel()
                        })
                    .setNegativeButton(R.string.cancel,
                        DialogInterface.OnClickListener { dialog2, id ->
                            dialog2.cancel()
                        })
                    .create()
                    .show()
                dialog1.cancel()
            })
            .setNegativeButton(R.string.supprimer, DialogInterface.OnClickListener { dialog1, id ->
                val toastDeleted =
                    Toast.makeText(this, getString(R.string.listDeleted), Toast.LENGTH_SHORT)
                val toastNotDeleted =
                    Toast.makeText(this, getString(R.string.listNotDeleted), Toast.LENGTH_LONG)
                activityScope.launch {
                    val response = DataProvider.deleteListInApi(urlBaseAPI!!, hash!!, liste.id)
                    if (response != null) {
                        updateListesDataSet()
                        toastDeleted.show()
                    } else {
                        toastNotDeleted.show()
                    }
                }
                dialog1.cancel()
            })
            .setNeutralButton(R.string.cancel, DialogInterface.OnClickListener { dialog1, id ->
                dialog1.cancel()
            })
            .create()
            .show()
    }

    /**
     * Implémente le clique sur le bouton retour pour forcer le passage à l'activité MainActivity
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(Intent(this, MainActivity::class.java))
            false
        } else super.onKeyDown(keyCode, event)
    }
}
