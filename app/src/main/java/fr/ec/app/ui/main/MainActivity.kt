package fr.ec.app.ui.main

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.ec.app.R
import fr.ec.app.ui.main.adapter.ItemAdapter
import fr.ec.app.data.model.Post

class MainActivity : AppCompatActivity(), ItemAdapter.ActionListener {

    private val adapter = newAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list: RecyclerView = findViewById(R.id.list)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)

        val dataSet = mutableListOf<Post>()

        repeat(100_000) {
            dataSet.add(Post("Title $it","SubTitle $it"))
        }

        adapter.showData(dataSet)
    }

    private fun newAdapter(): ItemAdapter {

        val adapter = ItemAdapter(
            actionListener = this
        )
        return adapter
    }

    override fun onItemClicked(post: Post) {
        Log.d("MainActivity", "onItemClicked $post")
        Toast.makeText(this,post.title,Toast.LENGTH_LONG).show()
    }

}


