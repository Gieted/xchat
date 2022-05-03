package pl.pawelkielb.xchat.server.access_control


class AccessRuleScope(val grantedPermissions: MutableSet<Permission> = mutableSetOf()) {
    fun grantAccess(vararg permissions: Permission) {
        grantedPermissions.addAll(permissions)
    }
}
