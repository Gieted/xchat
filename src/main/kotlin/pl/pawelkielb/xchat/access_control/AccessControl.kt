package pl.pawelkielb.xchat.access_control

import pl.pawelkielb.xchat.Ref
import pl.pawelkielb.xchat.Resource
import pl.pawelkielb.xchat.services.ResourceManager


class AccessControl(private val resourceManager: ResourceManager) {

    @Suppress("UNCHECKED_CAST")
    suspend fun <T : Any> hasPermission(
        participant: Participant,
        resource: Resource,
        data: T,
        permission: Permission
    ): Boolean {
        if (participant.isAdmin) {
            return true
        }
        
        val accessRule = accessRules[resource] as AccessRule<T>?
            ?: throw IllegalArgumentException("Unknown resource: $resource")
        
        val accessRuleScope = AccessRuleScope()

        return permission in accessRuleScope.grantedPermissions
    }

    inner class AccessRuleScope(val grantedPermissions: MutableSet<Permission> = mutableSetOf()) {
        suspend fun <T : Any> Ref<T, *>.get() = with(resourceManager) {
            get()
        }

        fun grantAccess(vararg permissions: Permission) {
            grantedPermissions.addAll(permissions)
        }
    }
}
