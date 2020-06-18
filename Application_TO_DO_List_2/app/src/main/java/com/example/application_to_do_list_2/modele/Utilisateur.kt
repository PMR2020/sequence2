package com.example.application_to_do_list_2.modele

// MODIFICATION DE LA STRUCTURE PAR RAPPORT A LA SEQUENCE 1 POUR FAIRE APPARAITRE ID
class Utilisateur (var login : String="", var listes: MutableList<Liste> = emptyList<Liste>().toMutableList(), var active:Boolean=false){
    val id : Int

    init{
        this.id=
            utilisateurs.size
        utilisateurs.add(this)
    }

    fun getListe(id : Int) : Liste {
        for (liste in listes){
            if(liste.id==id){
                return liste
            }
        }
        return Liste(0)
    }


    companion object {
        var utilisateurs =emptyList<Utilisateur>().toMutableList()
        fun getUser(pseudo : String ) : Utilisateur {
            for (u in utilisateurs) {
                if(u.login==pseudo) return u
            }
            return Utilisateur(pseudo)
        }
    }


}