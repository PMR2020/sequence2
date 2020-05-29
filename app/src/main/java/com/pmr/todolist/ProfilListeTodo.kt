package com.pmr.todolist

import com.pmr.todolist.choixlist.ListeToDo
import java.io.Serializable

class ProfilListeTodo {
    var login: String = ""
    var mesListeTodo: Array<ListeToDo> = emptyArray()

    constructor()

    constructor(l: String, items: MutableList<ListeToDo>) {
        login = l
        mesListeTodo = items.toTypedArray()
    }

    constructor(l: String) {
        login = l
    }

    fun ajouteListe(liste: ListeToDo) {
        val array = Array<ListeToDo>(mesListeTodo.size+1) {
            if (it < mesListeTodo.size) {
                mesListeTodo[it]
            } else {
                liste
            }
        }

        mesListeTodo = array
    }
}