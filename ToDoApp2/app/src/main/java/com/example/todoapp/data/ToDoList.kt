package com.example.todoapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ToDoList (
    @SerializedName("label")
    var title: String = "Nouvelle Liste",
    val items : MutableList<ToDoItem> = mutableListOf(),
    @SerializedName("id")
    val id : Int = 0
) : Serializable{

    fun searchItem (description : String) : ToDoItem {
        for (item in items) {

            if (item.description == description)
                return item
        }
        return ToDoItem() //return un item vide si jamais non trouvé
    }

    fun addItem(item : ToDoItem) = items.add(item) //ajoute un seul item


    fun addItems(items : MutableList<ToDoItem>){ //ajoute une collection d'items
        for (item in items)
            this.items.add(item)
    }

}