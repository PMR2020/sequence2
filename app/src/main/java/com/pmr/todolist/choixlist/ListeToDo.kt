package com.pmr.todolist.choixlist

import com.pmr.todolist.showlist.ItemToDo
import java.io.Serializable

class ListeToDo : Serializable {
    var titreListeTodo: String = ""
    var lesItems: MutableList<ItemToDo> = mutableListOf()

    constructor()

    constructor(titre: String) {
        titreListeTodo = titre
    }

    fun rechercherItem(desc: String) : ItemToDo? {
        for (item: ItemToDo in lesItems) {
            if (item.description == desc) {
                return item
            }
        }

        return null
    }
}