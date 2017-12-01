package jp.sonicgarden.socialchange

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class BlogPostModel : RealmObject() {

    companion object {
        //
    }

    @PrimaryKey
    open var id: String = ""
    open var date: Date = Date()
    open var link: String = ""
    open var title: String = ""
    open var content: String = ""
}