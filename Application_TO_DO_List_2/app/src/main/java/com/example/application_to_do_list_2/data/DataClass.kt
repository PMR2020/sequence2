package com.example.application_to_do_list_2.data

import com.example.application_to_do_list_2.modele.Item
import com.example.application_to_do_list_2.modele.Liste

data class DataUser (val hash:String) // UTILISATEUR

data class DataLists (val lists : List<Liste>) // LISTE DES LISTES

data class DataItems (val items:List<Item>) // LISTE DES ITEMS D'UNE LISTE

data class DataItem (val item: Item) // ITEM