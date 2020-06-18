package com.example.tp1_todolist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.tp1_todolist.R
import com.example.tp1_todolist.model.ItemTask

class ItemAdapterTask(private val actionlistener:ActionListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val dataSetTask:MutableList<ItemTask> = mutableListOf()

    override fun getItemCount(): Int {
        return dataSetTask.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i("ItemViewHolderTask", "createViewHolder")
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_task, parent, false)
        return ItemViewHolderTask(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("ItemViewHolderTask", "onBindViewHolder")
        val itemTask = dataSetTask[position]
        when (holder) {
            is ItemViewHolderTask -> {
                holder.bind(itemTask)
            }
        }
    }

    fun showData(newDataSet : List<ItemTask>) {
        dataSetTask.clear()
        dataSetTask.addAll(newDataSet)
        notifyDataSetChanged()
    }

    inner class ItemViewHolderTask(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBoxTask: CheckBox = itemView.findViewById(R.id.checkBoxTask)

        init {
            checkBoxTask.setOnClickListener() {
                val taskPosition = adapterPosition
                if (taskPosition != RecyclerView.NO_POSITION) {
                    val taskDescription: String = checkBoxTask.text as String
                    Log.i("ItemViewHolderTask", "click at $taskDescription")
                    val taskClickd: ItemTask = dataSetTask[taskPosition]
                    val idTask:Int?=taskClickd.id
                    if (taskClickd.checked==0){
                        Log.i("ItemViewHolderTask", "before click,taskClickd check etat is ${taskClickd.checked}")
                        Log.i("ItemViewHolderTask", "before click,checkBoxTask check etat is ${checkBoxTask.isChecked}")
                        checkBoxTask.isChecked = true
                        taskClickd.checked=1
                        Log.i("ItemViewHolderTask", "after click,taskClickd check etat is ${taskClickd.checked}")
                        Log.i("ItemViewHolderTask", "after click,checkBoxTask check etat is ${checkBoxTask.isChecked}")
                        actionlistener.onItemClicked(idTask!!,taskClickd.checked)
                    }else{
                        Log.i("ItemViewHolderTask", "before click,taskClickd check etat is ${taskClickd.checked}")
                        Log.i("ItemViewHolderTask", "before click,checkBoxTask check etat is ${checkBoxTask.isChecked}")
                        checkBoxTask.isChecked = false
                        taskClickd.checked=0
                        Log.i("ItemViewHolderTask", "after click,taskClickd check etat is ${taskClickd.checked}")
                        Log.i("ItemViewHolderTask", "after click,checkBoxTask check etat is ${checkBoxTask.isChecked}")
                        actionlistener.onItemClicked(idTask!!,taskClickd.checked)
                    }
                    notifyDataSetChanged()
                }
            }

        }

        fun bind(itemTask: ItemTask) {
            checkBoxTask.text = itemTask.label
            checkBoxTask.setChecked(itemTask.checked==1)
        }
    }

    interface ActionListener {
        fun onItemClicked(idTask: Int,check:Int)
    }
}


