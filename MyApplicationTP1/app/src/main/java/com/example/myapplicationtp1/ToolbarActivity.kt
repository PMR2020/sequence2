package com.example.myapplicationtp1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.myapplicationtp1.R
import com.example.myapplicationtp1.R.*
import com.example.myapplicationtp1.R.id.*
import com.example.myapplicationtp1.R.layout.*
import com.example.myapplicationtp1.SettingsActivity
import com.example.myapplicationtp1.MainActivity.Companion.pseudo

abstract class ToolbarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_toolbar)
    }

    fun setupToolBar() {
        val toolbar = findViewById<Toolbar>(toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.putExtra("Pseudo",pseudo)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}