package jp.sonicgarden.socialchange

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SocialChangeApi {

    data class PostResponse(
            val id: String,
            val date: String,
            val link: String,
            val title: Title,
            val content: Content
    ) {
        data class Title(val rendered: String)
        data class Content(val rendered: String)
    }

    @GET("posts/{id}")
    fun getPost(@Path("id") id: String): Single<PostResponse>

    @GET("posts")
    fun getPosts(@Query("page") page: Int = 1,
                 @Query("per_page") per_page: Int = 100)
            : Single<Response<List<PostResponse>>>
}