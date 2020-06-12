package com.pmr.todolist.showlist

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pmr.todolist.R
import com.pmr.todolist.SettingsActivity
import com.pmr.todolist.data.DataProvider
import com.pmr.todolist.data.ItemProperties
import kotlinx.android.synthetic.main.show_list.*
import kotlinx.coroutines.*

class ShowListActivity : AppCompatActivity(), ItemTodoAdapter.ActionListener {
    private var listId = 0
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

        itemView.adapter = adapter
        itemView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        newItemButton.setOnClickListener {
            val text = newItemEdit.text.toString()

            if (text != "") {
                addItemToDo(newItemEdit.text.toString())
                newItemEdit.text.clear()
            }

            refreshItems()
        }
    }

    override fun onStart() {
        super.onStart()

        listId = intent.extras!!.getInt("listId")!!

        refreshItems()
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

    override fun onDestroy() {
        activityScope.cancel()
        super.onDestroy()
    }

    override fun onItemCheckChanged(item: ItemProperties, checked: Boolean) {
        activityScope.launch {
            withContext(Dispatchers.IO) {
                DataProvider.setItem(getHash(), listId, item.id, checked)
            }
        }
    }

    private fun addItemToDo(itemName: String) {
        activityScope.launch {
            withContext(Dispatchers.IO) {
                DataProvider.addItem(getHash(), listId, itemName)
                refreshItems()
            }
        }
    }

    private fun refreshItems() {
        activityScope.launch {
            val items = DataProvider.getItems(getHash(), listId)
            adapter.updateData(items)
        }
    }

    private fun getHash(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        return prefs.getString("hash", "")!!
    }
}