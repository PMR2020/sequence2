package com.example.todolistfinal.network

data class Hash(
    val hash: String)

data class ListfromUser(
    val id: Int,
    val label: String)

data class ItemDescription(
    val id: Int,
    val label: String,
    val checked: Int)

data class ListsofLists(
    val lists: List<ListfromUser>)

data class ListofItems(
    val items: List<ItemDescription>)