package jp.sonicgarden.socialchange

import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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

    fun getPost(id: String,
                onSuccess: () -> Unit, onError: (e: Throwable) -> Unit) {

        getApi().getPost(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ postResponse ->
                    // TODO:
                    Log.v("kuranuki", postResponse.toString())
                    onSuccess()
                }, { error ->
                    onError(error)
                })
    }
}