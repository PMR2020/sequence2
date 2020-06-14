package com.example.pmr_tp2.data_management

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import com.example.pmr_tp2.R
import com.example.pmr_tp2.activities.MainActivity
import com.example.pmr_tp2.data_management.ToDoAPIService.Companion.DEFAULT_BASE_URL
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides data directly from APIResponse objects.
 * Is in charge of communicating with the API.
 */
class DataProvider(val context: Context) {
    val service : ToDoAPIService
    val prefs : SharedPreferences
    var baseURL : String?
    init {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        baseURL = prefs.getString("urlPref", ToDoAPIService.DEFAULT_BASE_URL)
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseURL)
            .build()
        service = retrofit.create(ToDoAPIService::class.java)
    }

   suspend  fun putHashInPreferences(pseudoInput: String, passwordInput: String): String?  = withContext(Dispatchers.Default) {
        val result = service.authenticate(pseudoInput, passwordInput)
        prefs.edit().putString("hash", result.hash).apply()
        result.hash

    }

    /**
     * @param hash : hashscode corresponding to a certain user
     * @return Array<ListeToDo> containing the lists from the request
     */
    suspend fun getListsFromUser(hash : String?) = withContext(Dispatchers.Default){
        val result = service.lists(hash)
        result.lists
    }

    /**
     * Adds a list for a user.
     * @param hash : hashcode corresponding to a certain user
     * @param listName : the name of the list to create
     */
    suspend fun addListForUser(hash: String?, listName: String) = withContext(Dispatchers.Default){
        service.addList(hash = hash, listName = listName)
    }

    suspend fun getItemsFromList(hash : String?, listID : Int) = withContext(Dispatchers.Default){

        var request : String = "lists/$listID/items"
        val result = service.getItems(request, listID, hash)
        result.items
    }

    suspend fun addItemToList(hash : String?, listID : Int, itemName : String) = withContext(Dispatchers.Default){

        var request : String = "lists/$listID/items"
        service.addItem(request, listID, itemName, hash)
    }

    /**
     * @param check == 1 if has to be checked and == 0 if has to be unchecked
     */
    suspend fun checkItemFromList(hash : String?, listID : Int, itemID : Int, check : Int) = withContext(Dispatchers.Default){

        Log.i("SNOW", "ITEM ID : $itemID")

        var request : String = "lists/$listID/items/$itemID"

        service.checkItem(request, check, hash)
    }

}