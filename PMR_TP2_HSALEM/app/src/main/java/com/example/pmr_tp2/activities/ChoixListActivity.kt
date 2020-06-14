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
import com.example.pmr_tp2.adapter.ChoixListeAdapter
import com.example.pmr_tp2.model.ListeToDo
import com.example.pmr_tp2.data_management.UserManagerAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ChoixListActivity : AppCompatActivity(), ChoixListeAdapter.ListOfListsListener, View.OnClickListener{

    var login : String = ""
    var password : String = ""
    val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)


    override fun onCreate(savedInstanceState: Bundle?) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) // to prevent keyboard from showing up
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choixliste)

        // Getting extras from intent
        this.login = this.intent.extras!!.getString("login")!!
        this.password = this.intent.extras!!.getString("password")!!
        findViewById<TextView>(R.id.usernamedisplay).setText("Listes de $login")

        // Adding a listener to the add button
        val addListBtn = findViewById<Button>(R.id.ajouter_liste_btn)
        addListBtn.setOnClickListener(this)

        // Creating a UserManager
        val userManager = UserManagerAPI(this)

        // RecyclerView
        val rvAdapter = ChoixListeAdapter(this)
        val listOfListsRecyclerView = findViewById<RecyclerView>(R.id.list_of_lists)
        listOfListsRecyclerView.adapter = rvAdapter
        listOfListsRecyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        // Using a coroutine to call getLists
        var listNamesList = mutableListOf<ListeToDo>()

        activityScope.launch {
            Log.i("SNOW", "Getting lists...")
            listNamesList = userManager.getLists()
            Log.i("SNOW", "Got lists, size = " + listNamesList.size)
            rvAdapter.showData(listNamesList) // calling this inside the coroutine because it requires the lists to be loaded
        }

    }

    override fun onListClicked(listeToDo: ListeToDo, indexOfList : Int) {
        // Going to ShowListActivity, giving the list object, the name of its owner, and its userManager
        val b = Bundle()
        b.putString("listName", listeToDo.titreListeToDo)
        b.putInt("idListe", listeToDo.idListe)
        val toShowListActivity = Intent(this, ShowListActivity::class.java)
        toShowListActivity.putExtras(b)
        startActivity(toShowListActivity)
    }

    override fun onClick(v: View?) { // button to add a list
        val userManager = UserManagerAPI(this)
        val listName = findViewById<EditText>(R.id.list_name_input).getText().toString()
        val b = Bundle()
        b.putString("login", login)
        b.putString("password", password)
        val toChoixListActivity = Intent(this, ChoixListActivity::class.java)
        toChoixListActivity.putExtras(b)

        activityScope.launch {
            userManager.addList(listName)
            // Reloading the activity so that modifications appear
            finish()
            startActivity(toChoixListActivity) }

    }
}