package com.tppmr.tp.helper

import com.tppmr.tp.model.*
import retrofit2.http.*

interface ServiceApi {
    @POST("authenticate")
    suspend fun getHash(@Query("user") pseudo: String, @Query("password") passwordEntered: String): LoginResponse


    @GET("users")
    suspend fun getUsers(@Query("hash") adminHash: String): UsersResponse

    @GET("lists")
    suspend fun getLists(@Query("hash") userHash: String): ListsResponse

    @GET("lists/{id}/items")
    suspend fun getItems(@Path("id") listId: Int,@Query("hash") userHash: String): ItemsResponse


    @POST("users")
    suspend fun createUser(@Query("pseudo") pseudo: String, @Query("pass") passwordEntered: String, @Query("hash") adminHash: String): User

    @POST("lists")
    suspend fun createList(@Query("label") label: String, @Query("hash") userHash: String): Liste

    @POST("lists/{id}/items")
    suspend fun createItem(@Path("id") listId: Int, @Query("label") label: String, @Query("hash") userHash: String): Item


    @PUT("lists/{id}")
    suspend fun modifyList(@Path("id") listId: Int, @Query("label") newLabel: String, @Query("hash") userHash: String) : Liste

    @PUT("lists/{idList}/items/{id}")
    suspend fun modifyItem(@Path("idList") listId: Int, @Path("id") itemId: Int, @Query("label") newLabel: String, @Query("hash") userHash: String) : Item

    @PUT("lists/{idList}/items/{id}")
    suspend fun modifyCheckedStateItem(@Path("idList") listId: Int, @Path("id") itemId: Int,@Query("check") checked: Int, @Query("hash") userHash: String) : Item


    @DELETE("lists/{id}")
    suspend fun deleteList(@Path("id") listId: Int, @Query("hash") userHash: String) : Liste

    @DELETE("lists/{idList}/items/{itemId}")
    suspend fun deleteItem(@Path("idList") listId: Int, @Path("itemId") itemId: Int, @Query("hash") userHash: String) : Item
}