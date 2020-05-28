package com.pmr.todolist.showlist

import java.io.Serializable

class ItemToDo : Serializable {
    var description: String = ""
    var fait: Boolean = false

    constructor()

    constructor(desc: String) {
        description = desc
    }

    constructor(desc: String, done: Boolean) {
        description = desc
        fait = done
    }

    override fun toString(): String {
        return description + if (fait) "(done)" else "todo"
    }
}