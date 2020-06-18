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
import com.example.tp1_todolist.adapter.ItemAdapterList
import com.example.tp1_todolist.data.model.DataProvider
import kotlinx.coroutines.*


class ChoixListActivity:AppCompatActivity(),ItemAdapterList.ActionListener {
    private val activityScope= CoroutineScope(
        SupervisorJob()// 用于cancel的单向传播，和常规的Job唯一的不同是：SupervisorJob 的取消只会向下传播
                + Dispatchers.Main
                + CoroutineExceptionHandler{_,throwable->
            Log.e("MainActivity","${throwable.message}")
        }
    )

    private val adapter = newAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choix_list_activity)

        //Recuperer le hash dans les preferences
        val sharedPreference = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val hash=sharedPreference.getString("hash",null)
        val pseudo=sharedPreference.getString("pseudo","My")
        if(pseudo=="My") this.title="My List"
        else this.title="$pseudo's List"

        var progressBarList: ProgressBar =findViewById(R.id.progressbarList)
        val rvList:RecyclerView = findViewById(R.id.rv_list)
        val btnNouvelleList: Button =findViewById(R.id.btnNouvelleList)
        val etNouvelleList:EditText=findViewById(R.id.etNouvelleList)
        rvList.adapter=adapter
        rvList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        loadLists(hash!!,progressBarList,rvList)

        btnNouvelleList.setOnClickListener(){
            Log.i("ChoixListActivity","btnNouvelleList is clicked")
            val listTitle:String=etNouvelleList.getText().toString()
            if(listTitle!=""){
                createList(hash,listTitle)
                adapter.notifyDataSetChanged()
                etNouvelleList.setText("")
            }else{
                val t = Toast.makeText(this, "A list name is need", Toast.LENGTH_SHORT)
                t.show()
            }
        }


    }

    //Récupérer les listes d’items associés à l’utilisateur connecté en faisant une requête, et les afficher
    private fun loadLists(hash:String,progressBarList: ProgressBar, rvList: RecyclerView){
        progressBarList.visibility = View.VISIBLE
        rvList.visibility = View.GONE
        activityScope.launch{
            val dataSetList= DataProvider.getLists(hash)
            if(dataSetList!=null){
                Log.i("ChoixListActivity","dataSetList is $dataSetList")
                adapter.showData(dataSetList)
                progressBarList.visibility = View.GONE
                rvList.visibility = View.VISIBLE
            }else{
                Log.i("ChoixListActivity","get lists fail")
            }
        }
    }

    //Création d'une liste pour l'utilisateur connecté
    private fun createList(hash:String,listTitle:String){
        activityScope.launch{
            val newList=DataProvider.createList(hash,listTitle)
            if(newList!=null){
                adapter.dataSet.add(newList)
                adapter.notifyDataSetChanged()
                Log.i("ChoixListActivity","newList is $newList")
            }else{
                Log.i("ChoixListActivity","createList fail")
            }
        }
    }


    private fun newAdapter(): ItemAdapterList {
        val adapter = ItemAdapterList(
            actionlistener=this//将ChoixListActivity implementer为ItemAdapter.ActionListener
        )
        return adapter
    }

    //Lors du clic sur une des listes, passer à l’activité suivante en lui fournissant
    //l’identifiant de la liste sélectionnée.
    override fun onItemClicked(listId: Int?,listTitle:String) {
        Log.i("onItemClickedList","listId is $listId")
        val bundle:Bundle= Bundle()
        bundle.putInt("listId",listId!!)
        bundle.putString("listTitle",listTitle)
        val intentToShowListActivity:  Intent= Intent(this@ChoixListActivity,ShowListActivity::class.java)
        intentToShowListActivity.putExtras(bundle)
        startActivity(intentToShowListActivity)
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
                val intentToSetting:Intent=Intent(this@ChoixListActivity,SettingsActivity::class.java)
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
                val intentToConnection:Intent=Intent(this@ChoixListActivity,MainActivity::class.java)
                startActivity(intentToConnection)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}