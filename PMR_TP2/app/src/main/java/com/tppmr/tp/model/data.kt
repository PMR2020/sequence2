package com.tppmr.tp.model

data class LoginResponse(
    val version: Int,
    val success: Boolean,
    val status: Int,
    val hash: String
)

data class UsersResponse(
    val version: Int,
    val success: Boolean,
    val status: Int,
    val users: List<User>
)

data class User(
    val id: Int,
    val pseudo: String,
    val pass: String,
    val hash: String
)

data class ListsResponse(
    val version: Int,
    val success: Boolean,
    val status: Int,
    val lists: MutableList<Liste>
)

data class Liste(
    val id: Int,
    val idUser: Int,
    val label: String
)

data class ItemsResponse(
    val version: Int,
    val success: Boolean,
    val status: Int,
    val items: MutableList<Item>
)

data class Item(
    val id: Int,
    val idList: Int,
    val label: String,
    val checked: Int,
    val url: String
)
