package com.example.tp1_todolist.data.model

import com.example.tp1_todolist.model.ItemList
import com.example.tp1_todolist.model.ItemTask
import com.example.tp1_todolist.model.User

class ReponseApi {
//    {"version":1,"success":true,"status":202,"hash":"3f42b18b7f71498b166d1662848a5bec"}
//
//    {"version":1,"success":true,"status":200,"lists":[{"id":"2","label":"list_tom"},{"id":"465","label":"Nouvelle
//        Liste"},{"id":"466","label":"Nouvelle Liste"},{"id":"467","label":"test"},{"id":"468","label":"Nouvelle
//        Liste"},{"id":"469","label":"list6_tom"}]}
//
//        {"version":1,"success":true,"status":200,"users":[{"id":"2","pseudo":"isa"},{"id":"1","pseudo":"tom"},{"id":"79","pseudo":"toto"}]}
//
//        {"version":1,"success":true,"status":200,"list":[]}
//
//        {"version":1,"success":true,"status":200,"items":[]}
//
//        {"version":1,"success":true,"status":200,"item":[]}

    //{"version":1,"success":true,"status":201,"item":{"id":"1059","label":"Nouvel item","checked":"0","url":null}}
    var version:Int=1
    var success:Boolean = false
    var status:Int=400
    var hash:String?=null
    var users:MutableList<User>?=null
    var lists:MutableList<ItemList>?=null
    var list:ItemList?=null
    var items:MutableList<ItemTask>?=null
    var item: ItemTask?=null

}