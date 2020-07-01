package com.example.todoapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.ToDoList
import kotlinx.coroutines.*

class ToDoListActivity : AbstractActivity(), ToDoListViewHolder.OnRvListener{
    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("ShowListActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )

    private var lists = MutableList<ToDoList>(1) { _ -> ToDoList()} //lists des todolist
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.template_list)
        val clickListener = View.OnClickListener { view ->
            when(view.id){
                R.id.bntList -> onClickBtnNewList()
            }
        }

        val btnNewList = findViewById<Button>(R.id.bntList) //appui sur le bouton -> nouvelle liste
        btnNewList.setOnClickListener(clickListener)

        val edtList = findViewById<EditText>(R.id.edtList)
        val str = "Nouvelle Liste"
        edtList.setText(str) //texte correspondant à l'activité choix des listes
        Log.e("TODOTEST", "dans ChooseListActivity")

    }

    override fun onResume() { //car on peut y revenir sans re appeler onCreate
        super.onResume()
        //setUserParameters()
        //setTodoLists() OLD
        loadList()
        Log.e("TODOTEST", "Retour aux todos avec $user")
    }

    private fun loadList(){
        activityScope.launch {
            Log.e("DEBUG", "le hash est ${getUserHash()}")
            lists = DataProvider.getLists(getUserHash())
            Log.e("DEBUG", lists.toString())
            val rvTodoList = findViewById<RecyclerView>(R.id.rvList)
            rvTodoList.layoutManager = LinearLayoutManager(this@ToDoListActivity)
            rvTodoList.adapter = ToDoListAdapter(lists, this@ToDoListActivity, this@ToDoListActivity)
        }
    }

    private fun getUserHash() : String{
        val sharedPreferences : SharedPreferences = getSharedPreferences(MainActivity.userPref, Context.MODE_PRIVATE)
        return sharedPreferences.getString(MainActivity.userHash, "") ?: ""
    }
    /*override fun onPause() {//on sauve l'user quand on change d'activité
        super.onPause()
        jsonManager.fromUserToFile(user, this)
        Log.e("TODOTEST", "Pause de l'afficahge des todos, sauvegarde de $user")
    }*/

    private fun onClickBtnNewList(){
        val todoTitle : EditText = findViewById(R.id.edtList)
        val todoList = ToDoList(title = todoTitle.text.toString())
        user.todoLists.add(todoList)
        setTodoLists()
    }



    private fun setTodoLists(){
        val rvTodoList = findViewById<RecyclerView>(R.id.rvList)
        rvTodoList.layoutManager = LinearLayoutManager(this)
        rvTodoList.adapter = ToDoListAdapter(user.todoLists, this, this)
    }

    override fun onRvItemClick(position: Int) {
        //user.todolists[position] dans le bundle
        val intent = Intent(this, ItemListActivity::class.java)

        val b = Bundle()
        b.putInt("toDoListPosition", lists[position].id)
        intent.putExtras(b)//et la position dans un bundle

        startActivity(intent)
    }
}
