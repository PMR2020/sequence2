package com.example.todolistfinal.showlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistfinal.R
import com.example.todolistfinal.network.ItemDescription

class ShowListAdapter(private val actionListener: ActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet: MutableList<ItemDescription> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TodoListViewHolder(
            inflater.inflate(
                R.layout.card_item_lists_from_users,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = dataSet.size
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        Log.d("ItemAdapter", "onBindViewHolder $position")
        val todoListProperty = dataSet[position]
        when (holder) {
            is TodoListViewHolder -> {
                holder.bind(dataSet[position])
            }
        }
    }
    fun showData(newDataSet: List<ItemDescription>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }
    inner class TodoListViewHolder(itemView : View): RecyclerView.ViewHolder(itemView)
    { private val title: CheckBox = itemView.findViewById(
        R.id.checkBox
    )
        init {

            itemView.findViewById<CheckBox>(R.id.checkBox).setOnClickListener {
                val postPosition = adapterPosition
                if (postPosition != RecyclerView.NO_POSITION) {
                    val clickedPost = dataSet[postPosition]
                    val checkedPost = (it as CheckBox).isChecked
                    actionListener.onItemChecked(clickedPost, checkedPost)
                }

            }
        }
        fun bind(listofitems: ItemDescription){
            title.isChecked = listofitems.checked == 1
            title.text = listofitems.label.toString()
        }
    }
    interface ActionListener {
        fun onItemChecked(post: ItemDescription, checkBox: Boolean)
    }
}

//    companion object DiffCallback : DiffUtil.ItemCallback<ListfromUser>() {
//        override fun areItemsTheSame(
//            oldDataClass: ListfromUser,
//            newDataClass: ListfromUser
//        ): Boolean {
//            return oldDataClass === newDataClass
//        }

//        override fun getChangePayload(oldItem: ListfromUser, newItem: ListfromUser): Any? {
//            return super.getChangePayload(oldItem, newItem)
//        }

//        override fun areContentsTheSame(
//            oldDataClass: ListfromUser,
//            newDataClass: ListfromUser
//        ): Boolean {
//            return oldDataClass.id == newDataClass.id
//        }
//    }
