package pl.pawelkielb.xchat

import kotlinx.serialization.json.Json
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule

val json = Json { serializersModule = IdKotlinXSerializationModule }
