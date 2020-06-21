package com.example.todolistfinal.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL ="http://tomnab.fr/todo-api/"
//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
//    .addCallAdapterFactory(CoroutineCallAdapterFactory())
//    .baseUrl(BASE_URL)
//    .build()
interface TodoListApiService {
    @GET("lists/{id}/items")
    suspend fun getListItems(@Path("id") id: Int, @Query("hash") hash: String): ListofItems

    @GET("lists")
    suspend fun getLists(@Query("hash") hash: String):
            ListsofLists

    @POST("authenticate")
    suspend fun getHash(@Query("user") user: String, @Query("password") password: String): Hash

    @POST("lists")
    suspend fun addList(@Query("label") label: String, @Query("hash") hash: String)

    @POST("lists/{id}/items")
    suspend fun addItem(@Path("id") listId: Int, @Query("label") label: String, @Query("hash") hash: String)

    @PUT("lists/{id}/items/{itemId}")
    suspend fun setItem(@Path("id") listId: Int, @Path("itemId") itemId: Int, @Query("check") checked: Int, @Query("hash") hash: String)
}
//object TodoListApi {
//    val retrofitService : TodoListApiService by lazy {
//        retrofit.create(TodoListApiService::class.java) }
//}