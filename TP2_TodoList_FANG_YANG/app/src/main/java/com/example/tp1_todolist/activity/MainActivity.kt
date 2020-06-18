package com.example.tp1_todolist.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tp1_todolist.R
import com.example.tp1_todolist.data.model.DataProvider
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    private val activityScope= CoroutineScope(
        SupervisorJob()// 用于cancel的单向传播，和常规的Job唯一的不同是：SupervisorJob 的取消只会向下传播
                + Dispatchers.Default
                + CoroutineExceptionHandler{_,throwable->
            Log.e("MainActivity","${throwable.message}")
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        this.title="答答清单"

        val editTextPseudo:EditText=findViewById(R.id.editTextPseudo)
        val editTextPassword:EditText=findViewById(R.id.editTextPassword)

        val sharedPreference = getSharedPreferences("Setting", Context.MODE_PRIVATE)
        val pseudo=sharedPreference.getString("pseudo",null)
        val password=sharedPreference.getString("password",null)
        if(pseudo!=null && password!=null){
            editTextPseudo.setText(pseudo)
            editTextPassword.setText(password)
        }

        val btnMainOK:Button=findViewById(R.id.btnMainOK)
        btnMainOK.setOnClickListener(){
            //check internet conncetion
            val connectivityManager=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo=connectivityManager.activeNetworkInfo
            if(networkInfo!=null && networkInfo.isConnected){
                Log.i("MainActivity","btnMainOK clicked")
                val pseudo=editTextPseudo.getText().toString()
                val password=editTextPassword.getText().toString()
                login(pseudo,password)
            }else{
                val t = Toast.makeText(MainActivity(), "No Internet Connection", Toast.LENGTH_SHORT)
                t.show()
            }
        }
    }


     /*
     *demander une identification auprès de l’API en
     *lui envoyant pseudo/mot de pass een faisant une requête @POST authenticate
     */
    private fun login(pseudo:String, password:String){
        activityScope.launch{
        val hash=DataProvider.authenticate(pseudo,password)
        withContext(Dispatchers.Main){
            if (hash!=null){
                Log.i("MainActivity","Request Success")
                Log.i("MainActivity","hash = ${hash}")
                //Récupérer le token d’identification sous forme d’un hash et le stocker dans
                //les préférences de l’application
                val sharedPreference = getSharedPreferences("Setting", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                    editor.putString("hash",hash)
                    editor.putString("pseudo",pseudo)
                    editor.putString("password",password)
                    editor.commit()
                val intentToChoixList:Intent=Intent(this@MainActivity,ChoixListActivity::class.java)
                startActivity(intentToChoixList)
            }else{
                Log.i("MainActivity","Failure")
                val t = Toast.makeText(MainActivity(), "Pseudo or password incorrect", Toast.LENGTH_SHORT)
                t.show()
            }
        }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            //un menu “setting” dans toutes les activités, qui fera venir à l’activité de setting
            R.id.action_settings -> {
                val intentToSetting:Intent=Intent(this@MainActivity,SettingsActivity::class.java)
                startActivity(intentToSetting)
                true
            }
            //un menu “déconnexion” dans toutes les activités, qui fera revenir à l’activité de connexion
            R.id.action_logout -> {
                val t = Toast.makeText(this, "You have been logout", Toast.LENGTH_SHORT)
                t.show()
                val sharedPreference = getSharedPreferences("Setting", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                    editor.remove("pseudo")
                    editor.remove("password")
                    editor.commit()
                val intentToConnection:Intent=Intent(this@MainActivity,MainActivity::class.java)
                startActivity(intentToConnection)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
