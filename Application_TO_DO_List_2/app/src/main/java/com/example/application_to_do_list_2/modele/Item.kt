package com.example.application_to_do_list_2.modele

// MODIFICATION DE LA STRUCTURE PAR RAPPORT A LA SEQUENCE 1 POUR FAIRE APPARAITRE ID
class Item(var id:Int,var label : String = "Non précisé", var checked : Int = 0 ) {
    fun chgtStatut(){
        checked = 1-checked
    }
}