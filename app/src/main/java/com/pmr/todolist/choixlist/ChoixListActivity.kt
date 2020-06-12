package com.pmr.todolist.choixlist

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
import com.pmr.todolist.data.ListProperties
import com.pmr.todolist.showlist.ShowListActivity
import kotlinx.android.synthetic.main.choix_list.*
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

        listView.adapter = adapter
        listView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        newListButton.setOnClickListener {
            val listName = newListEdit.text.toString()

            if (listName != "") {
                addList(listName)
                newListEdit.text.clear()
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
            val lists = withContext(Dispatchers.IO) {
                DataProvider.getLists(getHash())
            }

            adapter.updateData(lists)
        }
    }

    private fun getHash(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        return prefs.getString("hash", "")!!
    }

    private fun addList(title: String) {
        activityScope.launch {
            withContext(Dispatchers.IO) {
                DataProvider.addList(getHash(), title)
            }

            refreshLists()
        }
    }
}