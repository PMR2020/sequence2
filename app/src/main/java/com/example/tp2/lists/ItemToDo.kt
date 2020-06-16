package com.example.tp2.lists

import java.io.Serializable

class ItemToDo (var id : Long = -1, var description : String = "", var fait : Boolean = false) : Serializable {

    override fun toString(): String = "Tâche $description ${if (!fait) "non" else ""} effectuée"

}