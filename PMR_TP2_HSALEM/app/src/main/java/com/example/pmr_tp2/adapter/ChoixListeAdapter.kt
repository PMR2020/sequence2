package com.example.pmr_tp2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pmr_tp2.R
import com.example.pmr_tp2.model.ListeToDo

class ChoixListeAdapter(private val listOfListsListener: ListOfListsListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() { // TODO : insert actionListener as parameter for the adapter

    private val dataSet: MutableList<ListeToDo> = mutableListOf()

    override fun getItemCount(): Int {
        Log.i("SNOW", "DATASET SIZE : ${dataSet.size}")
        return dataSet.size
    }
    // Can override getItemViewType to manage different types of views within the same RecyclerView

    /**
     * Replaces the current data with the new data passed as argument
     * Notifies the RecyclerView that data has changed so that it updates
     */
    fun showData(newDataSet: MutableList<ListeToDo>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
        Log.i("SNOW", "showData called")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i("SNOW", "onCreateViewHolder called")
        val inflater = LayoutInflater.from(parent.context)
        return ChoixListeViewHolder(inflater.inflate(R.layout.list_of_lists_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("SNOW", "onBindViewHolder called")
        when (holder) {
            is ChoixListeViewHolder -> holder.bind(dataSet[position].titreListeToDo)
        }
    }

    inner class ChoixListeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val listName: TextView = view.findViewById(
            R.id.choixliste_list_name
        )

        init {
            view.setOnClickListener {
                val listPosition = adapterPosition
                if (listPosition != RecyclerView.NO_POSITION) {
                    val clickedList = dataSet[listPosition]
                    listOfListsListener.onListClicked(clickedList,listPosition)

                    Log.i("SNOW", "List ${dataSet[listPosition].titreListeToDo} clicked")
                }
            }
        }

        fun bind(listNameString: String) {
            Log.i("SNOW", "bind called")
            listName.text = listNameString
        }
    }

    interface ListOfListsListener {
        fun onListClicked(listeToDo: ListeToDo, indexOfList : Int)
    }
}