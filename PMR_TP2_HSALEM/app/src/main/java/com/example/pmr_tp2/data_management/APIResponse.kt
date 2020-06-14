package com.example.pmr_tp2.data_management

import com.example.pmr_tp2.model.ItemToDo
import com.example.pmr_tp2.model.ListeToDo
import com.google.gson.annotations.SerializedName

/**
 * Represents the response coming from the TodoAPI
 */
data class APIResponse(
    @SerializedName("version") val version : String,
    @SerializedName("success") val success: Boolean,
    @SerializedName("status") val status : Int,
    @SerializedName("hash") val hash : String,
    @SerializedName("lists") val lists : Array<ListeToDo>,
    @SerializedName("list") val list : ListeToDo,
    @SerializedName("items") val items : Array<ItemToDo>
    )



