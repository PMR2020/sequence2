package com.example.tp2.api.model

import com.google.gson.annotations.SerializedName

data class ItemsResponse(
    @SerializedName("items")
    val items : List<ItemResponse>
)

data class ItemResponse(
    @SerializedName("id")
    val id : Long,
    @SerializedName("label")
    val description : String,
    @SerializedName("url")
    val url : String,
    @SerializedName("checked")
    val fait : Int
)