package jp.sonicgarden.socialchange

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            SocialChange.getPost("25918", {
                Log.v("kuranuki", "success")
            }, { error ->
                Log.v("kuranuki", error.toString())
            })
        }
    }
}
