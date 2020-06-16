package com.example.tp2

import android.app.Activity
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.tp2.api.ServiceApi
import com.example.tp2.lists.ItemToDo
import com.example.tp2.lists.ListeToDo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataProvider {

    private var prefs : SharedPreferences?= PreferenceManager.getDefaultSharedPreferences(MyApp.appContext)

    private val BASE_URL = prefs!!.getString("APIurl", "http://tomnab.fr/todo-api/")

    private val service = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ServiceApi::class.java)

    suspend fun testConnection() = service.testConnection()

    suspend fun connection(user : String, password : String) : String = service.connection(user, password).hash

    suspend fun getLists(hash : String) : List<ListeToDo> =
        service.getLists(hash).lists.map {
            ListeToDo(it.id, it.titre)
        }


    suspend fun getItems(hash : String, listId : Long) : List<ItemToDo> =
        service.getItems(hash, listId).items.map {
            ItemToDo(it.id, it.description, it.fait == 1)
        }

    suspend fun newList(hash : String, list : String) = service.newList(hash, list)

    suspend fun newItem(hash : String, idList : Long, item : String) = service.newItem(hash, idList, item)

    suspend fun alterItem(hash : String, idList : Long, idItem : Long, check : Int) = service.alterItem(hash, idList, idItem, check)

}