package fr.ec.app.data.api

import fr.ec.app.data.model.PostsResponse
import retrofit2.http.GET


interface ServiceProductHunt {

    @GET("v1/posts?access_token=46a03e1c32ea881c8afb39e59aa17c936ff4205a8ed418f525294b2b45b56abb")
    suspend fun getPostsResponse() : PostsResponse

}