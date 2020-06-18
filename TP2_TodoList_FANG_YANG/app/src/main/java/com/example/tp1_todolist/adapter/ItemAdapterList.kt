package com.example.tp1_todolist.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tp1_todolist.R
import com.example.tp1_todolist.model.ItemList

class ItemAdapterList(private val actionlistener:ActionListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dataSet:MutableList<ItemList> = mutableListOf()

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i("ItemAdapterList", "createViewHolder")
        val inflater:LayoutInflater= LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_list,parent,false)
        return ItemViewHolderList(itemView)
    }

    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        Log.i("ItemAdapterList","onBindViewHolder")
        val itemList=dataSet[position]
        when (holder) {
            is ItemViewHolderList->{
                holder.bind(itemList)
         }
        }
    }

    fun showData(newDataSet : List<ItemList>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }



    inner class ItemViewHolderList(itemView: View):RecyclerView.ViewHolder(itemView){
        val tvList:TextView = itemView.findViewById(R.id.tvList)

        init {
            itemView.setOnClickListener(){
                val listPosition=adapterPosition
                if(listPosition!=RecyclerView.NO_POSITION){
                    val listTitle:String= tvList.text as String
                    val itemList=dataSet[listPosition]
                    val listId:Int?=itemList.id
//                    val listClickd:ItemList=dataSet[listPosition]
                    actionlistener.onItemClicked(listId,listTitle)
                    Log.i("ItemViewHolderList","click at $listTitle")
                    Log.i("ItemViewHolderList","listId is $listId")

                }

            }

        }
        fun bind(itemList: ItemList){
            tvList.text=itemList.title
        }
    }

    interface ActionListener{
        fun onItemClicked(idlist:Int?,listTitle:String)
    }


}