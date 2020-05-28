package com.pmr.todolist.showlist

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pmr.todolist.R

class ShowListActivity : Activity(),
    ItemTodoAdapter.ActionListener {
    private val adapter: ItemTodoAdapter = newItemTodoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_list)

        val list: RecyclerView = findViewById(R.id.item_view)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val dataSet = mutableListOf<ItemToDo>()

        repeat(100) {
            dataSet.add(
                ItemToDo(
                    "Item $it",
                    it % 2 == 0
                )
            )
        }

        adapter.updateData(dataSet)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onItemClicked(item: ItemToDo) {
        item.fait = !item.fait
        // TODO?: adapter.updateData()
    }

    private fun newItemTodoAdapter(): ItemTodoAdapter {
        return ItemTodoAdapter(this)
    }
}