package com.example.tp1_todolist.model

import com.google.gson.annotations.SerializedName

data class ItemTask( @SerializedName("label") var label:String?)  {
    @SerializedName("id")
    val id:Int?=null
    @SerializedName("checked")
    var checked :Int = 0 //private属性没有getter、setter

}