package com.example.tp2.api

import com.example.tp2.api.model.ConnectionResponse
import com.example.tp2.api.model.ItemsResponse
import com.example.tp2.api.model.ListsResponse
import retrofit2.http.*

interface ServiceApi {

    @GET("users")
    suspend fun testConnection()

    @POST("authenticate")
    suspend fun connection(@Query("user") user : String, @Query("password") password : String) : ConnectionResponse

    @GET("lists")
    suspend fun getLists(@Header("hash") hash : String) : ListsResponse

    @GET("lists/{id}/items")
    suspend fun getItems(@Header("hash") hash : String, @Path("id") id : Long) : ItemsResponse

    @POST("lists")
    suspend fun newList(@Header("hash") hash : String, @Query("label") list : String)

    @POST("lists/{id}/items")
    suspend fun newItem(@Header("hash") hash : String, @Path("id") id : Long, @Query("label") item : String)

    @PUT("lists/{idList}/items/{idItem}")
    suspend fun alterItem(@Header("hash") hash : String, @Path("idList") idList : Long, @Path("idItem") idItem : Long, @Query("check") check : Int)
}