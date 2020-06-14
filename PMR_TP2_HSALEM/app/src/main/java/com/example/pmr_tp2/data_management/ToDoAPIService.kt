package com.example.pmr_tp2.data_management

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ToDoAPIService {
    companion object {
        val DEFAULT_BASE_URL = "http://10.0.2.2/todo-api/" // this default url should work with any emulator, as long as the api is in localhost in a folder named todo-api
    }

    @POST("index?request=authenticate")
    suspend fun authenticate(
        @Query("user") user: String,
        @Query("password") password: String
    ) : APIResponse

    @GET("index.php?request=lists")
    suspend fun lists(
        @Query("hash") hash : String?
    ) : APIResponse

    @POST("index.php?request=lists")
    suspend fun addList(
        @Query("hash") hash : String?,
        @Query("label") listName : String
    ) : APIResponse

    @GET("index.php?")
    suspend fun getItems(
        @Query("request") request : String,
        @Query("id") id : Int,
        @Query("hash") hash : String?

    ) : APIResponse

    @POST("index.php?")
    suspend fun addItem(
        @Query("request") request : String,
        @Query("id") id : Int,
        @Query("label") label : String,
        @Query("hash") hash : String?
    )

    @PUT("index.php?")
    suspend fun checkItem(
        @Query("request") request : String,
        @Query("check") check : Int,
        @Query("hash") hash : String?
    )
}


