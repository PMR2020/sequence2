package com.example.pmr_tp2.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pmr_tp2.R
import com.example.pmr_tp2.adapter.ShowListAdapter
import com.example.pmr_tp2.data_management.UserManagerAPI
import com.example.pmr_tp2.model.ItemToDo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ShowListActivity : AppCompatActivity(), ShowListAdapter.ShowListListener, View.OnClickListener{

    var listName = ""
    var listID = -1
    val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)


    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) // to prevent keyboard from showing up
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showlist)

        listName = this.intent.extras!!.getString("listName")!!
        listID = this.intent.extras!!.getInt("idListe")

        findViewById<TextView>(R.id.list_name_display).setText("${listName}")

        // Adding a listener to the add button
        val addItemBtn = findViewById<Button>(R.id.ajouter_item_btn)
        addItemBtn.setOnClickListener(this)

        // RecyclerView
        val adapter = ShowListAdapter(this)
        val listOfItemsRecyclerView = findViewById<RecyclerView>(R.id.list_of_items)
        listOfItemsRecyclerView.adapter = adapter
        listOfItemsRecyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)


        val userManager = UserManagerAPI(this)
        var itemsList = mutableListOf<ItemToDo>()
        activityScope.launch {
            itemsList = userManager.getItems(listID)
            adapter.showData(itemsList)
        }


    }

    override fun onItemClicked(indexOfItem : Int, isChecked : Boolean) {

        Log.i("SNOW", "INDEX OF ITEM : " + indexOfItem)

        val userManager = UserManagerAPI(this)



        activityScope.launch {
            val itemID = userManager.getItems(listID)[indexOfItem].id
            userManager.checkItem(listID, indexOfItem, itemID, isChecked)
        }

    }

    override fun onClick(v: View?) {
        val userManager = UserManagerAPI(this)
        val itemDescription = findViewById<EditText>(R.id.item_description_input).text.toString()

        val b = Bundle()
        b.putString("listName", listName)
        b.putInt("idListe", listID)
        val toShowListActivity = Intent(this, ShowListActivity::class.java)
        toShowListActivity.putExtras(b)

        activityScope.launch {
            userManager.addItem(listID, itemDescription)
            // Reloading the activity to display changes
            finish()
            startActivity(toShowListActivity)
        }



    }
}
