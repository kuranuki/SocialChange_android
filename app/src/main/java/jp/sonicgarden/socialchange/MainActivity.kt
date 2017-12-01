package jp.sonicgarden.socialchange

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.blog_post_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var adapter: BlogPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()
        adapter = BlogPostAdapter()

        recyclerViewBlogPost.let {
            val linearLayoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.layoutManager = linearLayoutManager
            it.addItemDecoration(DividerItemDecoration(this, linearLayoutManager.orientation))
            it.adapter = adapter
        }

        SocialChange.getPosts(1, {
            val blogPostList = BlogPostModel.findAll(realm)
            if (blogPostList.isNotEmpty()) adapter.updateData(blogPostList)

            Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show()
        }, { error ->
            Log.v("kuranuki", error.toString())
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    class BlogPostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.textViewTitle
        val textViewDate: TextView = view.textViewDate
    }

    class BlogPostAdapter : RealmRecyclerViewAdapter<BlogPostModel, BlogPostViewHolder>(null, true) {

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BlogPostViewHolder {
            val inflater = LayoutInflater.from(parent?.context)
            return BlogPostViewHolder(inflater.inflate(R.layout.blog_post_row, parent, false))
        }

        override fun onBindViewHolder(holder: BlogPostViewHolder?, position: Int) {
            data?.get(position)?.let {
                holder?.apply {
                    textViewTitle.text = it.title
                    textViewDate.text = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.JAPAN).format(it.date)
                }
            }
        }

    }
}
