package com.example.tp2.api.model

import com.google.gson.annotations.SerializedName

data class ConnectionResponse(
    @SerializedName("hash")
    val hash : String
)