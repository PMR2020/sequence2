package com.example.todolistfinal.choixlist



import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolistfinal.R
import com.example.todolistfinal.network.ListfromUser
import com.example.todolistfinal.databinding.CardItemListsFromUsersBinding

class ChoixListAdapter(
    private val actionListener: ActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet: MutableList<ListfromUser> = mutableListOf()

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
        fun showData(newDataSet: List<ListfromUser>) {
            dataSet.clear()
            dataSet.addAll(newDataSet)
            notifyDataSetChanged()
        }
    inner class TodoListViewHolder(itemView : View):RecyclerView.ViewHolder(itemView)
    { private val title: TextView = itemView.findViewById(
        R.id.title_card_view
    )
        init {

            itemView.setOnClickListener {
                val postPosition = adapterPosition
                if (postPosition != RecyclerView.NO_POSITION) {
                    val clickedPost = dataSet[postPosition]
                    actionListener.onItemClicked(clickedPost)
                }

            }
        }
        fun bind(listfromuser: ListfromUser){
            title.text = listfromuser.label
        }
    }
    interface ActionListener {
        fun onItemClicked(post: ListfromUser)
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
