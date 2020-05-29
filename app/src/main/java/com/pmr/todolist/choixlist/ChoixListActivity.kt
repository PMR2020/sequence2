package com.pmr.todolist.choixlist

import android.app.Activity
import android.content.Context
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
import com.pmr.todolist.showlist.ItemToDo
import com.pmr.todolist.showlist.ShowListActivity

class ChoixListActivity : AppCompatActivity(), ListAdapter.ActionListener {
    private val adapter: ListAdapter = ListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choix_list)

        val list = findViewById<RecyclerView>(R.id.choix_view)
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val okButton = findViewById<Button>(R.id.new_list_button)
        val nameEdit = findViewById<EditText>(R.id.new_list_edit)

        okButton.setOnClickListener {
            val str = nameEdit.text.toString()

            if (str != "") {
                newList(str)
                nameEdit.text.clear()
            }

            refreshLists()
        }
    }

    override fun onStart() {
        super.onStart()

        refreshLists()
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

    override fun onItemClicked(item: ListeToDo) {
        val bundle = Bundle()
        bundle.putString("listTitle", item.titreListeTodo)

        val intent = Intent(this, ShowListActivity::class.java)
        intent.putExtras(bundle)

        startActivity(intent)
    }

    private fun refreshLists() {
        adapter.updateData(getProfile().mesListeTodo.toList())
    }

    private fun writeProfile(profile: ProfilListeTodo) {
        val gson = Gson()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = prefs.edit()

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

    private fun newList(title: String) {
        val gson = Gson()

        val profile = getProfile()
        profile.ajouteListe(ListeToDo(title))
        writeProfile(profile)
    }
}