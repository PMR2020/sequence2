package com.example.tp1_todolist.model

import com.google.gson.annotations.SerializedName

data class User(@SerializedName("pseudo") private var pseudoName:String?, private var password :String?) {
    @SerializedName("id")
    val id:Int?=null

    var myList= mutableListOf<ItemList>()

}