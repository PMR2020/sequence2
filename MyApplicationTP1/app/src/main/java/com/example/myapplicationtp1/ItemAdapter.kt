package com.example.myapplicationtp1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(
    private val actionListener: ActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet: MutableList<Choix> = mutableListOf()

    override fun getItemCount(): Int = dataSet.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            BIG_VIEW_TYPE
        } else {
            SMALL_VIEW_TYPE
        }
    }

    fun showData(newDataSet: MutableList<Choix>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            BIG_VIEW_TYPE -> {
                ItemViewHolder(inflater.inflate(R.layout.item, parent, false))
            }
            SMALL_VIEW_TYPE -> {
                ItemViewHolder(inflater.inflate(R.layout.item, parent, false))
            }
            else -> {
                throw IllegalArgumentException("View type $viewType not supported")
            }
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("ItemAdapter", "onBindViewHolder $position")
        when (holder) {
            is BigItemViewHolder -> {
                holder.bind(dataSet[position])
            }
            is ItemViewHolder -> {
                holder.bind(dataSet[position])
            }
        }

    }


    inner class BigItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(
            R.id.title
        )

        init {

            itemView.setOnClickListener {
                val ChoixPosition = adapterPosition
                if (ChoixPosition != RecyclerView.NO_POSITION) {
                    val clickedChoix = dataSet[ChoixPosition]
                    actionListener.onItemClicked(clickedChoix)
                }

            }
        }

        fun bind(Choix: Choix) {
            title.text = "${Choix.title} & ${Choix.subTitle}"
        }


    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(
            R.id.title
        )
        private val subTitle: TextView = itemView.findViewById(R.id.subTitle)

        init {

            itemView.setOnClickListener {
                val ChoixPosition = adapterPosition
                if (ChoixPosition != RecyclerView.NO_POSITION) {
                    val clickedChoix = dataSet[ChoixPosition]
                    actionListener.onItemClicked(clickedChoix)
                }

            }
        }

        fun bind(Choix: Choix) {
            title.text = Choix.title
            subTitle.text = Choix.subTitle
        }
    }

    interface ActionListener {
        fun onItemClicked(Choix: Choix)
    }

    companion object {
        private const val SMALL_VIEW_TYPE = 1
        private const val BIG_VIEW_TYPE = 2
    }
}