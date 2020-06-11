package com.pmr.todolist.showlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.pmr.todolist.R
import com.pmr.todolist.data.ItemProperties

class ItemTodoAdapter(private val actionListener: ActionListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet: MutableList<ItemProperties> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ItemTodoViewHolder(inflater.inflate(R.layout.item_todo, parent, false))
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemTodoViewHolder).bind(dataSet[position])
    }

    fun updateData(newDataSet: List<ItemProperties>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    inner class ItemTodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)

        init {
            itemView.findViewById<CheckBox>(R.id.checkBox).setOnClickListener {
                val itemPosition = adapterPosition

                if (itemPosition != RecyclerView.NO_POSITION) {
                    val clickedItem = dataSet[itemPosition]
                    val checked = (it as CheckBox).isChecked
                    actionListener.onItemCheckChanged(clickedItem, checked)
                }
            }
        }

        fun bind(item: ItemProperties) {
            checkBox.isChecked = item.checked == 1
            checkBox.text = item.label
        }
    }

    interface ActionListener {
        fun onItemCheckChanged(item: ItemProperties, checked: Boolean)
    }
}