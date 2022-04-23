package pl.pawelkielb.xchat.services

import org.bson.types.ObjectId
import org.litote.kmongo.id.toId
import pl.pawelkielb.xchat.Key
import pl.pawelkielb.xchat.Ref
import pl.pawelkielb.xchat.Resource
import pl.pawelkielb.xchat.access_control.AccessControl
import pl.pawelkielb.xchat.access_control.Participant
import pl.pawelkielb.xchat.data.ApplicationContext
import pl.pawelkielb.xchat.data.topLevelResources
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.NoSuchElementException
import kotlin.reflect.full.createInstance


@Singleton
class ResourceManager @Inject constructor(private val applicationContext: ApplicationContext) {
    private val accessControl = AccessControl(this)

    private fun findResource(key: String): Resource? {
        var resource: Resource? = null
        var resourceMap: Map<String, Resource>? = topLevelResources
        for (pathElement in key.drop(1).split("/")) {
            if (resourceMap == null) {
                return null
            }

            resource = resourceMap[pathElement]
            resourceMap = resource?.children
        }

        return resource
    }

    suspend fun list(key: String, participant: Participant): List<Any?> {
        Key.validate(key)

        if (!Key.targetsGroup(key)) {
            throw IllegalArgumentException("Key must target a resource group")
        }


        val resource = findResource(key) ?: throw NoSuchElementException()

        with(applicationContext) {
            val channel = Channel(ObjectId("6263e586d135cc0d33f7de32").toId())

            return listOf(channel.data())
        }
    }

    private fun documentToObject(document: Map<String, Any>, resource: Resource): Any {
        resource.type.createInstance()
        TODO()
    }

    fun <T : Any> get(key: String): T? {
        TODO()
    }

    fun modify() {

    }

    fun <T : Any> Ref<T, *>.get(): T {
        TODO()
    }
}
