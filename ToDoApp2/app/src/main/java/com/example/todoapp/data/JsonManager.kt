package com.example.todoapp.data

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import java.io.File
import java.io.InputStream

class JsonManager {
    private var gson = GsonBuilder().setPrettyPrinting().create()

    fun fromFileToUser(file : File) : ToDoProfile{
        val inputStream : InputStream = file.inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }
        inputStream.close()
        return gson.fromJson(inputString, ToDoProfile::class.java)
    }

    fun fromUserToFile(user : ToDoProfile, context : Context){
        val filename = user.pseudo
        val fileContents = gson.toJson(user)
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }
}