package jp.sonicgarden.socialchange

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()

        button.setOnClickListener {
            SocialChange.getPosts(1, {

                Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show()

                realm.where(BlogPostModel::class.java).findAll().forEach {
                    Log.v("kuranuki", it.title)
                }

                Log.v("kuranuki", "success")
            }, { error ->
                Log.v("kuranuki", error.toString())
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
