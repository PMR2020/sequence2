package com.example.todoapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ToDoItem (
    @SerializedName("label")
    var description : String = "",
    var done : Boolean = false
) : Serializable