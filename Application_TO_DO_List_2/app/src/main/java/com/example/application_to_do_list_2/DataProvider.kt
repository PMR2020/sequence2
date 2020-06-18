package com.example.application_to_do_list_2

import com.example.application_to_do_list_2.api.ServiceAPI
import com.example.application_to_do_list_2.modele.Liste
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object DataProvider {
    // 10.0.2.2 EST L'IP DE L'EMULATEUR
    var URL = "http://10.0.2.2/todo-api/"

    // ON ACCEDE AU SERVICE API
    private var service = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ServiceAPI::class.java)

    // POUR REDEMARRER LE SERVICE
    fun reStartService() {
        service = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServiceAPI::class.java)
    }

    suspend fun getListsFromApi(hash:String) : List<Liste> = service.getListProfils(hash).lists

    suspend fun getHashFromApi(pseudo:String,password:String):String = service.getHash(pseudo,password).hash

    suspend fun getItemsFromApi( requete:String,hash:String)=
        service.getItems(requete,hash).items

    suspend fun checkItemFromApi( requete:String,check:Int,hash:String)=
        service.checkItem(requete,check,hash).item

    suspend fun createItemFromApi( requete:String,label:String,hash:String)=
        service.createItem(requete,label,hash).item

}
