package com.pmr.todolist.showlist

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
import com.pmr.todolist.data.ItemProperties
import kotlinx.coroutines.*

class ShowListActivity : AppCompatActivity(), ItemTodoAdapter.ActionListener {
    private val adapter = ItemTodoAdapter(this)
    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("MainActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )

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

        refreshList()
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

    override fun onItemCheckChanged(item: ItemProperties, checked: Boolean) {
        val id = intent.extras!!.getInt("listId")!!

        activityScope.launch {
            withContext(Dispatchers.IO) {
                DataProvider.setItem(getHash(), id, item.id, checked)
            }
        }
    }

    override fun onDestroy() {
        activityScope.cancel()
        super.onDestroy()
    }

    private fun addItemToDo(itemName: String) {
        val id = intent.extras!!.getInt("listId")!!

        activityScope.launch {
            withContext(Dispatchers.IO) {
                DataProvider.addItem(getHash(), id, itemName)
                refreshList()
            }
        }

    }

    private fun getHash(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        return prefs.getString("hash", "")!!
    }

    private fun refreshList() {
        val id = intent.extras!!.getInt("listId")!!

        activityScope.launch {
            val items = DataProvider.getItems(getHash(), id)
            adapter.updateData(items)
        }
    }
}