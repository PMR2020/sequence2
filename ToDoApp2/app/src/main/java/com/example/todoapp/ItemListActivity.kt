package com.example.todoapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.ToDoItem
import com.example.todoapp.data.ToDoList
import com.example.todoapp.data.ToDoProfile
import kotlinx.coroutines.*

class ItemListActivity : AbstractActivity(), ToDoItemViewHolder.OnRvItemListener{

    private var toDoListPosition = 0
    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("ShowListActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.template_list)

        val edtList = findViewById<EditText>(R.id.edtList)
        val str = "Nouvel item"
        edtList.setText(str) //texte correspondant à l'activité choix des listes

        val clickListener = View.OnClickListener { view ->
            when(view.id){
                R.id.bntList -> onClickBtnNewItem()
            }
        }
        val btnNewItem = findViewById<Button>(R.id.bntList)
        btnNewItem.setOnClickListener(clickListener)

        //user = this.intent.getSerializableExtra("ToDoProfile") as ToDoProfile
        toDoListPosition = this.intent.extras?.getInt("toDoListPosition") ?:  0// au cas où le bundle est vide, on prend la première todolist
    }
    override fun onResume() { //car on peut y revenir sans re appeler onCreate
        super.onResume()
        //setUserParameters() // c'et là que ça coince
        //setItems() OLD
        loadItems()
        Log.e("TODOTEST", "Retour aux items avec $user")
        Log.e("TODOTEST", "la todolist considéré est ${toDoListPosition}")
    }

    fun loadItems(){
        activityScope.launch {
            val items = DataProvider.getItems(toDoListPosition, getUserHash())
            val rvToDoItem = findViewById<RecyclerView>(R.id.rvList)
            rvToDoItem.layoutManager = LinearLayoutManager(this@ItemListActivity)
            //Log.e("TODOTEST", "les itesms sont ${user.todoLists[toDoListPosition].items}")
            rvToDoItem.adapter = ToDoItemAdapter(items, this@ItemListActivity, this@ItemListActivity)
        }
    }

    private fun getUserHash() : String{
        val sharedPreferences : SharedPreferences = getSharedPreferences(MainActivity.userPref, Context.MODE_PRIVATE)
        return sharedPreferences.getString(MainActivity.userHash, "") ?: ""
    }
    /*override fun onPause() {
        super.onPause()
        jsonManager.fromUserToFile(user, this)
        Log.e("TODOTEST", "Pause de l'affichage des items, sauvegarde de $user")
    }*/
    private fun onClickBtnNewItem(){
        val itemTitle : EditText = findViewById(R.id.edtList)
        val todoItem = ToDoItem(description = itemTitle.text.toString(), done = false)
        user.todoLists[toDoListPosition].addItem(todoItem)
        setItems()
    }


    private fun setItems(){
        val rvToDoItem = findViewById<RecyclerView>(R.id.rvList)
        rvToDoItem.layoutManager = LinearLayoutManager(this)
        Log.e("TODOTEST", "les itesms sont ${user.todoLists[toDoListPosition].items}")
        rvToDoItem.adapter = ToDoItemAdapter(user.todoLists[toDoListPosition].items, this, this)
    }

    override fun onRvItemClick(position: Int) {
        Log.e("TODOTEST", "ON EST LA")
        user.todoLists[toDoListPosition].items[position].done = true
        setItems()
    }
}
