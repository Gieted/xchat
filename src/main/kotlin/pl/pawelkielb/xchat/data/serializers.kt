package pl.pawelkielb.xchat.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

object AnySerializer : KSerializer<Any> {
    override val descriptor = PrimitiveSerialDescriptor("Any", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Any = decoder.decodeString()

    override fun serialize(encoder: Encoder, value: Any) {
        TODO("Not yet implemented")
    }
}

