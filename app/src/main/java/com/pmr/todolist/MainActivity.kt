package com.pmr.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.pmr.todolist.choixlist.ChoixListActivity

class MainActivity : AppCompatActivity() {
    var pseudo : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val okBtn = findViewById<Button>(R.id.okButton)
        val pseudoEdit = findViewById<EditText>(R.id.pseudoEdit)

        okBtn.setOnClickListener {
            pseudo = pseudoEdit.text.toString()

            val bundle = Bundle()
            bundle.putString("pseudo", pseudoEdit.text.toString())
            val intent = Intent(this, ChoixListActivity::class.java)
            intent.putExtras(bundle)

            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)

        return super.onOptionsItemSelected(item)
    }
}
