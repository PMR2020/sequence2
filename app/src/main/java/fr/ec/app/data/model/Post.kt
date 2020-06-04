package fr.ec.app.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("name")
    val title: String,
    @SerializedName("tagline")
    val subTitle: String
)