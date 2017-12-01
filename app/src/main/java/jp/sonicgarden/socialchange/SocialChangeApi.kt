package jp.sonicgarden.socialchange

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

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
}