package com.example.todoapp.api

import com.example.todoapp.data.ToDoItem
import com.example.todoapp.data.ToDoList
import com.google.gson.annotations.SerializedName
import retrofit2.http.*


interface API{
    @POST("authenticate")
    suspend fun getHash(@Query("user") user: String, @Query("password") pass: String): Hash

    @GET("lists")
    suspend fun getLists(@Header("hash") hash: String) : ListToDoList

    @GET("lists/{idList}/items")
    suspend fun getItems(@Path("idList") idList: Int, @Header("hash") hash: String): ListToDoItem

    /*@POST("authenticate?user=tom&password=web")
    suspend fun debug() : Hash*/
}

data class Hash (
    @SerializedName("hash")
    val hash: String)

data class ListToDoList(
    @SerializedName("lists")
    val lists: MutableList<ToDoList>
)

data class ListToDoItem(
    @SerializedName("items")
    val items: MutableList<ToDoItem>
)