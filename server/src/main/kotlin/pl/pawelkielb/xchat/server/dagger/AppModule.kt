package pl.pawelkielb.xchat.server.dagger

import dagger.Module
import dagger.Provides
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    @Singleton
    fun db(): CoroutineDatabase {
        return KMongo.createClient().coroutine.getDatabase("xchat")
    }
}
