package fr.ec.app.data

import com.google.gson.Gson
import fr.ec.app.data.model.Post
import fr.ec.app.data.model.PostsResponse
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL


object DataProvider {
    val POST_API_URL =
        "https://api.producthunt.com/v1/posts?access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb"

    val gson = Gson()
    fun getPostFromApi(): List<Post> {

        val json = makeCall()
        val postsResponse = gson.fromJson(json, PostsResponse::class.java)
        return postsResponse.posts
    }

    private fun makeCall(): String? {
        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        try {
            urlConnection = URL(POST_API_URL).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            reader = urlConnection.inputStream?.bufferedReader()
            return reader?.readText()

        } finally {
            urlConnection?.disconnect()
            reader?.close()
        }
    }
}