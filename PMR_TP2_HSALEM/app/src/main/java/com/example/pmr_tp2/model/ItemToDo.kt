package com.example.pmr_tp2.model

import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable

class ItemToDo (
    @SerializedName("label") var description : String = "",
    @SerializedName("id") var id : Int,
    @SerializedName("checked") var fait : Int = 0) : Serializable{

    override fun toString(): String {
        return "<Item : $description || Fait: ${if (fait==1) "oui" else "non"}>\n "
    }

    /**
     * @return A java JSONObject representing this item
     */
    fun toJSONObject() : JSONObject{
        var itemJSON = JSONObject()
        itemJSON.put("description", description)
        itemJSON.put("fait", fait)
        return itemJSON
    }

    /**
     * @return A string containing JSON text corresponding to this item
     */
    fun toJSONString() : String{
        val itemJSON = toJSONObject()
        return itemJSON.toString()
    }
}

// 