package com.example.tp2.lists

import java.io.Serializable

class ProfilListeToDo(var login : String = "") : Serializable {

    var mesListeToDo : MutableList<ListeToDo> = mutableListOf()

    constructor(log: String, mesListes: MutableList<ListeToDo>) : this() {
        login = log
        mesListeToDo = mesListes
    }

    fun ajouteListe(uneListe : ListeToDo) {
        mesListeToDo.add(uneListe)
    }

    // Add unItem to the list uneListe if it is present in mesListeToDo
    fun ajoutItem(uneListe: ListeToDo?, unItem : ItemToDo) {
        var updatedListes : MutableList<ListeToDo> = mutableListOf()
        for (list : ListeToDo in mesListeToDo) {
            if (list.titreListeToDo == uneListe!!.titreListeToDo) {
                list.ajoutItem(unItem)
                uneListe.ajoutItem((unItem))
            }
            updatedListes.add(list)
        }
        mesListeToDo = updatedListes
    }

    // Update unItem from the list uneListe
    fun updateItem(uneListe: ListeToDo?, unItem : ItemToDo) {
        var updatedListes : MutableList<ListeToDo> = mutableListOf()
        for (list : ListeToDo in mesListeToDo) {
            if (list.titreListeToDo == uneListe!!.titreListeToDo) {
                list.updateItem(unItem)
                uneListe.updateItem((unItem))
            }
            updatedListes.add(list)
        }
        mesListeToDo = updatedListes
    }

    // Check if a list with the given title already exists
    fun listAlreadyExists(title : String) : Boolean {
        for (list : ListeToDo in mesListeToDo) {
            if (list.titreListeToDo == title) {
                return true
            }
        }
        return false
    }

    override fun toString(): String = "Listes du profil $login : $mesListeToDo"



}