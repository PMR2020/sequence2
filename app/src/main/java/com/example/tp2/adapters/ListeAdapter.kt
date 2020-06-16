package com.example.tp2.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.R
import com.example.tp2.lists.ListeToDo

class ListeAdapter(private val actionListener: ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet : MutableList<ListeToDo> = mutableListOf()


    fun setData(newDataSet : List<ListeToDo>?) {
        dataSet.clear()
        if (newDataSet != null) {
            dataSet.addAll(newDataSet)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list, parent, false)

        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).bind(dataSet[position])
    }

    inner class ItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val title : TextView = itemView.findViewById(R.id.titleList)

        init {
            itemView.setOnClickListener {
                val listPosition = adapterPosition
                if (listPosition != RecyclerView.NO_POSITION) {
                    val clickedList = dataSet[listPosition]
                    actionListener.onItemClicked(clickedList)
                }
            }
        }

        fun bind(listeToDo : ListeToDo) {
            title.text = listeToDo.titreListeToDo
        }
    }


    interface ActionListener {
        fun onItemClicked(listeToDo : ListeToDo)
    }

}