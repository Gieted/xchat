package pl.pawelkielb.xchat.dagger

import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule
import org.litote.kmongo.reactivestreams.KMongo
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun database() =  KMongo.createClient().coroutine.getDatabase("xchat")
}
