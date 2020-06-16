package com.tppmr.tp

import android.util.Log
import com.tppmr.tp.helper.ServiceApi
import com.tppmr.tp.model.Item
import com.tppmr.tp.model.Liste
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.CancellationException


object DataProvider {

    /**
     * Retrieves the hash of the user who logs in
     */
    suspend fun getHashFromApi(pseudo: String, enteredPassword: String, baseUrl: String): String? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            val loginResponse = service.getHash(pseudo, enteredPassword)
            loginResponse.hash
        } catch (e: Exception) {
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Retrieves the pseudo of all users who have logged in
     */
    suspend fun getAllUsersPseudosFromApi(
        baseUrl: String,
        adminHash: String
    ): MutableList<String>?{
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        val pseudos = mutableListOf<String>()
        return try {
            val usersResponse = service.getUsers(adminHash)
            for( i in usersResponse.users) {
                pseudos.add(i.pseudo)
            }
            pseudos
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            mutableListOf("")
        }
    }

    /**
     * Retrieves all lists of the user who have logged in
     */
    suspend fun getListsFromApi(
        baseUrl: String,
        userHash: String
    ): MutableList<Liste>? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            val listsResponse = service.getLists(userHash)
            listsResponse.lists
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Retrieves all items of the list where the user have clicked
     */
    suspend fun getItemsFromApi(
        baseUrl: String,
        userHash: String,
        listeId: Int
    ): MutableList<Item>?{
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            val itemsResponse = service.getItems(listeId, userHash)
            itemsResponse.items
        } catch (e: java.lang.Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Creates a new user in the API
     */
    suspend fun createUserInApi(
        pseudo: String,
        enteredPassword: String,
        baseUrl: String,
        adminHash: String
    ): String? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            val user = service.createUser(pseudo, enteredPassword, adminHash)
            val loginResponse = service.getHash(pseudo,enteredPassword)
            loginResponse.hash
        } catch (e: Exception) {
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Creates a new list for the user who have logged in
     */
    suspend fun createListInApi(
        baseUrl: String,
        userHash: String,
        label: String
    ): Liste? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            service.createList(label, userHash)
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Creates a new item for the list where the user have clicked
     */
    suspend fun createItemInApi(
        baseUrl: String,
        userHash: String,
        listId: Int,
        label: String
    ): Item?{
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            service.createItem(listId, label, userHash)
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Modifies the label of the current list
     */
    suspend fun modifyListInApi(
        baseUrl: String,
        userHash: String,
        listId: Int,
        newLabel: String
    ): Liste? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            service.modifyList(listId,newLabel,userHash)
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Modifies the label of the current item
     */
    suspend fun modifyItemInApi(
        baseUrl: String,
        userHash: String,
        listId: Int,
        itemId: Int,
        newLabel: String
    ): Item? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            service.modifyItem(listId,itemId,newLabel,userHash)
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Modifies the checked state of the current item
     */
    suspend fun modifyCheckedStateItemInApi(
        baseUrl: String,
        userHash: String,
        listId: Int,
        itemId: Int,
        newChecked: Int
    ): Item?{
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            service.modifyCheckedStateItem(listId,itemId,newChecked,userHash)
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Deletes the current list
     */
    suspend fun deleteListInApi(
        baseUrl: String,
        userHash: String,
        listId: Int
    ): Liste? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            service.deleteList(listId,userHash)
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Deletes the current item
     */
    suspend fun deleteItemInApi(
        baseUrl: String,
        userHash: String,
        listId: Int,
        itemId: Int
    ): Item? {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            service.deleteItem(listId,itemId,userHash)
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            null
        }
    }

    /**
     * Deletes all lists of the user who have logged in
     */
    suspend fun deleteAllListsInApi(
        baseUrl: String,
        userHash: String
    ): Boolean {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            val listes = service.getLists(userHash).lists
            for(i in listes){
                service.deleteList(i.id,userHash)
            }
            true
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            false
        }
    }

    /**
     * Deletes all items of the list where the user have clicked
     */
    suspend fun deleteAllItemsInApi(
        baseUrl: String,
        userHash: String,
        listId: Int
    ): Boolean {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ServiceApi::class.java)
        return try {
            val items = service.getItems(listId, userHash).items
            for(i in items){
                service.deleteItem(listId,i.id,userHash)
            }
            true
        } catch (e: Exception){
            if(e is  CancellationException){
                throw e
            }
            Log.i("error",e.toString())
            false
        }
    }
}




