package com.example.todoapp.data

import java.io.Serializable

data class ToDoProfile( //les méthodes toString sont directements implémetées avec les dataclass
    var pseudo : String = "",
    val todoLists : MutableList<ToDoList> = mutableListOf() //que des setters personalisés pour elle
) : Serializable{
    fun addTodoList(todo : ToDoList) = todoLists.add(todo)
    fun addTodoLists(todos : MutableList<ToDoList>){
        for (todo in todos)
            todoLists.add(todo)
    }

}