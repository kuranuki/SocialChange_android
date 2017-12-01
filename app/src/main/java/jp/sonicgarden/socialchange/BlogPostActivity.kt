package jp.sonicgarden.socialchange

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_blog_post.*

class BlogPostActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    companion object {
        fun newIntent(context: Context, postId: String): Intent {
            val intent = Intent(context, BlogPostActivity::class.java)
            intent.putExtra("postId", postId)
            return intent
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_post)

        realm = Realm.getDefaultInstance()
        val postId = intent.getStringExtra("postId")
        val blogPostModel = BlogPostModel.find(realm, postId)

        webView.settings.javaScriptEnabled = true
        webView.loadDataWithBaseURL(null, blogPostModel?.content, "text/html", "UTF8", null)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
