package com.example.todolistfinal.showlist

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
import com.example.todolistfinal.choixlist.ChoixListAdapter
import com.example.todolistfinal.network.ItemDescription
import com.example.todolistfinal.network.ListfromUser
import com.example.todolistfinal.network.ListofItems
import kotlinx.android.synthetic.main.activity_choix_list.*
import kotlinx.android.synthetic.main.activity_choix_list.editTextList
import kotlinx.android.synthetic.main.activity_show_list.*
import kotlinx.coroutines.*

 class ShowListActivity : BasicActivity(), ShowListAdapter.ActionListener {
        private var viewAdapter: ShowListAdapter = newAdapter()
        private var listId = 0
        private val activityScope = CoroutineScope(
            SupervisorJob()
                    + Dispatchers.Main
                    + CoroutineExceptionHandler { _, throwable ->
                Log.e("ShowListActivity", "CoroutineExceptionHandler : ${throwable.message}")
            }
        )
        private fun newAdapter(): ShowListAdapter {
            val adapter = ShowListAdapter(
                actionListener = this
            )
            return adapter
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_show_list);
            // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
            //binding.setLifecycleOwner(this)

            val list: RecyclerView = findViewById(R.id.show_list_recyclerView)
            list.adapter =viewAdapter
            list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
            setListeners()
            //LoadItems()
        }

     override fun onResume() {
         super.onResume()
         listId = intent.extras!!.getInt("listId")!!
         LoadItems()
     }
        private fun LoadItems() {
            activityScope.launch {
                val items =
                    DataProvider.getItems(getLocalHash(), listId)
                viewAdapter.showData(items)
            }
        }
        private fun getLocalHash(): String {
            return PreferenceManager.getDefaultSharedPreferences(this).getString("hash", "").toString()
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu, menu)

            return true
        }
     override fun onStart() {
         listId = intent.extras!!.getInt("listId")!!
         LoadItems()
         super.onStart()
     }
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)

            return super.onOptionsItemSelected(item)
        }


        private fun setListeners() {
            button_add_new_item.setOnClickListener {
                val itemname = editTextNewitem.text.toString()

                if (itemname.toString() != "") {
                    addNewItem(itemname)
                    editTextNewitem.text.clear()
                    Toast.makeText(this, "Completed!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, ShowListActivity::class.java)
//                intent.putExtra("liste", listname.toString())
//                intent.putExtra("pseudo", pseudo)
//                startActivity(intent)
                }
            }
        }


        private fun addNewItem(listname: String) {
            activityScope.launch {
                withContext(Dispatchers.IO) {
                    DataProvider.addItem(getLocalHash(), listId,listname)
                    LoadItems()
                }
            }
        }

     //        override fun onItemClicked(post: ListfromUser) {
//            Log.d("MainActivity", "onItemClicked $post")
//            Toast.makeText(this, post.label, Toast.LENGTH_LONG).show()
//        }
     override fun onItemChecked(post: ItemDescription, checkBox: Boolean) {
         Log.d("ShowListActivity", "onItemChecked $post")
         activityScope.launch {
             withContext(Dispatchers.IO) {
                 DataProvider.setItem(getLocalHash(), listId, post.id, checkBox)
             }
         }
         Toast.makeText(this, post.label, Toast.LENGTH_LONG).show()
     }
     override fun onDestroy() {
         activityScope.cancel()
         super.onDestroy()
     }
 }
