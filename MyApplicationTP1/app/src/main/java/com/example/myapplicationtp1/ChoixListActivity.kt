package com.example.myapplicationtp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplicationtp1.data.DataProvider
import com.example.myapplicationtp1.data.ListProperties
import kotlinx.coroutines.*

class ChoixListActivity : AppCompatActivity() , ItemAdapter.ActionListener{

    private val adapter = newAdapter()
    private val dataSet = mutableListOf<Choix>()
    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("ChoixListActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        val pseudo = intent.getStringExtra("Pseudo")
        Log.v("Pseudo",pseudo)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)
        val list: RecyclerView = findViewById(R.id.list)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)


        repeat(2) {
            dataSet.add(Choix("Tâche $it","TO DO $it"))
        }

        adapter.showData(dataSet)
    }
    fun addNewTask(view: View){
        //val intent = Intent(this,ChoixListActivity::class.java)
        //startActivity(intent )
        var newTask= findViewById<EditText>(R.id.newTask).text.toString()
        Log.v("Tache = ", newTask)
        dataSet.add(Choix("Tâche ${dataSet.size}  : ",newTask))
        adapter.showData(dataSet)

    }

    private fun newAdapter(): ItemAdapter {

        val adapter = ItemAdapter(
            actionListener = this
        )
        return adapter
    }

    override fun onItemClicked(Choix: Choix) {
        Log.d("MainActivity", "onItemClicked $Choix")
        Toast.makeText(this,Choix.title, Toast.LENGTH_LONG).show()
        val intent = Intent(this, ShowListActivity::class.java)
        startActivity(intent)
    }

    private fun getHash(): String {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("hash", "")!!
    }

    private fun createList(title: String) {
        activityScope.launch {
            withContext(Dispatchers.IO) {
                DataProvider.addList(getHash(), title)
            }

            refreshLists()
        }
    }
    private fun refreshLists() {
        activityScope.launch {
            val lists = withContext(Dispatchers.IO) {
                DataProvider.getLists(getHash())
            }

            dataSet.add(lists)
        }
    }

}

private fun <E> MutableList<E>.add(lists: List<ListProperties>) {
    TODO("Not yet implemented")
}

