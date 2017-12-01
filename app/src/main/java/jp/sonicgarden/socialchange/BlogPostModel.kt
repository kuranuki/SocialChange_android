package jp.sonicgarden.socialchange

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.Sort
import io.realm.annotations.PrimaryKey
import java.util.*

open class BlogPostModel : RealmObject() {

    companion object {
        fun find(realm: Realm, id: String) =
                realm.where(BlogPostModel::class.java).equalTo(BlogPostModel::id.name, id).findFirst()

        fun findAll(realm: Realm, sort: Sort = Sort.DESCENDING): RealmResults<BlogPostModel> =
                realm.where(BlogPostModel::class.java).findAll().sort(BlogPostModel::date.name, sort)
    }

    @PrimaryKey
    open var id: String = ""
    open var date: Date = Date()
    open var link: String = ""
    open var title: String = ""
    open var content: String = ""
}