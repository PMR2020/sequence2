package com.pmr.todolist.showlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.pmr.todolist.ProfilListeTodo
import com.pmr.todolist.R
import com.pmr.todolist.SettingsActivity

class ShowListActivity : AppCompatActivity(), ItemTodoAdapter.ActionListener {
    private val adapter = ItemTodoAdapter(this)
    private var titre: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_list)

        val list: RecyclerView = findViewById(R.id.item_view)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val nameEdit = findViewById<EditText>(R.id.new_item_edit)
        val okButton = findViewById<Button>(R.id.new_item_button)

        okButton.setOnClickListener {
            val itemText = nameEdit.text.toString()
            ajouteItem(itemText)
            refreshList()
        }
    }

    override fun onStart() {
        super.onStart()

        titre = intent.extras!!.getString("listTitle")!!

        refreshList()
    }

    private fun ajouteItem(name: String) {
        val profile = getProfile()

        for (list in profile.mesListeTodo) {
            if (list.titreListeTodo == titre) {
                list.lesItems.add(ItemToDo(name))
            }
        }

        writeProfile(profile)
    }

    private fun writeProfile(profile: ProfilListeTodo) {
        val gson = Gson()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()

        Log.i("dbg/showlist", "wrote to $titre, ${gson.toJson(profile)}")
        editor.putString(profile.login, gson.toJson(profile))
        editor.commit()
    }

    private fun getProfile(): ProfilListeTodo {
        val gson = Gson()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val user = prefs.getString("pseudo", "user")!!
        var profileJSON = prefs.getString(user, gson.toJson(ProfilListeTodo(user)))

        return gson.fromJson(profileJSON, ProfilListeTodo::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }

    override fun onItemCheckChanged(item: ItemToDo, checked: Boolean) {
        val profile = getProfile()

        profile.mesListeTodo.iterator().forEach {
            if (it.titreListeTodo == titre) {
                it.lesItems.iterator().forEach {
                    if (it.description == item.description) {
                        it.fait = !it.fait
                    }
                }
            }
        }

        writeProfile(profile)
    }

    override fun onItemClicked(item: ItemToDo) {
        TODO("Not yet implemented")
    }

    private fun refreshList() {
        val profile = getProfile()

        for (list in profile.mesListeTodo) {
            if (list.titreListeTodo == titre) {
                adapter.updateData(list.lesItems)
            }
        }
    }
}