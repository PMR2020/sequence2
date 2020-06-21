package com.example.todolistfinal.choixlist

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistfinal.BasicActivity
import com.example.todolistfinal.DataProvider
import com.example.todolistfinal.R
import com.example.todolistfinal.SettingsActivity
import com.example.todolistfinal.network.ListfromUser
import com.example.todolistfinal.showlist.ShowListActivity
import kotlinx.android.synthetic.main.activity_choix_list.*
import kotlinx.coroutines.*

class ChoixListActivity : BasicActivity(),ChoixListAdapter.ActionListener {
    private var viewAdapter: ChoixListAdapter = newAdapter()
    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("ChoixListActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )
    private fun newAdapter(): ChoixListAdapter {
        val adapter = ChoixListAdapter(
            actionListener = this
        )
        return adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        super.setupToolBar()
        setContentView(R.layout.activity_choix_list);
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        //binding.setLifecycleOwner(this)
        val list: RecyclerView = findViewById(R.id.choice_list_recyclerView)
        list.adapter =viewAdapter
        list.layoutManager =LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        setListeners()
        LoadLists()
    }


    private fun LoadLists() {
        activityScope.launch {
            val newlists =
                DataProvider.getAllListsFromApi(getLocalHash())
            viewAdapter.showData(newlists)
        }
    }
    private fun getLocalHash(): String {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("hash", "").toString()
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


    private fun setListeners() {
        button_add_new_list.setOnClickListener {
            val listname = editTextList.text.toString()

            if (listname.toString() != "") {
                addnewList(listname)
                editTextList.text.clear()

                Toast.makeText(this, "Completed!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ShowListActivity::class.java)
//                intent.putExtra("liste", listname.toString())
//              intent.putExtra("pseudo", pseudo)
               startActivity(intent)
            }
        }
    }


    private fun addnewList(listname: String) {
        activityScope.launch {
            withContext(Dispatchers.IO) {
                DataProvider.addList(getLocalHash(), listname)
                LoadLists()
            }
        }
    }
    override fun onItemClicked(post: ListfromUser) {
        Log.d("ChoixListActivity", "onItemClicked $post")
        Toast.makeText(this, post.label, Toast.LENGTH_LONG).show()
        val bundle = Bundle()
        bundle.putInt("listId", post.id)

        val intent = Intent(this, ShowListActivity::class.java)
        intent.putExtras(bundle)

        startActivity(intent)
    }
    override fun onDestroy() {
        activityScope.cancel()
        super.onDestroy()
    }

}
