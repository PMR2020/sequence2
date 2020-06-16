package com.tppmr.tp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tppmr.tp.R
import com.tppmr.tp.model.Item

class ItemAdapter(private val actionListener: ActionListener) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val dataSet: MutableList<Item> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val label = dataSet[position].label
        val checked = dataSet[position].checked
        holder.bind(label, checked)
    }

    /**
     *  Updates the dataset containing the items of the lists with a new one and sort it accordingly
     */
    fun showData(newDataSet: MutableList<Item>) {
        val itemsChecked = mutableListOf<Item>()
        val itemsNotChecked = mutableListOf<Item>()
        newDataSet.forEach {
            if (it.checked == 0) {
                itemsNotChecked.add(it)
            } else {
                itemsChecked.add(it)
            }
        }
        dataSet.clear()
        dataSet.addAll(itemsNotChecked)
        dataSet.addAll(itemsChecked)
        notifyDataSetChanged()
    }

    /**
     * Implements functions specific to the items contained in the dataset
     */
    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        fun bind(label: String, checked: Int) {
            textView.text = label
            checkBox.isChecked = checked != 0
        }

        init {
            itemView.setOnLongClickListener {
                val positionItem = adapterPosition
                if (positionItem != RecyclerView.NO_POSITION) {
                    val item = dataSet[positionItem]
                    actionListener.onItemToDoLongClicked(item)
                }
                return@setOnLongClickListener true
            }

            checkBox.setOnClickListener {
                val positionItem = adapterPosition
                if (positionItem != RecyclerView.NO_POSITION) {
                    val item = dataSet[positionItem]
                    actionListener.onItemToDoCheckBoxClicked(item)
                }
            }
        }
    }

    interface ActionListener {
        fun onItemToDoLongClicked(item: Item)
        fun onItemToDoCheckBoxClicked(item: Item)
    }
}