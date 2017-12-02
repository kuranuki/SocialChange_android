package jp.sonicgarden.socialchange

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

object SocialChange {

    private val httpClient = OkHttpClient()
    private val converterFactory = GsonConverterFactory.create(
            GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    )!!

    private fun getApi() = Retrofit.Builder()
            .baseUrl("https://kuranuki.sonicgarden.jp/wp-json/wp/v2/")
            .client(httpClient.newBuilder().build())
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(SocialChangeApi::class.java)

    fun loadPosts(realm: Realm, onSuccess: () -> Unit, page: Int = 1) {
        getApi().getPosts(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    storeBlogPosts(realm, response.body())

                    val totalPages = response.headers().get("X-WP-TotalPages")!!.toInt()
                    if (page < totalPages) loadPosts(realm, onSuccess, page + 1) else onSuccess()
                }, { error ->
                    Log.v("socialchange", error.toString())
                })
    }

    private fun storeBlogPosts(realm: Realm, list: List<SocialChangeApi.PostResponse>?) {
        list?.forEach { post ->
            realm.executeTransaction {
                val blogPostModel = BlogPostModel().apply {
                    id = post.id
                    date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.JAPAN).parse(post.date)
                    link = post.link
                    title = post.title.rendered
                    content = post.content.rendered
                }
                realm.copyToRealmOrUpdate(blogPostModel)
            }
        }
    }

    private fun loadOgpImage(blogPostModel: BlogPostModel) {
        Single.create(SingleOnSubscribe<String> { emitter ->
            val url = Jsoup.connect(blogPostModel.link)
                    .get()
                    .select("meta[property=og:image]")
                    .attr("content")
            emitter.onSuccess(url)
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ imageUrl ->
                    blogPostModel.imageUrl = imageUrl
                }, {
                    // noop
                })
    }
}