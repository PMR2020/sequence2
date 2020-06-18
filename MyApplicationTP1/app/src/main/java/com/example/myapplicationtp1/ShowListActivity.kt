package com.example.myapplicationtp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShowListActivity : AppCompatActivity() , ItemAdapterItem.ActionListener{

    private val adapter = newAdapter()
    private val dataSet = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)
        val list: RecyclerView = findViewById(R.id.list)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)


        repeat(2) {
            dataSet.add(Item("Tâche $it",false))
        }

        adapter.showData(dataSet)
    }
    fun addNewItem(view: View){
        //val intent = Intent(this,ItemListActivity::class.java)
        //startActivity(intent )
        var newItem= findViewById<EditText>(R.id.newItem).text.toString()
        Log.v("Tache = ", newItem)
        dataSet.add(Item("Tâche ${dataSet.size} ",false))
        adapter.showData(dataSet)

    }

    private fun newAdapter(): ItemAdapterItem {

        val adapter = ItemAdapterItem(
            actionListener = this
        )
        return adapter
    }

    override fun onItemClicked(Item: Item) {
        Log.d("MainActivity", "onItemClicked $Item")
        Toast.makeText(this,Item.name, Toast.LENGTH_LONG).show()
    }

}

