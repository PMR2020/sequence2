package com.example.pmr_tp2.data_management

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.example.pmr_tp2.model.ItemToDo
import com.example.pmr_tp2.model.ListeToDo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Provides data in the form of Java objects from the model : ListeToDo, ItemToDo, ProfilListeToDo and lists of those objects
 */

class UserManagerAPI(context: Context){

    val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    val hash = prefs.getString("hash","No Hash registered yet")
    val dataProvider = DataProvider(context)


    suspend fun getLists() : MutableList<ListeToDo> = withContext(Dispatchers.Default) {
        // Using the data provider to get lists
        val listsArray = dataProvider.getListsFromUser(hash)
        val lists = mutableListOf<ListeToDo>()
        for (list in listsArray){
            lists.add(list)
        }
        lists
    }


    suspend fun addList(listName:String){
        dataProvider.addListForUser(hash, listName)
    }

    suspend fun getItems(listID : Int) = withContext(Dispatchers.Default){
        val itemsArray = dataProvider.getItemsFromList(hash, listID)
        val items = mutableListOf<ItemToDo>()
        for (list in itemsArray){
            items.add(list)
        }
        items
    }

    suspend fun addItem(listID : Int, itemName:String){
        dataProvider.addItemToList(hash, listID, itemName)
    }

    /**
     * Sets item to checked if it is unchecked and vice-versa
     */
    suspend fun checkItem(listID: Int, indexOfItem : Int , itemID: Int, isChecked: Boolean) = withContext(Dispatchers.Default){

        if(isChecked){

            val listeItems = getItems(listID)
            if (listeItems[indexOfItem].fait == 0){
                Log.i("SNOW", "trying to check with item unchecked")
                dataProvider.checkItemFromList(hash, listID, itemID, 1)
            }
            else{
                Log.i("SNOW", "trying to check with item already checked")
            }

        }

        if(!isChecked){
            val listeItems = getItems(listID)
            if (listeItems[indexOfItem].fait == 1){
                Log.i("SNOW", "trying to uncheck with item checked")
                dataProvider.checkItemFromList(hash, listID, itemID, 0)
            }
            else{
                Log.i("SNOW", "trying to uncheck with item already unchecked")
            }

        }
    }
}