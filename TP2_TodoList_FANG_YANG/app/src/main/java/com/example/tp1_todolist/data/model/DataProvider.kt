package com.example.tp1_todolist.data.model
import android.util.Log
import com.example.tp1_todolist.data.api.ServiceTodoApi
import com.example.tp1_todolist.model.ItemList
import com.example.tp1_todolist.model.ItemTask
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object DataProvider {
    var URL_BASE: String = "http://tomnab.fr/todo-api/"
    val service = Retrofit.Builder()
        .baseUrl(URL_BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create<ServiceTodoApi>()

    //Connexion auprès de l'API Renvoie un hash sans délai d'expiration
    suspend fun authenticate(pseudo: String, password: String): String? {
        val call = service.authenticate(pseudo, password)
        var hash: String? = null
        if (call.success) {
            hash = call.hash
            Log.i("DataProvider", "get hash : $hash")
        } else {
            val errorMessage = call.success
            Log.i("DataProvider", "Error : ${errorMessage}")
        }
        return hash
    }

    //Récupération des listes de l'utilisateur courant Le hash peut être fourni dans les entêtes
    suspend fun getLists(hash: String): MutableList<ItemList>? {
        val call = service.getLists(hash)
        var lists:MutableList<ItemList>?=null
        if (call.success) {
            lists = call.lists
            Log.i("DataProvider", "get lists: ${call.lists}")
        } else {
            val errorMessage = call.status
            Log.i("DataProvider", "Error : ${errorMessage}")
        }
        return lists
    }

    //Création d'une liste pour l'utilisateur connecté
    suspend fun createList(hash: String,labelList:String): ItemList? {
        val call = service.createList(hash,labelList)
        var newList:ItemList?=null
        if (call.success) {
            newList = call.list
            Log.i("DataProvider", "create a new list: ${call.list}")
        } else {
            val errorMessage = call.status
            Log.i("DataProvider", "Error : ${errorMessage}")
        }
        return newList
    }

    //get Items de la liste dont id est listId
    suspend fun getItems(hash:String,listId:Int): MutableList<ItemTask>? {
        val call = service.getItems(hash,listId)
        var tasks:MutableList<ItemTask>?=null
        if (call.success) {
            tasks = call.items
            Log.i("DataProvider", "get items: ${call.items}")
        } else {
            val errorMessage = call.status
            Log.i("DataProvider", "Error : ${errorMessage}")
        }
        return tasks
    }

    //Cocher l'item d'identifiant idList de la liste d'identifiant idItem
    suspend fun checkItem(hash:String,idList:Int,idItem:Int,check:Int): ItemTask? {
        val call = service.checkItem(hash,idList,idItem,check)
        var task:ItemTask?=null
        if (call.success) {
            task=call.item
            Log.i("DataProvider", "check item success ${idItem}: ${task} ")
        }
        return task
    }

    //Création d'un nouvel item dans la liste d'identifiant idList
    suspend fun createTask(hash: String,idList: Int,labelTask:String): ItemTask? {
        val call = service.createItem(hash,idList,labelTask)
        var newTask:ItemTask?=null
        if (call.success) {
            newTask = call.item
            Log.i("DataProvider", "create a new task: ${newTask}")
        } else {
            val errorMessage = call.status
            Log.i("DataProvider", "Error : ${errorMessage}")
        }
        return newTask
    }

    //Suppression de la liste d'identifiant idList de l'utilisateur connecté
    suspend fun deleteList(hash:String,idList:Int): Boolean {
        val call = service.deleteList(hash,idList)
        if (call.success) {
            Log.i("DataProvider", "Success: delete list${idList}")
            return true
        } else {
            val errorMessage = call.status
            Log.i("DataProvider", "Error : ${errorMessage}")
            return false
        }
    }

    //Supprimer l'item d'identifiant idTask de la liste d'identifiant idList
    suspend fun deleteTask(hash:String,idList:Int,idTask:Int): Boolean {
        val call = service.deleteItem(hash,idList,idTask)
        if (call.success) {
            Log.i("DataProvider", "Success: delete task${idList} of list${idList} ")
            return true
        } else {
            val errorMessage = call.status
            Log.i("DataProvider", "Error : ${errorMessage}")
            return false
        }
    }


//    suspend fun createUser(@Header("hash") hash:String, @Query("pseudo") myPseudo:String, @Query("pass") myPassword:String):ReponseApi


}




// requete asycrone
//        activityScope.launch {
//            call.enqueue(object: Callback<ReponseApi> {
//                override fun onFailure(call: Call<ReponseApi>, t: Throwable) {
//                    Log.i("MainActivity","Throwable:${t}")
//                    hash="failure"
//
//                }
//                override fun onResponse(call: Call<ReponseApi>, response: Response<ReponseApi>){
//                    Log.i("MainActivity","hash2 : $hash")
//                    hash=response.body()?.hash
//                    Log.i("MainActivity","hash3 : $hash")
//
//                }
//            });
//            Log.i("MainActivity","hash4 : $hash")
//        }

