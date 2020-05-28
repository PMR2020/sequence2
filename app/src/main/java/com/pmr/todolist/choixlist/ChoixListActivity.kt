package com.pmr.todolist.choixlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pmr.todolist.R
import com.pmr.todolist.showlist.ItemToDo
import com.pmr.todolist.showlist.ShowListActivity

class ChoixListActivity : Activity(), ListAdapter.ActionListener {
    private val adapter: ListAdapter = ListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choix_list)

        val list: RecyclerView = findViewById(R.id.choix_view)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val dataSet = mutableListOf<ListeToDo>()

        repeat(100) {
            dataSet.add(
                ListeToDo(
                    "Liste $it"
                )
            )
        }

        adapter.updateData(dataSet)
    }

    override fun onStart() {
        super.onStart()

        val okButton = findViewById<Button>(R.id.new_list_button)
        val nameEdit = findViewById<EditText>(R.id.new_list_edit)

        okButton.setOnClickListener {
            val str = nameEdit.text.toString()
            // TODO add list
            // adapter.updateData()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onItemClicked(item: ListeToDo) {
        Log.i("dbg", "list clicked")

        val bundle = Bundle()
        bundle.putString("listTitle", item.titreListeTodo)

        val intent = Intent(this, ShowListActivity::class.java)
        intent.putExtras(bundle)

        startActivity(intent)
    }

    private fun newList(title: String) {

    }
}