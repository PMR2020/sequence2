package com.example.tp2.api.model

import com.google.gson.annotations.SerializedName

data class ListsResponse(
    @SerializedName("lists")
    val lists : List<ListResponse>
)

data class ListResponse(
    @SerializedName("id")
    val id : Long,
    @SerializedName("label")
    val titre : String
)