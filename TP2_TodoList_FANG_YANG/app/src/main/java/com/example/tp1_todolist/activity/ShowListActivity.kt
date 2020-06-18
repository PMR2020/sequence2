package com.example.tp1_todolist.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp1_todolist.R
import com.example.tp1_todolist.adapter.ItemAdapterTask
import com.example.tp1_todolist.data.model.DataProvider
import kotlinx.coroutines.*

class ShowListActivity :AppCompatActivity(),ItemAdapterTask.ActionListener {
    private val activityScope= CoroutineScope(
        SupervisorJob()// 用于cancel的单向传播，和常规的Job唯一的不同是：SupervisorJob 的取消只会向下传播
                + Dispatchers.Main
                + CoroutineExceptionHandler{_,throwable->
            Log.e("MainActivity","${throwable.message}")
        }
    )

    private val adapter = newAdapter()
    var idList:Int=0
    var listTiltle:String="ShowListActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_list_activity)

        //Recuperer le hash dans les preferences
        val sharedPreference = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val hash=sharedPreference.getString("hash",null)
        val bundle=intent.extras
        idList= bundle?.getInt("listId")!!
        listTiltle=bundle?.getString("listTitle")!!
        this.title=listTiltle
        Log.i("ShowListActivity","idList is $idList")

        var progressBarTask: ProgressBar =findViewById(R.id.progressbarTask)
        val rvTask: RecyclerView = findViewById(R.id.rv_task)
        val btnNouvelleTask: Button =findViewById(R.id.btnNouvelleTask)
        val etNouvelleTask: EditText =findViewById(R.id.etNouvelleTask)
        //etNouvelleTask.getBackground().setAlpha(170);//0~255透明度值
        rvTask.adapter = adapter
        rvTask.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        loadTasks(hash!!,idList,progressBarTask,rvTask)

        btnNouvelleTask.setOnClickListener(){
            Log.i("ShowListActivity","btnNouvelleTask is clicked")
            val taskLabel:String=etNouvelleTask.getText().toString()
            if(taskLabel!=""){
                createTask(hash,idList,taskLabel)
                adapter.notifyDataSetChanged()
                etNouvelleTask.setText("")
            }else{
                val t = Toast.makeText(this, "A task name is need", Toast.LENGTH_SHORT)
                t.show()
            }
        }
    }

    //Récupérer les items associés à la liste sélectionnée en faisant une requête auprès de API et les afficher
    private fun loadTasks(hash:String,idList:Int,progressBarTask: ProgressBar, rvTask: RecyclerView){
        progressBarTask.visibility = View.VISIBLE
        rvTask.visibility = View.GONE
        activityScope.launch{
            val dataSetTasks= DataProvider.getItems(hash,idList)
            if(dataSetTasks!=null){
                Log.i("ShowListActivity","dataSetList is $dataSetTasks")
                adapter.showData(dataSetTasks)
                progressBarTask.visibility = View.GONE
                rvTask.visibility = View.VISIBLE
            }else{
                Log.i("ShowListActivity","get tasks fail")
            }
        }
    }

    //Création d'un nouvel item dans la liste
    private fun createTask(hash:String,idList: Int,taskLabel:String){
        activityScope.launch{
            val newTask=DataProvider.createTask(hash,idList,taskLabel)
            if(newTask!=null){
                adapter.dataSetTask.add(newTask)
                adapter.notifyDataSetChanged()
                Log.i("ShowListActivity","create a newTask: $newTask")
                Log.i("ShowListActivity","dataSetTask apres ajoute: ${adapter.dataSetTask}")

            }else{
                Log.i("ShowListActivity","createTask fail")
            }
        }
    }

    //cocher/décocher chaque item,
    private fun checkTask(hash:String,idList:Int,idTask: Int,check:Int){
        activityScope.launch{
            val result=DataProvider.checkItem(hash,idList,idTask,check)
            if( result!=null){
                adapter.notifyDataSetChanged()
                Log.i("ShowListActivity","Success: list${idList} task${idTask}'s etat is $check")
            }else{
                Log.i("ShowListActivity","check task fail")
            }
        }
    }


    private fun newAdapter(): ItemAdapterTask {
        val adapter = ItemAdapterTask(
            actionlistener=this//将ChoixListActivity implementer为ItemAdapter.ActionListener
        )
        return adapter
    }


    override fun onItemClicked(idTask: Int,check:Int) {
        val sharedPreference = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val hash=sharedPreference.getString("hash",null)
        checkTask(hash!!,idList,idTask,check)
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            //un menu “setting” dans toutes les activités, qui fera venir à l’activité de setting
            R.id.action_settings -> {
                val intentToSetting:Intent=Intent(this@ShowListActivity,SettingsActivity::class.java)
                startActivity(intentToSetting)
                true
            }
            //un menu “déconnexion” dans toutes les activités, qui fera revenir à l’activité de connexion
            R.id.action_logout -> {
                val t = Toast.makeText(this, "You have been logout", Toast.LENGTH_SHORT)
                t.show()
                val sharedPreference = getSharedPreferences("Setting", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                    editor.remove("pseudo")
                    editor.remove("password")
                    editor.commit()
                val intentToConnection:Intent=Intent(this@ShowListActivity,MainActivity::class.java)
                startActivity(intentToConnection)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}