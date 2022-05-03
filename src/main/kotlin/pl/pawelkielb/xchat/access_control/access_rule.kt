package pl.pawelkielb.xchat.access_control


class AccessRuleScope(val grantedPermissions: MutableSet<Permission> = mutableSetOf()) {
    fun grantAccess(vararg permissions: Permission) {
        grantedPermissions.addAll(permissions)
    }
}
