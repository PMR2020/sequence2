package fr.ec.app.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.ec.app.R
import fr.ec.app.data.DataProvider
import fr.ec.app.data.model.Post
import fr.ec.app.ui.main.adapter.ItemAdapter
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), ItemAdapter.ActionListener {

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("MainActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )
    private val adapter = newAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list: RecyclerView = findViewById(R.id.list)
        val progress: ProgressBar = findViewById(R.id.progressbar)

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        loadPost(progress, list)
    }


    private fun loadPost(
        progress: ProgressBar,
        list: RecyclerView
    ) {
        activityScope.launch {
            progress.visibility = View.VISIBLE
            list.visibility = View.GONE

            // main
            val posts = DataProvider.getPostFromApi()
            // main
            adapter.showData(posts)
            progress.visibility = View.GONE
            list.visibility = View.VISIBLE

        }

    }

    override fun onDestroy() {
        activityScope.cancel()
        super.onDestroy()
    }

    private fun newAdapter(): ItemAdapter {
        val adapter = ItemAdapter(
            actionListener = this
        )
        return adapter
    }

    override fun onItemClicked(post: Post) {
        Log.d("MainActivity", "onItemClicked $post")
        Toast.makeText(this, post.title, Toast.LENGTH_LONG).show()
    }

}


