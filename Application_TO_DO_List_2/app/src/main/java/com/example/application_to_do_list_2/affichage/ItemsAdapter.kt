package com.example.application_to_do_list_2.affichage

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application_to_do_list_2.R
import com.example.application_to_do_list_2.modele.Item
import kotlinx.android.synthetic.main.element_item.view.*

class ItemsAdapter(val items: MutableList<Item>, private var actionListener: ActionListener) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>(){
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        init{
                view.checkItem.setOnClickListener{
                    val position = adapterPosition
                    if(position!=RecyclerView.NO_POSITION) {
                        val itemClic : Item = items[position]
                        actionListener.onItemClicked(itemClic)

                    }
                }
        } //Valeur du checkItem
        val nomItem = view.checkItem
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val lineView = LayoutInflater.from(parent.context).inflate(R.layout.element_item, parent, false)
        return ViewHolder(lineView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.nomItem?.text= items[position].label
        holder?.nomItem?.isChecked = (items[position].checked ==1)
    }

    interface ActionListener{
        fun onItemClicked(item: Item){
        }
    }
}