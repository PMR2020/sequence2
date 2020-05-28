package com.pmr.todolist

import com.pmr.todolist.choixlist.ListeToDo
import java.io.Serializable

class ProfilListeTodo : Serializable {
    var login: String = ""
    var mesListeTodo: MutableList<ListeToDo> = mutableListOf()

    constructor()

    constructor(l: String, items: MutableList<ListeToDo>) {
        login = l
        mesListeTodo = items
    }

    constructor(items: MutableList<ListeToDo>) {
        mesListeTodo = items
    }

    fun ajouteListe(liste: ListeToDo) {
        mesListeTodo.add(liste)
    }

    override fun toString(): String {
        return login // TODO: ??
    }
}