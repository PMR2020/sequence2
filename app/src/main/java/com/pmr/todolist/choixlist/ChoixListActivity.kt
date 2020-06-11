package com.pmr.todolist.choixlist

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
import com.pmr.todolist.data.DataProvider
import com.pmr.todolist.data.ListProperties
import com.pmr.todolist.showlist.ShowListActivity
import kotlinx.coroutines.*

class ChoixListActivity : AppCompatActivity(), ListAdapter.ActionListener {
    private val adapter: ListAdapter = ListAdapter(this)
    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("MainActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )

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
                addList(str)
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

    override fun onItemClicked(item: ListProperties) {
        val bundle = Bundle()
        bundle.putInt("listId", item.id)

        val intent = Intent(this, ShowListActivity::class.java)
        intent.putExtras(bundle)

        startActivity(intent)
    }

    override fun onDestroy() {
        activityScope.cancel()
        super.onDestroy()
    }

    private fun refreshLists() {
        activityScope.launch {
            Log.i("dbg", "user hash is ${getHash()}")
            val lists = DataProvider.getLists(getHash())
            adapter.updateData(lists)
        }
    }

    private fun getHash(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        return prefs.getString("hash", "")!!
    }

    private fun addList(title: String) {
        // val gson = Gson()

        // val profile = getProfile()
        // profile.ajouteListe(ListeToDo(title))
        // writeProfile(profile)
    }
}