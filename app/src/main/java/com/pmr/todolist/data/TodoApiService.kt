package com.pmr.todolist.data

import retrofit2.http.*

data class HashResponse(val hash: String)

data class ListsResponse(val lists: List<ListProperties>)
data class ListProperties(val id: Int, val label: String)

data class ItemsResponse(val items: List<ItemProperties>)
data class ItemProperties(val id: Int, val label: String, val checked: Int)

interface TodoApiService {
    @POST("authenticate")
    suspend fun getHash(@Query("user") user: String, @Query("password") password: String): HashResponse

    @GET("lists")
    suspend fun getLists(@Query("hash") hash: String): ListsResponse

    @GET("lists/{id}/items")
    suspend fun getListItems(@Path("id") id: Int, @Query("hash") hash: String): ItemsResponse

    @PUT("lists/{id}/items/{itemId}")
    suspend fun setItem(@Path("id") listId: Int, @Path("itemId") itemId: Int, @Query("check") checked: Int, @Query("hash") hash: String)

    @POST("lists/{id}/items")
    suspend fun addItem(@Path("id") listId: Int, @Query("label") label: String, @Query("hash") hash: String)
}