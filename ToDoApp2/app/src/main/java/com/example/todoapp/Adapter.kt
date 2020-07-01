package com.example.todoapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.ToDoItem
import com.example.todoapp.data.ToDoList
import kotlinx.android.synthetic.main.todos_list.view.*
import kotlinx.android.synthetic.main.items_list.view.*

// NB : https://www.youtube.com/watch?v=69C1ljfDvl0 pour implémenter les click listener sur les recyclerView
class ToDoListAdapter(private val items: MutableList<ToDoList>, private val context: Context, private var onRvListener: ToDoListViewHolder.OnRvListener) :
    RecyclerView.Adapter<ToDoListViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        val v : View = LayoutInflater.from(context).inflate(R.layout.todos_list, parent, false)
        return ToDoListViewHolder(v, onRvListener)
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        holder.tvToDoList.text = items[position].title
    }

}

class ToDoListViewHolder (view: View, private val onRvListener: OnRvListener) : RecyclerView.ViewHolder(view), View.OnClickListener{
    val tvToDoList: TextView= view.tvToDoList
    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Log.e("TODOTEST", "position de l'adapteur :$adapterPosition")
        onRvListener.onRvItemClick(adapterPosition)
    }

    interface OnRvListener{
        fun onRvItemClick(position: Int)
    }
}


/*****************************Adapter et viewholder pour les items, même esprit*******************************************/
class ToDoItemAdapter(private val items: MutableList<ToDoItem>, private val context: Context, private val onRvListener: ToDoItemViewHolder.OnRvItemListener) :
    RecyclerView.Adapter<ToDoItemViewHolder>() {
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
        val v : View = LayoutInflater.from(context).inflate(R.layout.items_list, parent, false)
        return ToDoItemViewHolder(v, onRvListener)
    }

    override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
        holder.cbToDoList.text = items[position].description
        holder.cbToDoList.isChecked = items[position].done
    }
}

class ToDoItemViewHolder (view: View, private val onRvListener: OnRvItemListener) : RecyclerView.ViewHolder(view), View.OnClickListener{
    //TODO
    val cbToDoList : CheckBox = view.checkItem
    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Log.e("TODOTEST", "position de l'adapteur :$adapterPosition")
        onRvListener.onRvItemClick(adapterPosition)
    }

    interface OnRvItemListener{
        fun onRvItemClick(position: Int)
    }
}

