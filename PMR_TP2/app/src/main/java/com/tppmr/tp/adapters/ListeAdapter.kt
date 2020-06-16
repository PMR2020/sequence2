package com.tppmr.tp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tppmr.tp.R
import com.tppmr.tp.model.Liste

class ListeAdapter(private val actionListener: ActionListener) :
    RecyclerView.Adapter<ListeAdapter.ListeViewHolder>() {

    private val dataSet: MutableList<Liste> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listeView = inflater.inflate(R.layout.liste, parent, false)
        return ListeViewHolder(listeView)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ListeViewHolder, position: Int) {
        val label = dataSet[position].label
        holder.bind(label)
    }

    /**
     *  Updates the dataset containing the user's lists with a new one
     */
    fun showData(newDataSet: MutableList<Liste>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    /**
     * Implements functions specific to the lists contained in the dataset
     */
    inner class ListeViewHolder(listeView: View) : RecyclerView.ViewHolder(listeView) {
        private val textView: TextView = listeView.findViewById(R.id.textView)
        fun bind(label: String) {
            textView.text = label
        }

        init {
            listeView.setOnClickListener {
                val positionListe = adapterPosition
                if (positionListe != RecyclerView.NO_POSITION) {
                    val liste = dataSet[positionListe]
                    actionListener.onListeClicked(liste)
                }
            }

            listeView.setOnLongClickListener() {
                val positionListe = adapterPosition
                if (positionListe != RecyclerView.NO_POSITION) {
                    val liste = dataSet[positionListe]
                    actionListener.onListeLongClicked(liste)
                }
                return@setOnLongClickListener true
            }
        }

    }

    interface ActionListener {
        fun onListeClicked(liste: Liste)
        fun onListeLongClicked(liste: Liste)
    }
}

