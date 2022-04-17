package pl.pawelkielb.xchat

import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties


fun main() {
    val users = resource<User> {
        property(User::channels, isReference = true)
    }
}

class ResourceConfig {
    private data class PropertyData(val isReference: Boolean)

    private val propertyData = mutableMapOf<KProperty<*>, PropertyData>()

    fun property(property: KProperty<*>, isReference: Boolean) {
        propertyData[property] = PropertyData(isReference)
    }
}

inline fun <reified T : Any> resource(noinline configure: (ResourceConfig.() -> Unit)? = null): Resource {
    val theClass = T::class

    val config = configure?.invoke(ResourceConfig())

    val key = theClass.simpleName?.lowercase()?.plus("s")
        ?: throw IllegalArgumentException("Cannot create resource from an anonymous class")

    val properties = theClass.memberProperties.map {
        val type = when (it.returnType) {
            Int::class -> Resource.Property.Type.Integer()
            String::class -> Resource.Property.Type.String()
            else -> TODO()   
        }
    }
    
    TODO()
} 
