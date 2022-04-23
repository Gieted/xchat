package pl.pawelkielb.xchat

import kotlin.reflect.KProperty1


inline fun <reified T : Any> resource(identifier: KProperty1<T, *>, children: Set<Resource> = emptySet()): Resource {
    val theClass = T::class

    val key = theClass.simpleName?.lowercase()?.plus("s")
        ?: throw IllegalArgumentException("Cannot create resource from an anonymous class")

    
    /*val properties = theClass.memberProperties.map {
        val type = when (it.returnType) {
            Int::class -> Resource.Property.Type.Integer()
            String::class -> Resource.Property.Type.String()
            else -> TODO()
        }
    }

    TODO()*/

    return Resource(Resource.Property("id", Resource.Property.Type.String()), emptySet(), emptyMap(), Any::class)
}
