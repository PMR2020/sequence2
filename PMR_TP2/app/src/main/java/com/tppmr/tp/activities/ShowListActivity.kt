package com.tppmr.tp.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tppmr.tp.DataProvider
import com.tppmr.tp.R
import com.tppmr.tp.adapters.ItemAdapter
import com.tppmr.tp.model.Item
import kotlinx.coroutines.*

class ShowListActivity : AppCompatActivity(), View.OnClickListener,
    ItemAdapter.ActionListener {

    private var listeLabel: String = ""
    private var listeId: Int = 0
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
        setContentView(R.layout.activity_show_list)
        setSupportActionBar(findViewById(R.id.toolbar))
        title = "Todo List"

        val intent = this.intent
        if (intent.hasExtra(getString(R.string.listeIdIntent)) && intent.hasExtra(getString(R.string.listeLabelIntent))) {
            listeId = intent.getIntExtra(getString(R.string.listeIdIntent), 0)
            listeLabel = intent.getStringExtra(getString(R.string.listeLabelIntent))
            title = listeLabel
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        urlBaseAPI = prefs!!.getString(getString(R.string.urlBaseAPIPrefs), "")
        pseudo = prefs!!.getString(getString(R.string.pseudoPrefs), "")
        hash = prefs!!.getString(getString(R.string.hashPrefs), "")

        btnOk = findViewById(R.id.okBtn)
        btnOk!!.setOnClickListener(this)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

    }

    override fun onResume() {
        super.onResume()
        activityScope.launch {
            updateItemsDataSet()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }

    private fun newAdapter(): ItemAdapter {

        val adapter = ItemAdapter(
            actionListener = this
        )
        return adapter
    }

    /**
     * Met à jour le dataset de l'adapter
     */
    private suspend fun updateItemsDataSet() {
        val items = DataProvider.getItemsFromApi(urlBaseAPI!!, hash!!, listeId)
        if (items != null) {
            adapter.showData(items)
        }
    }

    /**
     * Inflate le menu_activity_main de l'activité avec le layout menu_activity_main
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_activity_show_list, menu)
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
        R.id.action_delete_all_items -> {
            val toastDeleted =
                Toast.makeText(this, getString(R.string.itemsDeleted), Toast.LENGTH_SHORT)
            val toastNotDeleted =
                Toast.makeText(this, getString(R.string.itemsNotDeleted), Toast.LENGTH_LONG)
            activityScope.launch {
                val response =
                    DataProvider.deleteAllItemsInApi(urlBaseAPI!!, hash!!, listeId)
                if (response) {
                    updateItemsDataSet()
                    toastDeleted.show()
                } else {
                    toastNotDeleted.show()
                }
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    /**
     * Implémente le clique sur le bouton Ok pour enregistrer un nouvel item
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.okBtn -> {
                val editText: EditText = findViewById(R.id.editText)
                val newLabel = editText.text.toString()
                val toastCreated =
                    Toast.makeText(this, getString(R.string.itemCreated), Toast.LENGTH_SHORT)
                val toastNotCreated =
                    Toast.makeText(this, getString(R.string.itemNotCreated), Toast.LENGTH_LONG)
                if (newLabel.isNotEmpty()) {
                    editText.setText("")
                    activityScope.launch {
                        val newItem =
                            DataProvider.createItemInApi(urlBaseAPI!!, hash!!, listeId, newLabel)
                        if (newItem != null) {
                            updateItemsDataSet()
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
     * Implémente le clique long sur un item pour soit modifier le nom de l'item soit le supprimer
     */
    override fun onItemToDoLongClicked(item: Item) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle(getString(R.string.modifierOuSupprimerItemToDo))
            .setPositiveButton(R.string.modifier, DialogInterface.OnClickListener { dialog1, id ->
                val builder2 = AlertDialog.Builder(this)
                val inflater = this.layoutInflater
                val layoutDialog: View = inflater.inflate(R.layout.dialog_new, null)
                val textView: TextView = layoutDialog.findViewById(R.id.textView)
                textView.text = getString(R.string.enterNewItem)
                val newLabelEditText: EditText = layoutDialog.findViewById(R.id.editText2)
                newLabelEditText.setText(item.label)
                builder2.setView(layoutDialog)
                    .setPositiveButton(R.string.ok,
                        DialogInterface.OnClickListener { dialog2, id ->
                            val newLabel = newLabelEditText.text.toString()
                            val toastModified = Toast.makeText(
                                this,
                                getString(R.string.itemModified),
                                Toast.LENGTH_SHORT
                            )
                            val toastNotModified = Toast.makeText(
                                this,
                                getString(R.string.itemNotModified),
                                Toast.LENGTH_LONG
                            )
                            activityScope.launch {
                                val response = DataProvider.modifyItemInApi(
                                    urlBaseAPI!!,
                                    hash!!,
                                    listeId,
                                    item.id,
                                    newLabel
                                )
                                if (response != null) {
                                    updateItemsDataSet()
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
                    Toast.makeText(this, getString(R.string.itemDeleted), Toast.LENGTH_SHORT)
                val toastNotDeleted =
                    Toast.makeText(this, getString(R.string.itemNotDeleted), Toast.LENGTH_LONG)
                activityScope.launch {
                    val response =
                        DataProvider.deleteItemInApi(urlBaseAPI!!, hash!!, listeId, item.id)
                    if (response != null) {
                        updateItemsDataSet()
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
     * Implémente le clique sur la case à cocher à côté de l'item
     * pour changer l'état de l'item de fait à non fait ou inversement
     */
    override fun onItemToDoCheckBoxClicked(item: Item) {
        var newChecked = 0
        if (item.checked == 0) {
            newChecked = 1
        }
        val toastCheckedStateModified =
            Toast.makeText(this, getString(R.string.itemCheckedStateModified), Toast.LENGTH_SHORT)
        val toastNotCheckedStateModified =
            Toast.makeText(this, getString(R.string.itemNotCheckedStateModified), Toast.LENGTH_LONG)
        activityScope.launch {
            val response = DataProvider.modifyCheckedStateItemInApi(
                urlBaseAPI!!,
                hash!!,
                listeId,
                item.id,
                newChecked
            )
            if (response != null) {
                updateItemsDataSet()
                toastCheckedStateModified.show()
            } else {
                toastNotCheckedStateModified.show()
            }
        }
    }
}
