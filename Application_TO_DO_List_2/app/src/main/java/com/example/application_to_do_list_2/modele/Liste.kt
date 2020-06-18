package com.example.application_to_do_list_2.modele

import android.util.Log

// MODIFICATION DE LA STRUCTURE PAR RAPPORT A LA SEQUENCE 1 POUR FAIRE APPARAITRE ID
class Liste(val id:Int,var label : String="Non précisé",var active:Boolean=false){
    init{
        var items : MutableList<Item> = mutableListOf()
    }

}