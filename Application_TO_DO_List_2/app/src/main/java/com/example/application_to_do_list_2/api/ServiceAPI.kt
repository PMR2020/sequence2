package com.example.application_to_do_list_2.api

import com.example.application_to_do_list_2.data.*
import retrofit2.http.*

interface ServiceAPI { // CF DOCUMENTATION DE L'API
    @GET("index.php?request=lists")
    suspend fun getListProfils(@Query("hash") hash:String) : DataLists

    @POST("index.php?request=authenticate")
    suspend fun getHash(@Query("user")  pseudo :String, @Query("password") password:String) : DataUser

    @GET("index.php?")
    suspend fun getItems(@Query("request")  requete :String, @Query("hash") hash:String) : DataItems

    @PUT("index.php?")
    suspend fun checkItem(@Query("request")  requete :String,  @Query("check") check:Int,@Query("hash") hash:String) : DataItem

    @POST("index.php?")
    suspend fun createItem(@Query("request")  requete :String,  @Query("label") label:String,@Query("hash") hash:String) : DataItem
}