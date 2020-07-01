package com.example.todoapp
import android.util.Log
import com.example.todoapp.api.API
import com.example.todoapp.data.ToDoItem
import com.example.todoapp.data.ToDoList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataProvider {

    private var url = "http://tomnab.fr/todo-api/"
    //private var url = "localhost/pmrrest/api/"

    private var service = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(API::class.java)

    suspend fun getHash(user: String, pass: String): String =
        service.getHash(user, pass).hash

    suspend fun getLists(hash: String): MutableList<ToDoList> =
        service.getLists(hash).lists

    suspend fun getItems(idList : Int, hash: String): MutableList<ToDoItem> =
        service.getItems(idList, hash).items

    //suspend fun debug() : String = service.debug().hash
}