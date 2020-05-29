package com.pmr.todolist

import com.pmr.todolist.choixlist.ListeToDo
import com.pmr.todolist.showlist.ItemToDo
import java.io.Serializable

class ProfilListeTodo {
    var login: String = ""
    var mesListeTodo: MutableList<ListeToDo> = mutableListOf()

    constructor()

    constructor(l: String, items: MutableList<ListeToDo>) {
        login = l
        mesListeTodo = items
    }

    constructor(l: String) {
        login = l
    }

    fun ajouteListe(liste: ListeToDo) {
        mesListeTodo.add(liste)
    }

    fun getList(title: String): ListeToDo? {
        for (list in mesListeTodo) {
            if (list.titreListeTodo == title) {
                return list
            }
        }

        return null
    }
}