package com.example.tp2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp2.adapters.ListeAdapter
import com.example.tp2.lists.ListeToDo
import kotlinx.coroutines.*

class ChoixListActivity : GenericActivity(), ListeAdapter.ActionListener, View.OnClickListener {

    private var adapter : ListeAdapter? = null
    private var refBtnOK: Button? = null
    private var refListInput: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        /*
        Declarations
         */
        refBtnOK = findViewById(R.id.OKBtnChoixList)
        refListInput = findViewById(R.id.listInputChoixList)
        adapter = newAdapter()

        refBtnOK!!.setOnClickListener(this)

        /*
        RecyclerView
         */
        setRecyclerView()
    }

    // Used to update the list when coming from ShowListActivity
    override fun onResume() {
        super.onResume()

        /*
        RecyclerView
         */
        setRecyclerView()

    }


    private fun newAdapter() : ListeAdapter = ListeAdapter(actionListener = this)


    private fun setRecyclerView() {
        val list : RecyclerView = findViewById(R.id.listOfList)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        updateDisplay()
    }

    private fun updateDisplay() {
        activityScope.launch {

            runCatching {
                val hash : String? = prefs!!.getString("hash", "")
                DataProvider.getLists(hash!!)
            }.fold(
                onSuccess = {
                    adapter!!.setData(it)
                },
                onFailure = {
                    Toast.makeText(MyApp.appContext, R.string.recupListsError, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }


    /*
    Item listener
     */
    override fun onItemClicked(listeToDo: ListeToDo) {
        val bundle = Bundle()
        bundle.putSerializable("id", listeToDo.id)

        val intent = Intent(this, ShowListActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    /*
    OK Button listener
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.OKBtnChoixList -> {

                activityScope.launch {

                    runCatching {
                        val hash : String? = prefs!!.getString("hash", "")
                        DataProvider.newList(hash!!, refListInput!!.text.toString())
                    }.fold(
                        onSuccess = {
                            updateDisplay()
                        },
                        onFailure = {
                            Toast.makeText(MyApp.appContext, R.string.creationListError, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }


}
