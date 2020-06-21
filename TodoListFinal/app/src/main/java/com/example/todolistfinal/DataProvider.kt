package com.example.todolistfinal

import com.example.todolistfinal.network.ItemDescription
import com.example.todolistfinal.network.ListfromUser
import com.example.todolistfinal.network.TodoListApiService
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object DataProvider {

    val BASE_URL = "http://tomnab.fr/todo-api/"
    private var service =Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TodoListApiService::class.java)

//
//    var okHttpClient =
//        OkHttpClient().newBuilder().addInterceptor(object : Interceptor() {
//            @Throws(IOException::class)
//            fun intercept(chain: Interceptor.Chain): Response? {
//                val originalRequest: Request = chain.request()
//                val builder: Request.Builder = originalRequest.newBuilder().header(
//                    "Authorization",
//                    Credentials.basic("aUsername", "aPassword")
//                )
//                val newRequest: Request = builder.build()
//                return chain.proceed(newRequest)
//            }
//        }).build()


    suspend fun getAllListsFromApi(hash: String) : List<ListfromUser> = service.getLists(hash).lists

    suspend fun getHashFromApi(name: String, password: String): String = service.getHash(name, password).hash

    suspend fun addList(hash:String, label: String) = service.addList(label, hash)

    suspend fun getItems(hash: String, id: Int): List<ItemDescription> = service.getListItems(id, hash).items

    suspend fun setItem(hash: String, listId: Int, itemId: Int, checked: Boolean)  = service.setItem(listId, itemId, checked.toString().toInt(), hash)

    suspend fun addItem(hash: String, listId: Int, label: String) = service.addItem(listId, label, hash)

}