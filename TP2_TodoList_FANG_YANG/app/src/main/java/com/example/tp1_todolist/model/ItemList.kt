package com.example.tp1_todolist.model

import com.google.gson.annotations.SerializedName

data class ItemList( @SerializedName("label") var title: String?)  {
    @SerializedName("id")
    val id:Int? =null

    var myTask= mutableListOf<ItemTask>()

}

