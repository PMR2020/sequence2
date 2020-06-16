package com.example.tp2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.adapters.ItemAdapter
import com.example.tp2.lists.ItemToDo
import kotlinx.coroutines.*

class ShowListActivity : GenericActivity(), ItemAdapter.ActionListener, View.OnClickListener {

    private var adapter : ItemAdapter? = null
    private var refBtnOK: Button? = null
    private var refListInput: EditText? = null
    private var listId : Long? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        /*
        Declarations
         */
        refBtnOK = findViewById(R.id.OKBtnShowList)
        refListInput = findViewById(R.id.listInputShowList)
        adapter = newAdapter()

        refBtnOK?.let { btn -> btn.setOnClickListener(this) }


        /*
        Get info from ChoixListActivity
         */
        val bundle = this.intent.extras
        listId = bundle!!.getLong("id")

        /*
        RecyclerView
         */
        val list : RecyclerView = findViewById(R.id.listOfItem)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        updateDisplay()
    }

    private fun updateDisplay() {
        activityScope.launch {

            runCatching {
                val hash : String? = prefs!!.getString("hash", "")
                DataProvider.getItems(hash!!, listId!!)
            }.fold(
                onSuccess = {
                    adapter!!.setData(it)
                },
                onFailure = {
                    Toast.makeText(MyApp.appContext, R.string.recupItemsError, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun newAdapter() : ItemAdapter = ItemAdapter(actionListener = this)

    /*
    Item listener
     */
    override fun onItemClicked(itemToDo: ItemToDo, value : Boolean) {

        activityScope.launch {

            val hash : String? = prefs!!.getString("hash", "")
            val fait : Int = if (value) 1 else 0
            DataProvider.alterItem(hash!!, listId!!, itemToDo.id, fait)
        }
    }

    /*
    OK Button listener
    */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.OKBtnShowList -> {

                activityScope.launch {

                    runCatching {
                        val hash : String? = prefs!!.getString("hash", "")
                        DataProvider.newItem(hash!!, listId!!, refListInput!!.text.toString())
                    }.fold(
                        onSuccess = {
                            updateDisplay()
                        },
                        onFailure = {
                            Toast.makeText(MyApp.appContext, R.string.creationItemError, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }

}
