package com.example.tp1_todolist.data.api

import com.example.tp1_todolist.data.model.ReponseApi
import retrofit2.Call
import retrofit2.http.*

interface ServiceTodoApi {
    //Connexion auprès de l'API Renvoie un hash sans délai d'expiration
    @POST("authenticate?")
    suspend fun authenticate(@Query("user") pseudo:String, @Query("password") password:String): ReponseApi

    //Récupération des listes de l'utilisateur courant Le hash peut être fourni dans les entêtes
    @GET("lists")
    suspend fun getLists(@Header("hash") myHash:String):ReponseApi

    //Création de l'utilisateur toto (mot de passe tata)
    @POST("users")
    suspend fun createUser(@Header("hash") hash:String, @Query("pseudo") myPseudo:String, @Query("pass") myPassword:String):ReponseApi

    //Modification du mot de passe de l'utilisateur d'identifiant 2
    @PUT("/users/{id}")
    suspend fun modifierPassword(@Header("hash") hash:String,@Path("id") id:Int,@Query("pass") newPassword:String):ReponseApi

    //Liste dont l'identifiant vaut 2 de l'utilisateur connecté
    @GET("lists/{idList}")
    suspend fun getOneList(@Header("hash") myHash:String,@Path("idList") idList:Int):ReponseApi

    //Création d'une liste pour l'utilisateur connecté
    @POST("lists")
    suspend fun createList(@Header("hash") hash:String, @Query("label") labelList:String):ReponseApi

    //Modification du label de la liste 1 de l'utilisateur connecté
    @PUT("lists/{idList}")
    suspend fun modifierLabelList(@Header("hash") hash:String, @Path("idList") idList:Int, @Query("label") labelList:String):ReponseApi


    //Suppression de la liste d'identifiant 7 de l'utilisateur connecté
    @DELETE("lists/{idList}")
    suspend fun deleteList(@Header("hash") hash:String, @Path("idList") idList:Int):ReponseApi

    //get Items de la liste 3
    @GET("lists/{idList}/items")
    suspend fun getItems(@Header("hash") myHash:String,@Path("idList") idList:Int):ReponseApi

    //get Item d'identifiant 4 de la liste d'identifiant 3
    @GET("lists/{idList}/items/{idItem}")
    suspend fun getOneItem(@Header("hash") myHash:String,@Path("idList") idList:Int,@Path("idItem") idItem:Int):ReponseApi

    //Création d'un nouvel item dans la liste d'identifiant 3
    @POST("lists/{idList}/items")
    suspend fun createItem(@Header("hash") hash:String, @Path("idList") idList:Int, @Query("label") labelItem:String):ReponseApi

    //Cocher l'item d'identifiant 4 de la liste d'identifiant 3
    @PUT("lists/{idList}/items/{idItem}")
    suspend fun checkItem(@Header("hash") hash:String, @Path("idList") idList:Int,@Path("idItem") idItem:Int, @Query("check") check:Int):ReponseApi

    //Changer le label de l'item d'identifiant 4 de la liste d'identifiant 3
    @PUT("lists/{idList}/items/{idItem}")
    suspend fun changerItemLabel(@Header("hash") hash:String, @Path("idList") idList:Int,@Path("idItem") idItem:Int, @Query("label") labelItem:String):ReponseApi

    //Supprimer l'item d'identifiant 5 de la liste d'identifiant 1
    @DELETE("lists/{idList}/items/{idItem}")
    suspend fun deleteItem(@Header("hash") myHash:String,@Path("idList") idList:Int,@Path("idItem") idItem:Int):ReponseApi

}
