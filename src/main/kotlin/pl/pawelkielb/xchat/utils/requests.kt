package pl.pawelkielb.xchat.utils

import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.toId
import pl.pawelkielb.xchat.access_control.AccessRule
import pl.pawelkielb.xchat.access_control.AccessRuleScope
import pl.pawelkielb.xchat.access_control.Permission
import pl.pawelkielb.xchat.access_control.ResourceAccessDeniedException
import pl.pawelkielb.xchat.ChannelData
import pl.pawelkielb.xchat.Data
import pl.pawelkielb.xchat.UserData
import pl.pawelkielb.xchat.model.Channel
import pl.pawelkielb.xchat.model.Model
import pl.pawelkielb.xchat.model.User

fun Data.toModel(db: CoroutineDatabase) = when (this) {
    is UserData -> User(id, db)
    is ChannelData -> Channel(id, db)
    else -> TODO()
}

suspend inline fun <reified T : Data> CoroutineDatabase.listResource(accessingUserId: String?): List<T> {
    val db = this
    val collection = when (T::class) {
        ChannelData::class -> "channels"
        UserData::class -> "users"
        else -> throw AssertionError()
    }

    val data = db.getCollection<T>(collection).find().toList()

    return data.filter {
        val model = it.toModel(db)
        hasPermission(permission = Permission.Read, accessingUserId, resource = model, db)
    }.ifEmpty { throw ResourceAccessDeniedException() }
}

suspend fun <T : Model> hasPermission(
    permission: Permission,
    accessingUserId: String?,
    resource: T,
    db: CoroutineDatabase
): Boolean {
    if (accessingUserId?.lowercase() == "admin") {
        return true
    }

    val user = if (accessingUserId != null) User(accessingUserId.toId(), db) else null
    val accessRuleScope = AccessRuleScope()
    
    @Suppress("UNCHECKED_CAST")
    val accessRule = resource.accessRule as AccessRule<T>
    
    accessRule.invoke(accessRuleScope, user, resource)

    return permission in accessRuleScope.grantedPermissions
}
