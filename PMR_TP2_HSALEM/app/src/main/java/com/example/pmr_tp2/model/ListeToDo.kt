package com.example.pmr_tp2.model

import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

class ListeToDo(
    @SerializedName("label") var titreListeToDo: String = "",
    @SerializedName("id") var idListe : Int = -1,
    var lesItems: MutableList<ItemToDo> = mutableListOf()
) : Serializable {

    fun rechercherItem(descriptionItem: String): ItemToDo? {
        for (item in lesItems) {
            if (item.description == descriptionItem) return item
        }
        return null
    }

    override fun toString(): String {
        var stringItems = ""
        for (liste in lesItems) {
            stringItems += (liste.toString())
        }
        val string =
            "** LISTE : $titreListeToDo **\n" +
                    "Items de la liste :\n " +
                    stringItems + "\n"
        return string
    }

    /**
     * @return A java JSONObject representing this item
     */
    fun toJSONObject(): JSONObject {
        val listJSON = JSONObject()
        listJSON.put("titreListe", titreListeToDo)

        val lesItemsJSON = JSONArray()
        for (item in lesItems) {
            lesItemsJSON.put(item.toJSONObject())
        }
        listJSON.put("items", lesItemsJSON)

        return listJSON
    }

    /**
     * @return A string containing JSON text corresponding to this item
     */
    fun toJSONString(): String {
        val listJSON = toJSONObject()
        return listJSON.toString()
    }
}