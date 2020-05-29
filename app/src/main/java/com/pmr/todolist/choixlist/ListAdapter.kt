package com.pmr.todolist.choixlist

import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pmr.todolist.R

class ListAdapter(private val actionListener: ActionListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet: MutableList<ListeToDo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ListViewHolder(inflater.inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListViewHolder).bind(dataSet[position])
    }

    fun updateData(newDataSet: List<ListeToDo>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.liste_title)

        init {
            itemView.setOnClickListener {
                val itemPosition = adapterPosition

                if (itemPosition != RecyclerView.NO_POSITION) {
                    val clickedItem = dataSet[itemPosition]
                    actionListener.onItemClicked(clickedItem)
                }
            }
        }

        fun bind(item: ListeToDo) {
            title.text = item.titreListeTodo
        }
    }

    interface ActionListener {
        fun onItemClicked(item: ListeToDo)
    }
}