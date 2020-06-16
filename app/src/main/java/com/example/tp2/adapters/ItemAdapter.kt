package com.example.tp2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.R
import com.example.tp2.lists.ItemToDo

class ItemAdapter(private val actionListener: ItemAdapter.ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val dataSet : MutableList<ItemToDo> = mutableListOf()


    fun setData(newDataSet : List<ItemToDo>?) {
        dataSet.clear()
        if (newDataSet != null) {
            dataSet.addAll(newDataSet)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(dataSet[position])
    }

    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val descItem : CheckBox = itemView.findViewById(R.id.descItem)

        init {
            val checkItemView : CheckBox = (itemView as ViewGroup).getChildAt(0) as CheckBox
            checkItemView.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                val listPosition = adapterPosition
                if (listPosition != RecyclerView.NO_POSITION) {
                    val clickedList = dataSet[listPosition]
                    actionListener.onItemClicked(clickedList, b)
                }
            }
        }

        fun bind(itemToDo : ItemToDo) {
            descItem.text = itemToDo.description
            descItem.isChecked = itemToDo.fait
        }
    }


    interface ActionListener {
        fun onItemClicked(itemToDo : ItemToDo, value : Boolean)
    }

}
