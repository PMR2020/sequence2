package com.pmr.todolist.data

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.pmr.todolist.choixlist.ListeToDo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataProvider {
    private const val defaultUrl = "http://tomnab.fr/todo-api/"
    private var baseUrl = ""
    private var service = updateApiUrl(defaultUrl)

    fun updateApiUrl(url: String): TodoApiService {
        Log.i("dbg", "new url: $url")

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)

        if (url != baseUrl) {
            kotlin.runCatching {
                service = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build()
                    .create(TodoApiService::class.java)
            }.onFailure {
                updateApiUrl(defaultUrl)
            }
        }

        return service
    }

    suspend fun getHash(name: String, password: String): String = service.getHash(name, password).hash
    suspend fun getLists(hash: String): List<ListProperties> = service.getLists(hash).lists
    suspend fun getItems(hash: String, id: Int): List<ItemProperties> = service.getListItems(id, hash).items
    suspend fun setItem(hash: String, listId: Int, itemId: Int, checked: Boolean)
        = service.setItem(listId, itemId, if (checked) 1 else 0, hash)
    suspend fun addItem(hash: String, listId: Int, label: String) = service.addItem(listId, label, hash)
}