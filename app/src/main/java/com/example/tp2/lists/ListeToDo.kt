package com.example.tp2.lists

import java.io.Serializable

class ListeToDo (val id : Long = -1, var titreListeToDo: String = "",  var lesItems: MutableList<ItemToDo> = mutableListOf<ItemToDo>()) : Serializable {

    fun rechercherItem(descriptionItem : String): Boolean {
        for (item in lesItems) {
            if (item.description == descriptionItem) {
                return true
            }
        }
        return false
    }

    fun ajoutItem(unItem : ItemToDo) {
        lesItems.add(unItem)
    }

    fun updateItem(unItem : ItemToDo) {
        var updatedItem : MutableList<ItemToDo> = mutableListOf()
        for (item : ItemToDo in lesItems) {
            if (item.description == unItem.description) {
                updatedItem.add(unItem)
            }
            else {
                updatedItem.add(item)
            }
        }
        lesItems = updatedItem
    }

    override fun toString(): String = "Liste $titreListeToDo composé de $lesItems"

}