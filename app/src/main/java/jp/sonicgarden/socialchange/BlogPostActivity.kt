package jp.sonicgarden.socialchange

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_blog_post.*

class BlogPostActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var blogPostModel: BlogPostModel

    companion object {
        fun newIntent(context: Context, postId: String): Intent {
            val intent = Intent(context, BlogPostActivity::class.java)
            intent.putExtra("postId", postId)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_post)

        realm = Realm.getDefaultInstance()
        val postId = intent.getStringExtra("postId")
        blogPostModel = BlogPostModel.find(realm, postId) ?: BlogPostModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onResume() {
        super.onResume()
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadDataWithBaseURL(null, blogPostModel.content, "text/html", "UTF8", null)
    }
}
