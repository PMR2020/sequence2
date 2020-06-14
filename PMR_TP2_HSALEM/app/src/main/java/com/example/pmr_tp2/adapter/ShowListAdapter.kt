package com.example.pmr_tp2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.pmr_tp2.R
import com.example.pmr_tp2.model.ItemToDo

class ShowListAdapter(private val showListListener: ShowListListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet: MutableList<ItemToDo> = mutableListOf()
    override fun getItemCount(): Int = dataSet.size

    fun showData(newDataSet: MutableList<ItemToDo>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ShowListViewHolder(inflater.inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShowListViewHolder -> holder.bind(itemToDo = dataSet[position])
        }
    }

    inner class ShowListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val item: CheckBox = view.findViewById(
            R.id.checkBoxItem
        )

        init {
            item.setOnCheckedChangeListener { buttonView, isChecked ->
                val itemPosition = adapterPosition
                if (itemPosition != RecyclerView.NO_POSITION) {
                    val clickedItem = dataSet[itemPosition]
                    showListListener.onItemClicked(itemPosition, isChecked)
                    Log.i("SNOW", "${clickedItem.description} is${if(isChecked) "";else " not"} checked")
                }

            }

        }


        fun bind(itemToDo: ItemToDo) {
            item.text = itemToDo.description
            item.isChecked = if (itemToDo.fait==1) true else false
        }
    }

    interface ShowListListener {
        fun onItemClicked(indexOfItem : Int, isChecked: Boolean)
    }
}