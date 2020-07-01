package com.example.todoapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.data.JsonManager
import com.example.todoapp.data.ToDoProfile
import java.io.File

abstract class AbstractActivity : AppCompatActivity() { //activité abstraite qui gère le sytème de menu et dont toutes les activités héritent

    protected var user = ToDoProfile() //empty user
    protected var jsonManager = JsonManager()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_settings -> changeToSettingsActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeToSettingsActivity(){
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent) //execution de l'intent
    }

    protected fun setUserParameters(){//regarde si l'user existe déjà dans la sauvegarde, si oui récupère l'objet depuis le json
        val sharedPreferences : SharedPreferences = getSharedPreferences(MainActivity.userPref, Context.MODE_PRIVATE)
        user.pseudo = sharedPreferences.getString(MainActivity.userPseudoKey, MainActivity.defaultPseudoValue) ?: MainActivity.defaultPseudoValue

        val userFile = File(this.filesDir, user.pseudo)
        if(!userFile.isFile){
            Log.e("TODOTEST", "${user.pseudo} ne correspond pas à un file : ")
            return
        }

        if (userFile.exists() && userFile.canRead()){ //si l'utilisateur est connu
            user = jsonManager.fromFileToUser(userFile) // on lit l'user depuis le profile
            Log.e("TODOTEST", "extraction de : $user")
        }
        else{
            jsonManager.fromUserToFile(user, this) // et on le sauvegarde
            Log.e("TODOTEST", "user inexistant : $user")
        }

    }
}