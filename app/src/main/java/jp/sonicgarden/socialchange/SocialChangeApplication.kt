package jp.sonicgarden.socialchange

import android.app.Application
import io.realm.Realm

class SocialChangeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(applicationContext)
    }
}