package pl.pawelkielb.xchat.access_control

import pl.pawelkielb.xchat.model.User

class AccessRuleScope(val grantedPermissions: MutableSet<Permission> = mutableSetOf()) {
    fun grantAccess(vararg permissions: Permission) {
        grantedPermissions.addAll(permissions)
    }
}

typealias AccessRule<T> = suspend AccessRuleScope.(User?, T) -> Unit
