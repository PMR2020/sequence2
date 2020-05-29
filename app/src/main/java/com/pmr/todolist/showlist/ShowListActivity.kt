package com.pmr.todolist.showlist

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
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
    private var user: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_list)

        val list: RecyclerView = findViewById(R.id.item_view)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val nameEdit = findViewById<EditText>(R.id.new_item_edit)
        val okButton = findViewById<Button>(R.id.new_item_button)

        okButton.setOnClickListener {
            val text = nameEdit.text.toString()

            if (text != "") {
                addItemToDo(nameEdit.text.toString())
                nameEdit.text.clear()
            }

            refreshList()
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = getCurrentUser()

        if (user == "" || user == currentUser) {
            user = currentUser
            titre = intent.extras!!.getString("listTitle")!!
            refreshList()
        } else {
            finish() // User accessed the settings and changed users; back off
        }
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

        profile.getList(titre)?.rechercherItem(item.description)?.fait = checked

        writeProfile(profile)
    }

    private fun getCurrentUser(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        return prefs.getString("pseudo", "user")!!
    }

    private fun addItemToDo(itemName: String) {
        val profile = getProfile()

        for (list in profile.mesListeTodo) {
            if (list.titreListeTodo == titre && list.rechercherItem(itemName) == null) {
                list.lesItems.add(ItemToDo(itemName))
            }
        }

        writeProfile(profile)
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
        val profileJSON = prefs.getString(user, gson.toJson(ProfilListeTodo(user)))

        return gson.fromJson(profileJSON, ProfilListeTodo::class.java)
    }

    private fun refreshList() {
        val profile = getProfile()

        adapter.updateData(profile.getList(titre)?.lesItems ?: listOf())
    }
}